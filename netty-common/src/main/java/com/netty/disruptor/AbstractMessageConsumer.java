package com.netty.disruptor;

import com.lmax.disruptor.WorkHandler;
import com.netty.model.TransformDataWrapper;

import lombok.Data;

@Data
public abstract class AbstractMessageConsumer implements WorkHandler<TransformDataWrapper> {

	protected String consumerId;

	public AbstractMessageConsumer(String consumerId) {
		this.consumerId = consumerId;
	}
}
