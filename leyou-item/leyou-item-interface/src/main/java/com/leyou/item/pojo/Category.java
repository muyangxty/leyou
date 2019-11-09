package com.leyou.item.pojo;


import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 分类管理-实体类
 * @author MuYang
 */
@Data
//表名
@Table(name="tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long parentId;
    // 注意isParent生成的getter和setter方法需要手动加上Is
    private Boolean isParent;
    private Integer sort;
    // getter和setter略
}
