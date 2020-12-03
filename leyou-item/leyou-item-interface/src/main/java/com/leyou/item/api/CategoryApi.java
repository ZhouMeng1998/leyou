package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {
    @GetMapping("list")
    public List<Category> queryCategoriesByPid(@RequestParam(value = "pid", defaultValue = "0")Long pid);

    @GetMapping("bid/{bid}")
    public List<Category> queryCategoriesByBid(@PathVariable("bid")Long bid);

    @GetMapping("names")
    public List<String> queryNamesById(@RequestParam("ids")List<Long> ids);
}
