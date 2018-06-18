package com.example.starterboot.controller;

import com.example.starterboot.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController
{
    //注入自定义starter内逻辑
    @Autowired
    HelloService helloService;

    /**
     * 测试访问地址/hello
     * @return 格式化字符串
     */
    @RequestMapping(value = "/hello")
    public String sayHello()
    {
        return helloService.sayHello();
    }
}