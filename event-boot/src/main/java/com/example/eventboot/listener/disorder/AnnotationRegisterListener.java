package com.example.eventboot.listener.disorder;

import com.example.eventboot.eneity.UserBean;
import com.example.eventboot.event.UserRegisterEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

//@Component
public class AnnotationRegisterListener {

    /**
     * 注册监听实现方法
     * @param userRegisterEvent 用户注册事件
     */
    @EventListener
    public void register(UserRegisterEvent userRegisterEvent)
    {
        //获取注册用户对象
        UserBean user = userRegisterEvent.getUser();

        //../省略逻辑

        //输出注册用户信息
        System.out.println("注解 @EventListener 监听注册信息，用户名："+user.getName()+"，密码："+user.getPassword());
    }
}