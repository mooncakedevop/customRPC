package com.gosec.customrpc.client;

import com.alibaba.fastjson.JSON;
import com.gosec.customrpc.annotation.RPCService;
import com.gosec.customrpc.protocol.decoder.ResponseMessagePacketDecoder;
import com.gosec.customrpc.protocol.encoder.RequestMessagePacketEncoder;
import com.gosec.customrpc.protocol.message.ResponseMessagePacket;
import com.gosec.customrpc.protocol.serializer.FastJsonSerializer;
import com.gosec.customrpc.server.service.HelloService;
import com.gosec.customrpc.transform.HelloServiceImplTransform;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    @RPCService
    private HelloService helloService;

    public Client() {
        this.helloService = new HelloServiceImplTransform();
    }

    public static void main(String[] args) throws InterruptedException {

        int port = 9092;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
            bootstrap.option(ChannelOption.TCP_NODELAY, Boolean.TRUE);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new RequestMessagePacketEncoder(FastJsonSerializer.X));
                    ch.pipeline().addLast(new ResponseMessagePacketDecoder());
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<ResponseMessagePacket>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ResponseMessagePacket packet) throws Exception {
                            Object targetPayload = packet.getPayload();
                            if (targetPayload instanceof ByteBuf) {
                                ByteBuf byteBuf = (ByteBuf) targetPayload;
                                int readableByteLength = byteBuf.readableBytes();
                                byte[] bytes = new byte[readableByteLength];
                                byteBuf.readBytes(bytes);
                                targetPayload = FastJsonSerializer.X.decode(bytes, String.class);
                                byteBuf.release();
                            }
                            packet.setPayload(targetPayload);
                            log.info("接收到来自服务器的消息，消息内容：{}", JSON.toJSONString(packet));
                        }
                    });

                }
            });
            ChannelFuture future = bootstrap.connect("localhost", port).sync();
            log.info("启动nettyClient[{}]成功 ...", port);
            ClientChannelHolder.CHANNEL_ATOMIC_REFERENCE.set(future.channel());

//            HelloService helloService = ContractProxyFactory.ofProxy(HelloService.class);
            Client c = new Client();
            c.hello();

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void hello() {
        String result = this.helloService.sayHello("throwable");
        log.info(result);
    }
}
