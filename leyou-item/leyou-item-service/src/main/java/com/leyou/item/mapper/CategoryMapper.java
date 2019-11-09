package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 分类管理相关的持久层接口
 *
 * @author MuYang
 */
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category, Long> {

    /**
     * 根据品牌id,查询商品分类
     * @param bid 品牌id
     * @return
     */
    @Select("SELECT * FROM tb_category WHERE id IN (SELECT category_id FROM tb_category_brand WHERE brand_id = #{bid})")
    List<Category> queryByBrandId(Long[] bid);
}
