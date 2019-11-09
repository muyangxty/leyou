package com.leyou.item.api;


import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 规格参数相关控制器层
 *
 * @author MuYang
 */
@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 根据条件查询规格参数
     *
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     */
    @RequestMapping("params")
    List<SpecParam> queryParams(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "generic", required = false) Boolean generic,
            @RequestParam(value = "searching", required = false) Boolean searching);


}
