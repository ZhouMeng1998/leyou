package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("brand")
public interface BrandApi {

    @GetMapping("page")
    public PageResult<Brand> getBrandsByPage(
            @RequestParam(required = false, value = "key")String key,
            @RequestParam(defaultValue = "1", value = "page")Integer page,
            @RequestParam(defaultValue = "5", value = "rows")Integer rows,
            @RequestParam(required = false, value = "sortBy")String sortBy,
            @RequestParam(required = false, value = "desc")boolean desc
    );

    @PostMapping
    public void saveBrand(Brand brand, @RequestParam("cids") List<Long> cids);

    @GetMapping("delete/{bid}")
    public void deleteBrand(@PathVariable("bid") Long bid);

    @PutMapping
    public void changeBrand(Brand newBrand, @RequestParam("cids") List<Long> cids);

    @GetMapping("cid/{cid}")
    public List<Brand> queryBrandsByCid(@PathVariable("cid")Long cid);

    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id")Long id);
}
