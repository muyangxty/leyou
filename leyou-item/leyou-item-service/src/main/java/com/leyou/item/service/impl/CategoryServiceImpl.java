package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.ICategoryService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类管理相关的业务层实现类
 *
 * @author MuYang
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据父id查询子节点
     *
     * @param pid 父id
     * @return 查询到的信息
     */
    @Override
    public List<Category> queryCategoriesByPid(Long pid) {
        Category record = new Category();
        record.setParentId(pid);
        return this.categoryMapper.select(record);
    }

    /**
     * 根据品牌id,查询商品分类
     *
     * @param ids 品牌id
     * @return
     */
    @Override
    public List<String> queryNameByIds(List<Long> ids) {
        List<Category> categories = this.categoryMapper.selectByIdList(ids);
        return categories.stream().map(category -> category.getName()).collect(Collectors.toList());
    }


    /**
     * 根据品牌id,查询商品分类
     *
     * @param bid 品牌id
     * @return
     */
    @Override
    public List<Category> queryByBrandId(Long[] bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }

    /**
     * 根据品牌id,删除
     *
     * @param bid 品牌id
     */
    @Override
    public void deleteByBrandId(Long[] bid) {
        this.categoryMapper.deleteByPrimaryKey(bid);
    }

    @Override
    public void updateBrand(Category category) {
        this.categoryMapper.updateByPrimaryKeySelective(category);
    }

}
