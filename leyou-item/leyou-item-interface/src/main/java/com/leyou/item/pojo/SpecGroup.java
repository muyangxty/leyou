package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "tb_spec_group")
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //商品分类id
    private Long cid;
    //规格组名称
    private String name;

    @Transient
    private List<SpecParam> params;

}