package com.gosec.customrpc.zookeeper;

import com.gosec.customrpc.protocol.decoder.RequestMessagePacketDecoder;
import com.gosec.customrpc.protocol.encoder.ResponseMessagePacketEncoder;
import com.gosec.customrpc.protocol.message.RequestMessagePacket;
import com.gosec.customrpc.protocol.message.ResponseMessagePacket;
import com.gosec.customrpc.protocol.serializer.FastJsonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient extends SimpleChannelInboundHandler<ResponseMessagePacket> {
    private ResponseMessagePacket packet;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessagePacket packet) throws Exception {
        this.packet = packet;
    }
    public ResponseMessagePacket client(RequestMessagePacket requestMessagePacket) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new RequestMessagePacketDecoder())
                                    .addLast(new ResponseMessagePacketEncoder(FastJsonSerializer.X))
                                    .addLast(NettyClient.this);
                        }
                    });
            String[] discover = ZookeeperOp.discover(requestMessagePacket.getInterfaceName()).split(":");
            ChannelFuture future = bootstrap.connect(discover[0], Integer.parseInt(discover[1])).sync();
            Channel channel = future.channel();
            channel.writeAndFlush(requestMessagePacket);
            channel.closeFuture().sync();
            return packet;


        } finally {
            group.shutdownGracefully();
        }
    }
}
