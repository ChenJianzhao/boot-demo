package com.example.druidboot;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.druidboot.entity.LearnResource;
import com.example.druidboot.mapper.LearnMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DruidBootApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    LearnMapper mapper;

    /**
     * 测试通用Mapper
     */
    @Test
    public void testMapperInsert(){

        System.out.println("select resource...");
        System.out.println(mapper.selectById(999L));

        System.out.println("insert resource...");
        mapper.insert(new LearnResource(1003L, "chenjz", "test insert", "com.example.mybatisplus"));

        System.out.println("select the new resource...");
        List<LearnResource> pageResource = mapper.selectPage(
                new Page<LearnResource>(0,10),
                new EntityWrapper<LearnResource>()
                        .eq("author", "chenjz")
                        .or("title={0}", "test"));

        pageResource.forEach(System.out::println);

        System.out.println("update the new resource...");
        LearnResource model = pageResource.isEmpty() ? null : pageResource.get(0);
        model.setTitle("test");
        model.setAuthor("updateAuthor");
        mapper.updateById(model);

    }

    @Test
    public void testMapperQuery() {

        System.out.println("select the updated resource...");
        List<LearnResource> pageResource = mapper.selectPage(
                new Page<LearnResource>(0,10),
                new EntityWrapper<LearnResource>()
                        .eq("author", "chenjz")
                        .or("title={0}", "test"));

        pageResource.forEach(System.out::println);
    }
}
