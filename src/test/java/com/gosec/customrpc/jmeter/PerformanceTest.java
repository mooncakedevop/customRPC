package com.gosec.customrpc.jmeter;

import com.alibaba.fastjson.JSON;
import com.gosec.customrpc.client.Client;
import com.gosec.customrpc.client.ClientChannelHolder;
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
import org.apache.commons.io.FileUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class PerformanceTest {

    static volatile long max = 0;
    static volatile long total = 0;

    public void init(){
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
//                            log.info("接收到来自服务器的消息，消息内容：{}", JSON.toJSONString(packet));
//                            log.info("接收到来自服务器的消息");

                        }
                    });

                }
            });
            ChannelFuture future = bootstrap.connect("localhost", port).sync();
            log.info("启动nettyClient[{}]成功 ...", port);
            ClientChannelHolder.CHANNEL_ATOMIC_REFERENCE.set(future.channel());

            Client client = new Client();
            long start = System.currentTimeMillis();
            client.hello("performance");
            long end = System.currentTimeMillis();
            long invokeTime = end - start;
            max = max > invokeTime ? max : invokeTime;
            total += invokeTime;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void singleClient(Client client) throws InterruptedException, IOException {
        int threadNum = 1000000;
        CountDownLatch count = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    client.hello("performance");
                    long end = System.currentTimeMillis();
                    long invokeTime = end - start;
                    max = max > invokeTime ? max : invokeTime;
                    total += invokeTime;
                    count.countDown();
                }
            }).run();
        }
        count.await();
        File file = new File("/Users/mooncake/IdeaProjects/customRPC/src/test/java/com/gosec/customrpc/jmeter/result.txt");

        FileUtils.write(file,"thread: " + threadNum + "\n", StandardCharsets.UTF_8,true);

        FileUtils.write(file,"max: " + max + "  ms\n", StandardCharsets.UTF_8,true);
        FileUtils.write(file,"average: " + total + "ms\n", StandardCharsets.UTF_8,true);



    }
    public void singleConnect() throws InterruptedException, IOException {
        int threadNum = 2500;
        CountDownLatch count = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PerformanceTest performanceTest = new PerformanceTest();
                    performanceTest.init();
                    count.countDown();
                }
            }).run();
        }
        count.await();
        File file = new File("/Users/mooncake/IdeaProjects/customRPC/src/test/java/com/gosec/customrpc/jmeter/result.txt");

        FileUtils.write(file,"client: " + threadNum + "\n", StandardCharsets.UTF_8,true);

        FileUtils.write(file,"max: " + max + "  ms\n", StandardCharsets.UTF_8,true);
        FileUtils.write(file,"average: " + total + "ms\n", StandardCharsets.UTF_8,true);


    }


    public static void main(String[] args) throws InterruptedException, IOException {
       PerformanceTest test = new PerformanceTest();
       test.init();
       test.singleClient(new Client());
    }

}

