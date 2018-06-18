package com.example.eventboot.listener.order;

import com.example.eventboot.eneity.UserBean;
import com.example.eventboot.event.UserRegisterEvent;
import com.example.eventboot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterSendMailListener implements SmartApplicationListener
{
    Logger logger = LoggerFactory.getLogger(UserRegisterSendMailListener.class);

    /**
     *  该方法返回true&supportsSourceType同样返回true时，才会调用该监听内的onApplicationEvent方法
     * @param aClass 接收到的监听事件类型
     * @return
     */
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        //只有UserRegisterEvent监听类型才会执行下面逻辑
        return aClass == UserRegisterEvent.class;
    }

    /**
     *  supportsEventType & supportsSourceType 两个方法返回true时调用该方法执行业务逻辑
     * @param applicationEvent 具体监听实例，这里是UserRegisterEvent
     */
    @Override
    @Async
    //只需要在异步方法上添加@Async注解就可以实现方法的异步调用
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //转换事件类型
        UserRegisterEvent userRegisterEvent = (UserRegisterEvent) applicationEvent;
        //获取注册用户对象信息
        UserBean user = userRegisterEvent.getUser();
        logger.info("【order: {}】 - 用户：{}，注册成功，发送邮件通知。", getOrder(), user.getName());
//        System.out.println("【order:】 "+ getOrder() +" - 用户："+user.getName()+"，注册成功，发送邮件通知。");
        // 模拟同步发送邮件延迟
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  该方法返回true&supportsEventType同样返回true时，才会调用该监听内的onApplicationEvent方法
     * @param aClass
     * @return
     */
    @Override
    public boolean supportsSourceType(Class<?> aClass) {
        //只有在UserService内发布的UserRegisterEvent事件时才会执行下面逻辑
        return aClass == UserService.class;
    }

    /**
     * 同步情况下监听执行的顺序
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }
}