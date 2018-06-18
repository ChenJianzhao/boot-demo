package com.example.starterboot.service;

import org.springframework.stereotype.Service;

//@Service
// 不使用注解，使用 autoConfiguration 构造
public class HelloService
{
    //消息内容
    private String msg;
    //是否显示消息内容
    private boolean show = true;

    public String sayHello()
    {
        return show ? "Hello，" + msg : "Hidden";
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}