package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("specification")
public interface SpecificationApi {
    @GetMapping("groups/{cid}")
    public List<SpecGroup> queryGroupByCid(@PathVariable("cid") Long cid);

    @GetMapping("params")
    public List<SpecParam> querySpecParamByParams(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "generic", required = false) Boolean generic,
            @RequestParam(value = "specification", required = false) Boolean searching);

    @PostMapping("param")
    public void addParam(SpecParam param);

    @PutMapping("param")
    public void editParam(SpecParam param);

    @DeleteMapping("param/{id}")
    public void deleteParamById(@PathVariable("id") Long id);
}
