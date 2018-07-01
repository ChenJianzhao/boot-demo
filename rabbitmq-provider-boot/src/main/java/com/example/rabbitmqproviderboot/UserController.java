package com.example.rabbitmqproviderboot;

import com.example.rabbitmqproviderboot.common.ExchangeEnum;
import com.example.rabbitmqproviderboot.common.QueueEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController
{
//	/**
//	 * 用户业务逻辑
//	 */
//	@Autowired
//	private UserService userService;
//
//	/**
//	 * 保存用户基本信息
//	 * @param userEntity
//	 * @return
//	 */
//	@RequestMapping(value = "/save")
//	public UserEntity save(UserEntity userEntity) throws Exception
//	{
//		userService.save(userEntity);
//		return userEntity;
//	}

	@Autowired
	QueueMessageService queueMessageService;

	@RequestMapping(value = "/send/{message}")
	public void send(@PathVariable("message") String message) throws Exception
	{
		/**
		 * 将消息写入消息队列
		 */
		queueMessageService.send(message, ExchangeEnum.USER_REGISTER, QueueEnum.USER_REGISTER);

	}
}
