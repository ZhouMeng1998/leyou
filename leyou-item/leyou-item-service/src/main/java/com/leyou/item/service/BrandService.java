package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper mapper;

    public PageResult<Brand> queryBrandsByPages(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        // 初始化example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        // 根据name模糊查询，或者根据首字母查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }


        // 添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        // 添加分页条件
        PageHelper.startPage(page, rows);
        List<Brand> brands = this.mapper.selectByExample(example);

        // 包装成pageInfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        // 包装成分页结果集返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }



    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        this.mapper.insertSelective(brand);
        cids.forEach(cid->{
            this.mapper.saveBrandAndCategory(brand.getId(), cid);
        });
    }

    public Brand selectBrandById(Long id) {
        Brand brand = new Brand();
        brand.setId(id);
        List<Brand> brands = mapper.selectByExample(brand);
        return brands.get(0);
    }

    public void deleteBrand(Long bid) {
        Brand brand = new Brand();
        brand.setId(bid);
        mapper.delete(brand);
        mapper.deleteMidTable_tb_category_brand(bid);
    }

    public List<Brand> queryBrandsByCid(Long cid) {
        return this.mapper.selectBrandsByCid(cid);
    }
}


