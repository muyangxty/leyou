package com.leyou.item.service;


import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

/**
 * 品牌管理相关的业务层接口
 *
 * @author MuYang
 */
public interface IBrandService {

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
    PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    /**
     * 新增品牌
     *
     * @param brand 品牌
     * @param cids  cid
     */
    void saveBrand(Brand brand, List<Long> cids);

    /**
     * 根据分类id,查询品牌列表
     *
     * @param cid 分类id
     * @return
     */
    List<Brand> queryBrandByCid(Long cid);


    Brand queryBrandById(Long id);
}
