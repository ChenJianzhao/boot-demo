package com.example.scheduleboot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PrintTask {

//    @Scheduled(cron = "0/3 12-13 15 * * *")
    public void  corn() {
        System.out.println(new Date(System.currentTimeMillis()));
    }

    // fixedRate 该属性的含义是上一个调用开始后再次调用的延时（不用等待上一次调用完成），如果上次调用的执行时间超过 rate 指定的时间，则上一次执行完成后立刻开始下一个执行
//    @Scheduled(fixedRate = 1000 )

    // fixedDelay 该属性会等到方法执行完成后延迟配置的时间再次执行该方法。
//    @Scheduled(fixedDelay = 1000)

    // initialDelay 该属性的作用是第一次执行延迟时间，只是做延迟的设定，并不会控制其他逻辑，所以要配合fixedDelay或者fixedRate来使用，
    @Scheduled(initialDelay = 5000, fixedDelay = 1000)
    public void fixedRate() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(new Date(System.currentTimeMillis()));
    }

}
