package com.example.mybatisboot;

import com.example.mybatisboot.mapper.LearnMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

		System.out.println(mapper.queryLearnResouceById(999l));
	}

}
