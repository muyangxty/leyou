package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.IBrandService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 品牌管理相关业务层实现类
 *
 * @author MuYang
 */
@Service
public class BrandServiceImpl implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 分页查询品牌信息
     *
     * @param key    查询条件
     * @param page   当前页数
     * @param rows   每页显示记录数
     * @param sortBy 排序字段
     * @param desc   排序标准（降序 or 升序）
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //初始化example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        //判断参数key是否为空，不为空---根据name模糊查询，或者根据首字母
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }
        //添加分页条件
        PageHelper.startPage(page, rows);
        //判断参数sortBy是否为空，不为空---添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        List<Brand> brands = this.brandMapper.selectByExample(example);
        //包装成pageInfo
        PageInfo<Brand> PageInfo = new PageInfo<>(brands);
        //包装成分页结果集返回
        return new PageResult<>(PageInfo.getTotal(), PageInfo.getList());
    }

    /**
     * 新增品牌
     *
     * @param brand 品牌
     * @param cids  cid
     */
    @Override
    //事务
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        this.brandMapper.insertSelective(brand);
        //再新增中间表
        cids.forEach(cid -> {
            this.brandMapper.insertCategoryAndBrand(cid, brand.getId());
        });


    }

    /**
     * 根据分类id,查询品牌列表
     *
     * @param cid 分类id
     * @return
     */
    @Override
    public List<Brand> queryBrandByCid(Long cid) {
        return this.brandMapper.selectBrandsByCid(cid);
    }

    @Override
    public Brand queryBrandById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }

}
