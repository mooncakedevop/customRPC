package com.gosec.customrpc.zookeeper;

import com.alibaba.fastjson.JSON;
import com.gosec.customrpc.protocol.decoder.RequestMessagePacketDecoder;
import com.gosec.customrpc.protocol.encoder.ResponseMessagePacketEncoder;
import com.gosec.customrpc.protocol.message.MessageType;
import com.gosec.customrpc.protocol.message.RequestMessagePacket;
import com.gosec.customrpc.protocol.message.ResponseMessagePacket;
import com.gosec.customrpc.protocol.serializer.FastJsonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.logging.Logger;
@Slf4j
public class ZkServer {

    public static void start(String ip, int port, Map<String, Object> params) throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class).
                    childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,4,0,4));
                            ch.pipeline().addLast(new LengthFieldPrepender(4));
                            ch.pipeline().addLast(new RequestMessagePacketDecoder());
                            ch.pipeline().addLast(new ResponseMessagePacketEncoder(FastJsonSerializer.X));
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<RequestMessagePacket>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, RequestMessagePacket packet) throws Exception {
                                    log.info("接收到来自客户端的消息： 消息内容[{}]", JSON.toJSONString(packet));
                                    ResponseMessagePacket response = new ResponseMessagePacket();
                                    response.setMagicNumber(packet.getMagicNumber());
                                    response.setVersion(packet.getVersion());
                                    response.setSerialNumber(packet.getSerialNumber());
                                    response.setMessageType(MessageType.RESPONSE);
                                    response.setAttachments(packet.getAttachments());
                                    response.setErrorCode(200L);
                                    ZkHelloServiceImpl zk = new ZkHelloServiceImpl();
                                    //查找实现类、匹配方法、创建对象、调用方法
                                    response.setMessage(zk.sayHello(""));
                                    ctx.writeAndFlush(response);
                                }
                            });
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("启动 netty Server[{}] 成功...", port);
            if (future.isSuccess()) {
                for (String key : params.keySet()) {
                    ZookeeperOp.register(key, ip + ":"+ port);
                }
            }
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
