package com.leyou.cart.service.Impl;

import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.ICartService;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 购物车相关的业务层实现类
 */
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFIX = "user:cart:";

    /**
     * 添加购物车
     *
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //查询购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        String key = cart.getSkuId().toString();
        Integer num = cart.getNum();
        //判断当前的商品是否在购物车中
        if (hashOperations.hasKey(cart.getSkuId().toString())) {
            //若有，更新数量
            String cartJson = hashOperations.get(key).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum() + num);
            hashOperations.put(key, JsonUtils.serialize(cart));
        } else {
            //没有，新增购物车
            Sku sku = this.goodsClient.querySkuBySkuId(cart.getSkuId());
            cart.setUserId(userInfo.getId());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
        }
        hashOperations.put(key, cart);
    }

    /**
     * 查询购物车
     *
     * @return
     */
    @Override
    public List<Cart> qureyCarts() {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //判断用户是否有购物车记录
        if (!this.redisTemplate.hasKey(KEY_PREFIX + userInfo.getId())) {
            return null;
        }
        //获取用户的购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        //获取购物车Map中所有dCart值集合
        List<Object> cartsJson = hashOperations.values();
        //如果购物车集合位空，直接返回null
        if (CollectionUtils.isEmpty(cartsJson)) {
            return null;
        }
        //把List<Object>集合转化为list<Cart>集合
        return cartsJson.stream().map(cartJson -> JsonUtils.parse(cartsJson.toString(), Cart.class)).collect(Collectors.toList());

    }

    /**
     * 修改购物车商品数量
     *
     * @param cart
     */
    @Override
    public void updateNum(Cart cart) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //判断用户是否有购物车记录
        if (!this.redisTemplate.hasKey(KEY_PREFIX + userInfo.getId())) {
            return;
        }

        Integer num = cart.getNum();
        //获取用户的购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();
        cart = JsonUtils.parse(cartJson, Cart.class);
        cart.setNum(num);
        hashOperations.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    /**
     * 删除购物车
     *
     * @param skuId
     */
    @Override
    public void deleteBySkuId(String skuId) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //判断用户是否有购物车记录
        if (!this.redisTemplate.hasKey(KEY_PREFIX + userInfo.getId())) {
            return;
        }
        //获取用户的购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        hashOperations.delete(skuId);
    }


}
