package com.leyou.item.api;


import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 分类管理相关的控制器层
 *
 * @Author MuYang
 */
@RequestMapping("category")
public interface CategoryApi {

    @GetMapping
    List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);

}
