package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.item.service.ICategoryService;
import com.leyou.item.service.IGoodsService;

import com.sun.media.jfxmedia.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 商品相关的业务层实现类+
 *
 * @author MuYang
 */
@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private AmqpTemplate amqpTemplate;


    /**
     * 根据条件分页查询spu
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows) {

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        // 搜索条件
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        // 分页条件
        PageHelper.startPage(page, rows);

        // 执行查询
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        List<SpuBo> spuBos = new ArrayList<>();
        spus.forEach(spu->{
            SpuBo spuBo = new SpuBo();
            // copy共同属性的值到新的对象
            BeanUtils.copyProperties(spu, spuBo);
            // 查询分类名称
            List<String> names = this.categoryService.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names, "/"));

            // 查询品牌的名称
            spuBo.setBname(this.brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());

            spuBos.add(spuBo);
        });

        return new PageResult<>(pageInfo.getTotal(), spuBos);

    }

    /**
     * 新增商品
     *
     * @param spuBo
     */
    @Override
    //事务
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        Date now = new Date();
        //设置spu属性
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(now);
        spuBo.setLastUpdateTime(now);
        //执行插入spu数据
        this.spuMapper.insertSelective(spuBo);
        //设置spuDetail属性
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        //执行插入spuDetail
        this.spuDetailMapper.insertSelective(spuDetail);
        //遍历sku集合，设置sku属性
        saveSkuAndStock(spuBo, now);

        sengMsg("insert", spuBo.getId());
    }

    /**
     * 修改（更新）商品信息
     *
     * @param spuBo
     */
    @Override
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        //根据spuId查询要删除sku
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        List<Sku> skus = this.skuMapper.select(record);
        //遍历查询出来的sku集合,再根据skuId删除stock
        skus.forEach(sku -> {
            this.stockMapper.selectByPrimaryKey(sku.getId());
        });

        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());
        //删除sku
        this.skuMapper.delete(sku);
        //新增sku和新增stock
        this.saveSkuAndStock(spuBo, new Date());

        //设置spu属性
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        //执行修改spu
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        //执行修改spuDetail
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        sengMsg("update", spuBo.getId());
    }

    /**
     * 根据spuId查询sku集合
     *
     * @param spuId
     * @return
     */
    @Override
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(record);
        //遍历sku集合，获取每个skuId,并查询stock库存
        skus.forEach(sku -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skus;
    }

    @Override
    public Spu querySpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据spuId,查询spuDetail
     *
     * @param spuId
     * @return
     */
    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 消息生产者--保存或修改商品
     * @param type
     * @param id
     */
    private void sengMsg(String type, Long id) {
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历sku集合，设置sku属性
     *
     * @param spuBo spuBo数据
     * @param now   系统当前时间
     */
    private void saveSkuAndStock(SpuBo spuBo, Date now) {
        spuBo.getSkus().forEach(sku -> {
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(now);
            sku.setLastUpdateTime(now);
            //执行插入sku数据
            this.skuMapper.insertSelective(sku);
            //设置stock属性
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            //执行插入stock数据
            this.stockMapper.insertSelective(stock);
        });
    }


}