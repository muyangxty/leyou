package com.leyou.cart.controller;


import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.ICartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ICartService cartService;

    /**
     * 添加购物车
     *
     * @param cart 购物车数据
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        this.cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询购物车
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCart() {
        List<Cart> carts = this.cartService.qureyCarts();
        if (CollectionUtils.isEmpty(carts)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 修改购物车商品数量
     *
     * @param cart
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart) {
        this.cartService.updateNum(cart);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除购物车
     *
     * @param skuId
     * @return
     */
    @PutMapping({"skuId"})
    public ResponseEntity<Void> deleteBySkuId(@PathVariable("skuId") String skuId) {
        this.cartService.deleteBySkuId(skuId);
        return ResponseEntity.ok().build();
    }


}
