package com.example.mybatisplusboot.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author chenjz
 * 2018/5/22
 */

@Data
@Component
//@TableName("learn_resoce")
public class LearnResource {

    private Long id;
    private String author;
    private String title;
    private String url;
    // SET和GET方法
}
