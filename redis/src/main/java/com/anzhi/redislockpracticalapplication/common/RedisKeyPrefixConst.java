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

    /**
     * 双写不一致锁
     */
    String INCONSISTENT_DOUBLE_WRITE_DATA_CACHE = "product:inconsistent:double:write:data";
}
