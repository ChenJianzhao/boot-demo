package com.example.mybatisplusboot.service.impl;

import com.example.mybatisplusboot.entity.LearnResource;
import com.example.mybatisplusboot.mapper.LearnMapper;
import com.example.mybatisplusboot.service.LearnService;
//import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenjz
 * 2018/5/22
 */
@Service
public class LearnServiceImpl implements LearnService {

    @Autowired
    LearnMapper learnMapper;

    @Override
    public int add(LearnResource learnResource) {
        return this.learnMapper.add(learnResource);
    }

    @Override
    public int update(LearnResource learnResource) {
        return this.learnMapper.update(learnResource);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return this.learnMapper.deleteByIds(ids);
    }

    @Override
    public LearnResource queryLearnResouceById(Long id) {
        return this.learnMapper.queryLearnResouceById(id);
    }

    @Override
    public List<LearnResource> queryLearnResouceList(Map<String,Object> params) {
//        PageHelper.startPage(Integer.parseInt(params.get("page").toString()), Integer.parseInt(params.get("rows").toString()));
        return this.learnMapper.queryLearnResouceList(params);
    }
}