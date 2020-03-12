package com.netty.client;

import com.netty.disruptor.AbstractMessageConsumer;
import com.netty.model.TransformData;
import com.netty.model.TransformDataWrapper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ChenJunhong
 * @date 2018/12/16
 * Description:
 */
@Slf4j
public class ClientMessageConsumer extends AbstractMessageConsumer {

    public ClientMessageConsumer(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TransformDataWrapper event) throws Exception {
        TransformData rsp = event.getData();
        ChannelHandlerContext context = event.getContext();
        try{
            // 具体的业务处理逻辑
            log.info("Client: id = {},name={},messge={}",rsp.getId(),rsp.getName(),rsp.getMessage());

            // 模拟耗时的操作
            Thread.sleep(100);
        }finally {
            // 释放缓存
            ReferenceCountUtil.refCnt(rsp);
        }

    }
}
