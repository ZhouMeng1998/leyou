package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据pid查找父节点
     * @param pid
     * @return
     */
    public List<Category> queryCategoriesByPid(Long pid) {
        Category record = new Category();
        record.setParentId(pid);
        List<Category> categories = this.categoryMapper.select(record);
        return categories;
    }

    public List<Category> queryCategoriesByBid(Long bid) {
        return categoryMapper.queryByBrandId(bid);
    }

    public List<String> queryByIdList(List<Long> asList) {
        List<Category> categories = categoryMapper.selectByIdList(asList);
        List<String> names = new ArrayList<>();
        for (Category category : categories) {
            names.add(category.getName());
        }
        return names;
    }

}
