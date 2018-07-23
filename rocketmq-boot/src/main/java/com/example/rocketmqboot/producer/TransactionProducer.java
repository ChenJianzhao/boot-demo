package com.example.rocketmqboot.producer;

/**
 * @author chenjz
 * 2018/7/23
 */
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Thread.*;

public class TransactionProducer {
    public static void main(String[] args) throws MQClientException, InterruptedException {


        TransactionMQProducer producer = new TransactionMQProducer("TransactionMQProducerGroup");
        producer.setNamesrvAddr("39.108.172.228:9876");

        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("client-transaction-msg-check-thread");
                        return thread;
                    }
        });

//        producer.setExecutorService(executorService);
//        producer.setTransactionListener(transactionListener);
        producer.start();

        TransactionCheckListener transactionListener = new TransactionListenerImpl();
        producer.setTransactionCheckListener(transactionListener);
        producer.setCallbackExecutor(executorService);

        String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            try {

                Message msg = new Message("TransactionTopic", tags[i % tags.length], "KEY" + i,
                                ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                msg.putUserProperty("order", String.valueOf(i));

                System.out.println("准备发送半消息...");

                SendResult sendResult = producer.sendMessageInTransaction(msg, new LocalTransactionExecuter() {
                    @Override
                    public LocalTransactionState executeLocalTransactionBranch(Message message, Object o) {

                        System.out.println("执行本地事务开始...");
                        try {
                            sleep(100); //模拟本地事务执行
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("执行本地事务完成...");

                        Integer order = Integer.valueOf(msg.getUserProperty("order"));
                        if(order % 2 == 0)
                            return LocalTransactionState.COMMIT_MESSAGE;
                        else
                            return LocalTransactionState.ROLLBACK_MESSAGE;
                    }
                },null);

                System.out.print("半消息发送结果...");
                System.out.printf("%s%n", sendResult);

                sleep(100);
            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // 保持生产者在线
        for (int i = 0; i < 100000; i++) {
            sleep(1000);
        }
        producer.shutdown();
    }
}
