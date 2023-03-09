package com.anzhi.redislockpracticalapplication.dao;

import com.anzhi.redislockpracticalapplication.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public class ProductMapper {
    /**
     * 创建商品
     */
    public Product create(Product product) {
        System.out.println("创建商品成功");
        return product;
    }

    /**
     * 更新商品数据
     * @param product
     * @return
     */
    public Product update(Product product) {
        System.out.println("修改商品成功");
        return product;
    }

    /**
     * 获取商品信息
     * @param productId
     * @return
     */
    public Product get(Long productId) {
        System.out.println("查询商品成功");
        Product product = new Product();
        product.setId(productId);
        product.setName("test");
        return product;
    }
}
