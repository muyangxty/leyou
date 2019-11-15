package com.leyou.item.controller;


import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.ISpecificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 规格参数相关控制器层
 *
 * @author MuYang
 */
@Controller
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private ISpecificationService specificationService;

    /**
     * 根据分类id查询参数组
     *
     * @param cid 分类id
     * @return
     */
    @RequestMapping("gorups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid) {
        List<SpecGroup> groups = this.specificationService.queryGroupByCid(cid);
        if (CollectionUtils.isEmpty(groups)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
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
    @RequestMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "generic", required = false) Boolean generic,
            @RequestParam(value = "searching", required = false) Boolean searching) {
        List<SpecParam> params = this.specificationService.queryParams(gid, cid, generic, searching);
        if (CollectionUtils.isEmpty(params)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }

    /**
     * 新增
     *
     * @param specGroup
     * @return
     */
    @RequestMapping("save")
    public ResponseEntity<Void> save(@RequestBody SpecGroup specGroup) {
        this.specificationService.save(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除
     *
     * @param gid
     * @return
     */
    @RequestMapping("delete")
    public ResponseEntity<Void> deleteById(@RequestParam("gid") Long gid) {
        this.specificationService.deleteById(gid);
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改
     *
     * @param gid
     * @return
     */
    @RequestMapping("update")
    public ResponseEntity<Void> updateById(@RequestParam("gid") Long gid) {
        //TODO
        return null;
    }

    /**
     * 根据cid查询参数组
     * @param cid
     * @return
     */
    @GetMapping("group/param/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupWithParam(@PathVariable("cid")Long cid){
        List<SpecGroup> groups = this.specificationService.queryGroupWithParam(cid);
        if (CollectionUtils.isEmpty(groups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }

}
