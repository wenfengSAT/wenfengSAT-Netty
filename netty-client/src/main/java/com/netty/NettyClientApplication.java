package com.netty;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.netty.client.ClientMessageConsumer;
import com.netty.client.NettyClient;
import com.netty.disruptor.AbstractMessageConsumer;
import com.netty.disruptor.RingbufferWorkerPoolFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);

        // 启动disruptor
        AbstractMessageConsumer[] consumers = new AbstractMessageConsumer[4];
        for (int i = 0; i < 4; i++) {
            AbstractMessageConsumer messageConsumer = new ClientMessageConsumer("code:sessionId:"+i);
            consumers[i] = messageConsumer;
        }

        RingbufferWorkerPoolFactory.getInstance().initAndStart(
                ProducerType.MULTI,
                1024*1024,
                new BlockingWaitStrategy(),
//                new YieldingWaitStrategy(),
                consumers
        );

        new NettyClient().sendData();
    }

}

