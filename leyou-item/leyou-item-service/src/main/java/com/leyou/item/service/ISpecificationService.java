package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;


/**
 * 规格参数相关业务层接口
 *
 * @author MuYang
 */
public interface ISpecificationService {

    /**
     * 根据分类id查询参数组
     *
     * @param cid 分类id
     */
    List<SpecGroup> queryGroupByCid(Long cid);

    /**
     * 根据条件查询规格参数
     *
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     */
    List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching);

    /**
     * 新增
     *
     * @param specGroup
     */
    void save(SpecGroup specGroup);

    /**
     * 删除
     *
     * @param gid
     */
    void deleteById(Long gid);

    List<SpecGroup> queryGroupWithParam(Long cid);
}
