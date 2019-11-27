package com.leyou.cart.service;


import com.leyou.cart.pojo.Cart;

import java.util.List;

/**
 * 购物车相关的业务层接口
 */
public interface ICartService {
    /**
     * 添加购物车
     *
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 查询购物车
     *
     * @return
     */
    List<Cart> qureyCarts();

    /**
     * 修改购物车商品数量
     *
     * @param cart
     */
    void updateNum(Cart cart);

    /**
     * 删除购物车
     *
     * @param skuId
     */
    void deleteBySkuId(String skuId);
}
