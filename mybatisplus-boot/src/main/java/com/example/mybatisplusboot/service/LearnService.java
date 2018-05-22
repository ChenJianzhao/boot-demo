package com.example.mybatisplusboot.service;

import com.example.mybatisplusboot.entity.LearnResouce;

import java.util.List;
import java.util.Map;

/**
 * @author chenjz
 * 2018/5/22
 */
public interface LearnService {

    int add(LearnResouce learnResouce);

    int update(LearnResouce learnResouce);

    int deleteByIds(String[] ids);

    LearnResouce queryLearnResouceById(Long learnResouce);

    List<LearnResouce> queryLearnResouceList(Map<String, Object> params);
}
