package com.example.rabbitmqproviderboot.common;

import lombok.Getter;

@Getter
public enum QueueEnum
{
	/**
	 * 用户注册枚举
	 */
	USER_REGISTER("user.register.queue","user.register")
	;
	/**
	 * 队列名称
	 */
	private String name;
	/**
	 * 队列路由键
	 */
	private String routingKey;

	QueueEnum(String name, String routingKey) {
		this.name = name;
		this.routingKey = routingKey;
	}
}
