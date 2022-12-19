package com.gosec.customrpc.server;

import com.gosec.customrpc.protocol.decoder.RequestMessagePacketDecoder;
import com.gosec.customrpc.protocol.encoder.ResponseMessagePacketEncoder;
import com.gosec.customrpc.protocol.serializer.FastJsonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(scanBasePackages = "com.gosec.customrpc")
@Slf4j
@ComponentScan(excludeFilters =
        {
                @ComponentScan.Filter(type = FilterType.REGEX,pattern = "com.gosec.customrpc.zookeeper.*")
        })
public class ServerApplication implements CommandLineRunner {
    @Value("${netty.port:9092}")
    private Integer nettyPort;

    @Autowired
    private ServerHandler serverHandler;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class,args);
    }
    @Override
    public void run(String... args) throws Exception {
        int port = nettyPort;
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,4,0,4));
                            ch.pipeline().addLast(new LengthFieldPrepender(4));
                            ch.pipeline().addLast(new RequestMessagePacketDecoder());
                            ch.pipeline().addLast(new ResponseMessagePacketEncoder(FastJsonSerializer.X));
                            ch.pipeline().addLast(serverHandler);

                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("启动nettyServer[{}]成功...", port);
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
