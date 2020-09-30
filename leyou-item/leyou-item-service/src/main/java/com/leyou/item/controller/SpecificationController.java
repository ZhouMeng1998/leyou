package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecGroupService;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecGroupService groupService;
    @Autowired
    private SpecParamService paramService;

    /**
     * 根据cid查询对应的规格组Group
     *
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid) {
        List<SpecGroup> groups = groupService.queryGroupByCid(cid);
        if (groups == null || groups.size() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(groups);
    }

    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamsByGid(@RequestParam("gid") Long gid) {
        List<SpecParam> params = paramService.queryParamsByGid(gid);
        if (params == null || params.size() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(params);
    }

    @PostMapping("param")
    private ResponseEntity<Void> addParam(SpecParam param) {
        paramService.addParam(param);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("param")
    private ResponseEntity<Void> editParam(SpecParam param) {
        try {
            paramService.editParam(param);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("param/{id}")
    private ResponseEntity<Void> deleteParamById(@PathVariable("id") Long id) {
        paramService.deleteParam(id);
        return ResponseEntity.ok().build();
    }
}