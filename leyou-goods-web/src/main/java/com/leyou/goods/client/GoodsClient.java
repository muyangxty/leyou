package com.leyou.goods.client;


import com.leyou.item.api.GoodsApi;
import com.leyou.item.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {

    Spu querySpuById(Long id);
}
