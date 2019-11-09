package com.leyou.item.controller;


import com.leyou.item.pojo.Category;
import com.leyou.item.service.ICategoryService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 分类管理相关的控制器层
 *
 * @Author MuYang
 */
@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 根据父节点id查询子节点
     *
     * @param pid 父id
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        //检查参数pid是否合法，为true则返回400:请求参数不合法
        if (pid == null || pid < 0) {
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.badRequest().build();
        }
        List<Category> categories = this.categoryService.queryCategoriesByPid(pid);
        //判断是否为空
        if (CollectionUtils.isEmpty(categories)) {
            //为空则返回404:资源服务器未找到
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categories);
    }

    @GetMapping
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids") List<Long> ids){
        List<String> names = this.categoryService.queryNameByIds(ids);
        //判断是否为空
        if (CollectionUtils.isEmpty(names)) {
            //为空则返回404:资源服务器未找到
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }

    /**
     * 根据品牌id,查询商品分类
     *
     * @param bid 品牌id
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long[] bid) {
        List<Category> categories = this.categoryService.queryByBrandId(bid);
        //判断是否为空, true则返回404错误:资源服务器未找到
        if (CollectionUtils.isEmpty(categories)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }

    /**
     * 根据品牌id，修改品牌信息
     *
     * @param category
     * @return
     */
    @RequestMapping("update")
    public ResponseEntity<Void> updateBrand(@RequestBody Category category) {
        //检查类目名称不能为空
        if (StringUtils.isNotBlank(category.getName())){
            //执行修改
            this.categoryService.updateBrand(category);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * 根据品牌id,删除品牌
     *
     * @param bid 品牌id
     * @return
     */
    @RequestMapping("delete")
    public ResponseEntity<Void> deleteByBrandId(@RequestParam("bid") Long[] bid) {

        //根据品牌id，查询商品
        List<Category> categories = this.categoryService.queryByBrandId(bid);
        //判断是否为空，true则抛出异常
        if (CollectionUtils.isEmpty(categories)) {
            //throw new ProductNotFoundException("删除失败！尝试访问的数据不存在");
            return ResponseEntity.notFound().build();
        }
        //执行删除
        this.categoryService.deleteByBrandId(bid);
        //返回成功
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
