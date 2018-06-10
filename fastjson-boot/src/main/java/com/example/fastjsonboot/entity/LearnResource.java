package com.example.fastjsonboot.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName("learn_resource")    // 注解表名
public class LearnResource {

    @TableId    // 注解 id 列
    private Long id;

    @TableField(value = "author") // 注解其他 column
    private String author;

    private String title;

    private String url;

    // 排除非表字段
//    @TableField(exist = false)
//    private String noColumn;

    // SET和GET方法
}
