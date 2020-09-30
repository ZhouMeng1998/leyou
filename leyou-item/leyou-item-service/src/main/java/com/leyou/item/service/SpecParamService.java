package com.leyou.item.service;

import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamService {
    @Autowired
    private SpecParamMapper paramMapper;

    public List<SpecParam> queryParamsByGid(Long gid) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        return paramMapper.select(param);
    }

    public void addParam(SpecParam param) {
        paramMapper.insertSelective(param);
    }

    public void editParam(SpecParam param) {
        this.deleteParam(param.getId());
        this.addParam(param);
    }

    public void deleteParam(Long id) {
        SpecParam param = new SpecParam();
        param.setId(id);
        paramMapper.delete(param);
    }

}
