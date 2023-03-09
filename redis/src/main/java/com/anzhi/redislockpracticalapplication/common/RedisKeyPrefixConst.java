package com.anzhi.redislockpracticalapplication.common;

/**
 * redis 缓存前缀
 */
public interface RedisKeyPrefixConst {
    /**
     * 产品基础信息缓存前缀
     */
    String PRODUCT_CACHE = "product:cache:";

    /**
     * 热门商品前缀
     */
    String HOT_PRODUCT_LOCK_CACHE = "product:hot:lock:cache";
}
