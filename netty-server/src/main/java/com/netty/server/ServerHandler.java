package com.netty.server;

import com.netty.disruptor.MessageProducer;
import com.netty.disruptor.RingbufferWorkerPoolFactory;
import com.netty.model.TransformData;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	/*
        TransformData request = (TransformData) msg;
        log.info("Server端：ID = {},name = {}, message = {}",request.getId(),request.getName(),request.getMessage());

        TransformData response = new TransformData();
        response.setId("resp" + request.getId());
        response.setName("resp" + request.getName());
        response.setMessage("resp" + request.getMessage());

        ctx.writeAndFlush(response);*/

        TransformData request = (TransformData) msg;
        // producerID 需要有自己的生成的规则，这里的code是机器码的意思
        String producerId = "code:sessionId:001";
        MessageProducer messageProducer = RingbufferWorkerPoolFactory.getInstance().getProducer(producerId);
        messageProducer.onData(ctx,request);
    }
}
