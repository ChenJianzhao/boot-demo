package com.example.mybatisplusboot.service;

import com.example.mybatisplusboot.entity.LearnResource;

import java.util.List;
import java.util.Map;

/**
 * @author chenjz
 * 2018/5/22
 */
public interface LearnService {

    int add(LearnResource learnResource);

    int update(LearnResource learnResource);

    int deleteByIds(String[] ids);

    LearnResource queryLearnResouceById(Long learnResouce);

    List<LearnResource> queryLearnResouceList(Map<String, Object> params);
}
