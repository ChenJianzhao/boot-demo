package com.example.mybatisboot;

import com.example.mybatisboot.mapper.LearnMapper;
import com.github.pagehelper.PageHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BootDemoApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	LearnMapper mapper;

	@Test
	public void testMapper(){

		assertThat(mapper, notNullValue());

		// 开启分页
		PageHelper.startPage(0,10);
		System.out.println(mapper.queryLearnResouceById(999L));
	}

}
