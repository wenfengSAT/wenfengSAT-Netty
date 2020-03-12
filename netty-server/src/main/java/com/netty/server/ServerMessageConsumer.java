package com.netty.server;

import com.netty.disruptor.AbstractMessageConsumer;
import com.netty.model.TransformData;
import com.netty.model.TransformDataWrapper;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMessageConsumer extends AbstractMessageConsumer {

	public ServerMessageConsumer(String consumerId) {
		super(consumerId);
	}

	@Override
	public void onEvent(TransformDataWrapper event) throws Exception {
		TransformData request = event.getData();
		ChannelHandlerContext context = event.getContext();

		// 业务处理逻辑
		log.info("Server端：ID = {},name = {}, message = {}", request.getId(), request.getName(), request.getMessage());

		// 模拟耗时的操作
		Thread.sleep(100);

		// 回送响应信息
		TransformData response = new TransformData();
		response.setId("resp" + request.getId());
		response.setName("resp" + request.getName());
		response.setMessage("resp" + request.getMessage());

		context.writeAndFlush(response);
	}
}
