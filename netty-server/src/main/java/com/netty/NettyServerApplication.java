package com.netty;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import com.netty.disruptor.AbstractMessageConsumer;
import com.netty.disruptor.RingbufferWorkerPoolFactory;
import com.netty.server.NettyServer;
import com.netty.server.ServerMessageConsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NettyServerApplication.class, args);

		// 启动disruptor
		AbstractMessageConsumer[] consumers = new AbstractMessageConsumer[4];
		for (int i = 0; i < 4; i++) {
			AbstractMessageConsumer messageConsumer = new ServerMessageConsumer("code:sessionId:" + i);
			consumers[i] = messageConsumer;
		}

		RingbufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI, 1024 * 1024,
				new BlockingWaitStrategy(),
				// new YieldingWaitStrategy(),
				consumers);

		new NettyServer(8765);
	}

}
