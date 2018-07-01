package com.example.rabbitmqconsumerboot;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "user.register.queue")
public class UserConsumer {

	@RabbitHandler
	public void execute(String message)
	{
		System.out.println("用户：" + message+"，完成了注册");

		//...//自行业务逻辑处理
	}
}