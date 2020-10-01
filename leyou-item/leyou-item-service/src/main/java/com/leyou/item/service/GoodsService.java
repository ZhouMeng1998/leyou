package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.GoodsMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Spu;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryService categoryService;

    public PageResult<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //先判断key
        if (StringUtils.isNotEmpty(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        //在判断是否有上/下架条件
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        PageHelper.startPage(page, rows);
        List<Spu> spus = goodsMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);
        //把spus变成spuBos
        List<SpuBo> spuBos = new ArrayList<>();
        spus.forEach(spu->{
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            List<String> cNameLists = categoryService.queryByIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            String cName = StringUtils.join(cNameLists, "-");
            spuBo.setCname(cName);
            spuBos.add(spuBo);
        });
        return new PageResult<SpuBo>(pageInfo.getTotal(), spuBos);
    }
}
