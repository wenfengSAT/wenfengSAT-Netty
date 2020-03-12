package com.netty.client;

import com.netty.disruptor.MessageProducer;
import com.netty.disruptor.RingbufferWorkerPoolFactory;
import com.netty.model.TransformData;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ChenJunhong
 * @date 2018/12/16
 * Description:
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*
        try{
            TransformData rsp = (TransformData) msg;
            log.info("Client: id = {},name={},messge={}",rsp.getId(),rsp.getName(),rsp.getMessage());
        }finally {
            // 释放缓存
            ReferenceCountUtil.refCnt(msg);
        }*/
        TransformData response = (TransformData) msg;
        String producerId = "code:sessionId:002";
        MessageProducer messageProducer = RingbufferWorkerPoolFactory.getInstance().getProducer(producerId);
        messageProducer.onData(ctx,response);
    }
}
