package com.anzhi.redislockpracticalapplication.service;

import com.alibaba.fastjson.JSON;
import com.anzhi.redislockpracticalapplication.common.RedisKeyPrefixConst;
import com.anzhi.redislockpracticalapplication.common.RedisUtil;
import com.anzhi.redislockpracticalapplication.dao.ProductMapper;
import com.anzhi.redislockpracticalapplication.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class ProductService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private RedisUtil redisUtil;
    
    
    public Product createProduct(Product product){
        // 创建商品
        Product createResultProduct = productMapper.create(product);
        // 设置缓存
        redisUtil.set(RedisKeyPrefixConst.PRODUCT_CACHE + createResultProduct.getId(), JSON.toJSONString(createResultProduct));
        return createResultProduct;
    }
    
    public Product updateProduct(Product product){
        Product updateResultProduct = productMapper.update(product);
        redisUtil.set(RedisKeyPrefixConst.PRODUCT_CACHE + updateResultProduct.getId(), JSON.toJSONString(updateResultProduct));
        return updateResultProduct;
    }
    
    public Product getProduct(Long productId){
        Product product = null;
        String productJsonString = redisUtil.get(RedisKeyPrefixConst.PRODUCT_CACHE + productId);
        if(!StringUtils.isEmpty(productJsonString)){
            return JSON.parseObject(productJsonString, Product.class);
        }

        product = productMapper.get(productId);
        if(!Objects.isNull(product)){
            redisUtil.set(RedisKeyPrefixConst.PRODUCT_CACHE + productId, JSON.toJSONString(product));
        }
        return product;
        
    }
}
