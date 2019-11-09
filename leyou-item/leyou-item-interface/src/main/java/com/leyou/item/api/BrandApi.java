package com.leyou.item.api;


import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 品牌管理相关控制器层
 *
 * @author MuYang
 */
@RequestMapping("brand")
public interface BrandApi {

    @GetMapping("{id}")
    Brand queryBrandById(@PathVariable("id") Long id);
}
