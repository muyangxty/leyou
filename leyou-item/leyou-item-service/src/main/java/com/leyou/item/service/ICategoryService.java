package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

/**
 * 分类管理业务层接口
 *
 * @author MuYang
 */
public interface ICategoryService {

    /**
     * 根据父id查询子节点
     *
     * @param pid 父id
     * @return
     */
    List<Category> queryCategoriesByPid(Long pid);

    /**
     * @param ids
     * @return
     */
    List<String> queryNameByIds(List<Long> ids);

    /**
     * 根据品牌id,查询商品分类
     *
     * @param bid 品牌id
     * @return
     */
    List<Category> queryByBrandId(Long[] bid);

    /**
     * 根据品牌id,删除
     *
     * @param bid 品牌id
     */
    void deleteByBrandId(Long[] bid);

    /**
     * 修改
     *
     * @param category
     */
    void updateBrand(Category category);
}
