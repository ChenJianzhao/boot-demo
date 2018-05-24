package com.example.mybatisplusboot.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author chenjz
 * 2018/5/22
 */

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
//@TableName("learn_resoce")
public class LearnResource {

    private Long id;
    private String author;
    private String title;
    private String url;
    // SET和GET方法
}
