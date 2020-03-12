package com.netty.model;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class TransformDataWrapper {

	private TransformData data;

	private ChannelHandlerContext context;
}
