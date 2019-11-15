package com.leyou.search.pojo;


import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
public class SearchResult extends PageResult<Goods> {

    private List<Map<String, Object>> categories;

    private List<Brand> brands;

    private List<Map<String, Object>> specs;

    public SearchResult(List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(List<Map<String, Object>> categories) {
        this.categories = categories;
    }

}
