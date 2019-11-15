package com.leyou.search.controller;


import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.service.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索服务控制器层
 *
 * @author MuYang
 * @date 2019-11-09
 */
@RestController
@RequestMapping
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 搜索商品
     *
     * @param request
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest request) {
        SearchResult result = this.searchService.search(request);
        if (request == null || CollectionUtils.isEmpty(result.getItems())) {
            return ResponseEntity.notFound().build();
        }
        //返回200
        return ResponseEntity.ok(result);
    }
}
