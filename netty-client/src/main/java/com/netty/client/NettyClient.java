package com.netty.client;

import com.netty.model.TransformData;
import com.netty.util.MarshallingCodeCFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ChenJunhong
 * @date 2018/12/16
 * Description:
 */
@Slf4j
public class NettyClient {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8765;

    /**
     * todo 使用concurrentHashMap<string,channel> 进行完善
     */
    private Channel channel;


    private EventLoopGroup workGroup = new NioEventLoopGroup();

    private ChannelFuture cf;

    public NettyClient() {
        this.connect(HOST,PORT);
    }

    private void connect(String host, int port) {

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.RCVBUF_ALLOCATOR,AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });
            cf = bootstrap.connect(host,port).sync();
            log.info("client connected ...");
            // 进行数据发送
            this.channel = cf.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendData(){
        for (int i = 0; i < 10; i++) {
            TransformData request = new TransformData();
            request.setId("ID:" + i);
            request.setName("Name:" + i);
            request.setMessage("Message：" + i);

//            log.info("create data id = {},name = {}, message = {}",request.getId(),request.getName(),request.getMessage());
            this.channel.writeAndFlush(request);
        }
    }

    public void close() throws InterruptedException {
        cf.channel().closeFuture().sync();
        workGroup.shutdownGracefully();
        log.warn("client shutdown!");
    }

}
