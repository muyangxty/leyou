package com.leyou.service;


import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Map<String, Object> loadData(Long spuId){
        Map<String, Object> model = new HashMap<>();
        //根据spuId查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);

        //查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailById(spuId);
        //查询分类Map<String, Object>
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);
        //初始化一个分类的Map
        List<Map<String, Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++){
            Map<String, Object> map = new HashMap<>();
            map.put("id", cids.get(i));
            map.put("name", names.get(i));
            categories.add(map);
        }

        //查询brands
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        //查询skus
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);

        //查询规格参数组
        List<SpecGroup> specGroups = this.specificationClient.queryGroupWithParam(spu.getCid3());

        //查询特殊的规格参数
        List<SpecParam> Params = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        //初始化特殊规格参数的map
        Map<Long, String> paramMap = new HashMap<>();
        Params.forEach(param -> {
            paramMap.put(param.getId(), param.getName());
        });
        model.put("spu", spu);
        model.put("spuDetail", spuDetail);
        model.put("categories", categories);
        model.put("brands", brand);
        model.put("skus", skus);
        model.put("groups", specGroups);
        model.put("paramMap", paramMap);
        return model;
    }
}
