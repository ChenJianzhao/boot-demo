package com.example.rocketmqboot.producer;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenjz
 * 2018/7/23
 */
public class TransactionListenerImpl implements TransactionCheckListener {
    private AtomicInteger transactionIndex = new AtomicInteger(0);

    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

//    @Override
//    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//        int value = transactionIndex.getAndIncrement();
//        int status = value % 3;
//        localTrans.put(msg.getTransactionId(), status);
//        return LocalTransactionState.UNKNOW;
//    }


    @Override
    public LocalTransactionState checkLocalTransactionState(MessageExt messageExt) {
//        Integer status = localTrans.get(msg.getTransactionId());
//        if (null != status) {
//            switch (status) {
//                case 0:
        System.out.println("回到检查接口");
//                    return LocalTransactionState.UNKNOW;
//                case 1:
//                    return LocalTransactionState.COMMIT_MESSAGE;
//                case 2:
//                    return LocalTransactionState.ROLLBACK_MESSAGE;
//            }
//        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}