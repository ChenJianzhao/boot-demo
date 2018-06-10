package com.example.mybatisboot.entity;

import lombok.Data;

/**
 * @author chenjz
 * 2018/5/22
 */

@Data
public class LearnResource {
    private Long id;
    private String author;
    private String title;
    private String url;
    // SET和GET方法
}
