package com.leyou.item.controller;


import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.IBrandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌管理相关控制器层
 *
 * @author MuYang
 */
@Controller
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    /**
     * 根据查询条件分页并排序查询品牌信息
     *
     * @param key    查询条件
     * @param page   当前页数
     * @param rows   行数
     * @param sortBy 排序字段
     * @param desc   排序标准（降序 or 升序）
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "desc", defaultValue = "asc") Boolean desc) {
        PageResult<Brand> result = this.brandService.queryBrandByPage(key, page, rows, sortBy, desc);
        //判断result是否为空
        if (CollectionUtils.isEmpty(result.getItems())) {
            //为空，返回404，资源未找到
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 新增品牌
     *
     * @param brand 品牌
     * @param cids  cid
     * @return
     */
    @RequestMapping()
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        this.brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据分类id,查询品牌列表
     *
     * @param cid 分类id
     * @return
     */
    @RequestMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid) {
        List<Brand> brands = this.brandService.queryBrandByCid(cid);
        if (CollectionUtils.isEmpty(brands)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        Brand brand = this.brandService.queryBrandById(id);
        if (brand == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }


}
