package com.netty.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.netty.model.TransformData;
import com.netty.model.TransformDataWrapper;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * 生产者
 */
@Data
public class MessageProducer {

	private String producerId;

	private RingBuffer<TransformDataWrapper> ringBuffer;

	public MessageProducer(String producerId, RingBuffer<TransformDataWrapper> ringBuffer) {
		this.producerId = producerId;
		this.ringBuffer = ringBuffer;
	}

	public void onData(ChannelHandlerContext context, TransformData data) {
		long sequence = ringBuffer.next();
		TransformDataWrapper dataWrapper = ringBuffer.get(sequence);
		dataWrapper.setContext(context);
		dataWrapper.setData(data);
		ringBuffer.publish(sequence);
	}
}
