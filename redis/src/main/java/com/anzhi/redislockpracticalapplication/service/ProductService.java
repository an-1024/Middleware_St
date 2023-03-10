package com.anzhi.redislockpracticalapplication.service;

import com.alibaba.fastjson.JSON;
import com.anzhi.redislockpracticalapplication.common.RedisKeyPrefixConst;
import com.anzhi.redislockpracticalapplication.common.RedisUtil;
import com.anzhi.redislockpracticalapplication.dao.ProductMapper;
import com.anzhi.redislockpracticalapplication.entity.Product;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private Redisson redisson;
    
    // 缓存过期时间 24 小时
    private static final Integer PRODUCT_CACHE_TIMEOUT = 60 * 60 * 24;
    
    private static final String EMPTY_CACHE = "{}";
    
    public Product createProduct(Product product){
        RLock createProductLock = redisson.getLock(RedisKeyPrefixConst.INCONSISTENT_DOUBLE_WRITE_DATA_CACHE + product.getId());
        createProductLock.lock();
        Product createResultProduct = null;
        try {
            // 创建商品
            createResultProduct = productMapper.create(product);
            // 设置缓存
            redisUtil.set(RedisKeyPrefixConst.PRODUCT_CACHE + createResultProduct.getId(), JSON.toJSONString(createResultProduct),
                    generateProductCacheTimeOut(), TimeUnit.SECONDS);
        }finally {
            createProductLock.unlock();
        }
        return createResultProduct;
    }
    
    public Product updateProduct(Product product){
        RLock updateProductLock = redisson.getLock(RedisKeyPrefixConst.PRODUCT_CACHE + product.getId());
        Product updateResultProduct = null;
        try {
             updateResultProduct = productMapper.update(product);
            redisUtil.set(RedisKeyPrefixConst.PRODUCT_CACHE + updateResultProduct.getId(), JSON.toJSONString(updateResultProduct),
                    generateProductCacheTimeOut(), TimeUnit.SECONDS);
        }finally {
            updateProductLock.unlock();
        }
        return updateResultProduct;
    }
    
    public Product getProduct(Long productId){
        Product product = null;
        String productCacheKey = RedisKeyPrefixConst.PRODUCT_CACHE + productId;
        String hotProductLockCacheKey = RedisKeyPrefixConst.HOT_PRODUCT_LOCK_CACHE + productId;
        String inconsistentDoubleWriteDataCacheKey = RedisKeyPrefixConst.INCONSISTENT_DOUBLE_WRITE_DATA_CACHE + productId;

        product = getProductFromCache(productCacheKey);
        if(!Objects.isNull(product)){
            return product;
        }
        RLock hotProductLockCache = redisson.getLock(hotProductLockCacheKey);
        hotProductLockCache.lock();
        try {
            // product 分为两种情况，一种是有商品信息；另一种是黑客攻击没有商品信息
            // 需要和前端约定如何处理。
            product = getProductFromCache(productCacheKey);
            if(!Objects.isNull(product)){
                return product;
            }
            return getProductFromData(productCacheKey, productId, inconsistentDoubleWriteDataCacheKey);
        }finally {
            hotProductLockCache.unlock();
        }
        
    }
    
    private Integer generateProductCacheTimeOut(){
        return PRODUCT_CACHE_TIMEOUT + new Random().nextInt(5) * 60 * 60;
    }


    private Integer generateEmptyCacheTimeOut(){
        return new Random().nextInt(60);
    }
    
    private Product getProductFromCache(String productCacheKey){
        Product product = null;
        String productJsonString = redisUtil.get(productCacheKey);
        if(!StringUtils.isEmpty(productJsonString)){
            if(EMPTY_CACHE.equals(productJsonString)){
                // 防止缓存穿透的攻击都是同一个商品，所以加锁延时
                redisUtil.expire(productCacheKey, generateEmptyCacheTimeOut(), TimeUnit.SECONDS);
                // 返回一个没有任何信息的商品表示是缓存穿透
                return new Product();
            }
            redisUtil.expire(productCacheKey, generateProductCacheTimeOut(), TimeUnit.SECONDS);
            product = JSON.parseObject(productJsonString, Product.class);
        }
        return product;
    }
    
    
    private Product getProductFromData(String productCacheKey, Long productId, String inconsistentDoubleWriteDataCacheKey){
        RLock getProductLock = redisson.getLock(inconsistentDoubleWriteDataCacheKey);
        getProductLock.lock();
        Product product = null;
        try {
            product = productMapper.get(productId);
            if (!Objects.isNull(product)) {
                redisUtil.set(productCacheKey, JSON.toJSONString(product),
                        generateProductCacheTimeOut(), TimeUnit.SECONDS);
                redisUtil.expire(productCacheKey, generateProductCacheTimeOut(), TimeUnit.SECONDS);
            } else {
                redisUtil.set(productCacheKey, EMPTY_CACHE, generateEmptyCacheTimeOut(), TimeUnit.SECONDS);
            }
        }finally {
            getProductLock.unlock();
        }
        return product;
    }
}
