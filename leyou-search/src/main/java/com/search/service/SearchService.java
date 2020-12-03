package com.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.*;
import com.search.client.BrandClient;
import com.search.client.CategoryClient;
import com.search.client.GoodsClient;
import com.search.client.SpecificationClient;
import com.search.pojo.Goods;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SearchService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Goods BuildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //查询Category名称
        List<String> names = categoryClient.queryNamesById(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询Spu下的sku(用goodsClient)
        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());
        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuMapList = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("title" ,sku.getTitle());
            skuMap.put("id" ,sku.getId());
            skuMap.put("price" ,sku.getPrice());
            skuMap.put("images", StringUtils.isNotEmpty(sku.getImages()) ?
                    StringUtils.split(sku.getImages(), ",")[0] : "");
            skuMapList.add(skuMap);
        });
        List<SpecParam> params = specificationClient.querySpecParamByParams(null, spu.getCid3(), null, true);
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());
        Map<Long, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>(){});
        Map<Long, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<Object>>>(){});
        Map<String, Object> paramMap = new HashMap<>();
        params.forEach(param -> {
            if (param.getGeneric()) {
                String value = genericSpecMap.get(param.getId()).toString();
                if (param.getNumeric()) {
                    value = chooseSegment(value, param);
                }
                paramMap.put(param.getName(), value);
            }else {
                paramMap.put(param.getName(), specialSpecMap.get(param.getId()));
            }
        });
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setAll(spu.getTitle() + brand.getName() + StringUtils.join(names, " "));
        goods.setPrice(prices);
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        goods.setSpecs(paramMap);
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其他";
        String[] segments = p.getSegments().split(",");
        for (String segment : segments) {
            String[] segs = segment.split("-");
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            if (begin <= val && val <= end) {
                if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                }else if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                }else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
