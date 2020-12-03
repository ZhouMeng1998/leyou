package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import com.netflix.discovery.converters.Auto;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> getBrandsByPage(
            @RequestParam(required = false, value = "key")String key,
            @RequestParam(defaultValue = "1", value = "page")Integer page,
            @RequestParam(defaultValue = "5", value = "rows")Integer rows,
            @RequestParam(required = false, value = "sortBy")String sortBy,
            @RequestParam(required = false, value = "desc")boolean desc
            ) {
        PageResult<Brand> result = this.brandService.queryBrandsByPages(key, page, rows, sortBy, desc);
        if (CollectionUtils.isEmpty(result.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        this.brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("delete/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") Long bid){
        this.brandService.deleteBrand(bid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping
    public ResponseEntity<Void> changeBrand(Brand newBrand, @RequestParam("cids") List<Long> cids) {
        //先把老的brand删掉
        this.brandService.deleteBrand(newBrand.getId());
        //然后用saveBrand方法插入新的brand
        this.brandService.saveBrand(newBrand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid")Long cid){
        List<Brand> brands = this.brandService.queryBrandsByCid(cid);
        if (CollectionUtils.isEmpty(brands)) {
            return ResponseEntity.notFound().build();
        }
        else
            return ResponseEntity.ok(brands);
    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id")Long id) {
        Brand brand = this.brandService.queryBrandById(id);
        if (brand == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }
}
