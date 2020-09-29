package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {
    @Insert("INSERT INTO tb_category_brand(category_id, brand_id) VALUES (#{cid},#{bid})")
    int saveBrandAndCategory(@Param("bid")Long bid, @Param("cid")Long cid);

    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    void deleteMidTable_tb_category_brand(Long bid);
}
