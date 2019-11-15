package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGorupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.ISpecificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 规格参数相关业务层实现类
 *
 * @author MuYang
 */
@Service
public class SpecificationServiceImpl implements ISpecificationService {

    @Autowired
    private SpecGorupMapper gorupMapper;

    @Autowired
    private SpecParamMapper paramMapper;

    /**
     * 根据分类id查询参数组
     *
     * @param cid 分类id
     * @return
     */
    @Override
    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup record = new SpecGroup();
        record.setCid(cid);
        return this.gorupMapper.select(record);
    }

    /**
     * 根据条件查询规格参数
     *
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     */
    @Override
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam record = new SpecParam();
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        return this.paramMapper.select(record);
    }

    @Override
    public void save(SpecGroup specGroup) {
        this.gorupMapper.insertSelective(specGroup);
    }

    /**
     * 删除
     *
     * @param gid
     */
    @Override
    public void deleteById(Long gid) {
        this.gorupMapper.deleteByExample(gid);
    }

    @Override
    public List<SpecGroup> queryGroupWithParam(Long cid) {
        List<SpecGroup> groups = this.queryGroupByCid(cid);
        groups.forEach(group ->{
            List<SpecParam> params = this.queryParams(group.getId(), null, null, null);
            group.setParams(params);
        });
        return groups;
    }

}
