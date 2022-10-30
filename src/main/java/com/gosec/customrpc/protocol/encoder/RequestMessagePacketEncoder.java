package com.gosec.customrpc.protocol.encoder;

import com.google.common.base.Utf8;
import com.gosec.customrpc.protocol.message.RequestMessagePacket;
import com.gosec.customrpc.protocol.serializer.FastJsonSerializer;
import com.gosec.customrpc.protocol.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.ssl.ApplicationProtocolConfig;

import java.nio.charset.StandardCharsets;

public class RequestMessagePacketEncoder extends MessageToByteEncoder<RequestMessagePacket> {

    private final Serializer serializer;

    public RequestMessagePacketEncoder(Serializer x) {
        serializer = x;
    }

    @Override
    public void encode(ChannelHandlerContext context, RequestMessagePacket packet, ByteBuf out) throws Exception{

        //魔数
        out.writeInt(packet.getMagicNumber());

        //版本
        out.writeInt(packet.getVersion());

        //流水号
        out.writeInt(packet.getSerialNumber().length());
        out.writeCharSequence(packet.getSerialNumber(), StandardCharsets.UTF_8);

        //消息类型
        out.writeByte(packet.getMessageType().getType());

        //附件
        out.writeInt(packet.getAttachments().size());

        packet.getAttachments().forEach( (k, v) ->{
            out.writeInt(k.length());
            out.writeCharSequence(k, StandardCharsets.UTF_8);

            out.writeInt(v.length());
            out.writeCharSequence(v, StandardCharsets.UTF_8);
        });

        //接口全类名
        out.writeInt(packet.getInterfaceName().length());
        out.writeCharSequence(packet.getInterfaceName(), StandardCharsets.UTF_8);

        // 方法名
        out.writeInt(packet.getMethodName().length());
        out.writeCharSequence(packet.getMethodName(), StandardCharsets.UTF_8);

        // 方法参数签名
        if (packet.getMethodArgumentSignatures() != null) {

            int len = packet.getMethodArgumentSignatures().length;
            out.writeInt(len);

            for (int i = 0; i < len; i++) {
                String methodArgumentSignature = packet.getMethodArgumentSignatures()[i];
                out.writeInt(methodArgumentSignature.length());
                out.writeCharSequence(methodArgumentSignature, StandardCharsets.UTF_8);
            }
        } else {
            out.writeInt(0);
        }

        // 方法参数
        if (packet.getMethodArguments() != null) {
            int len = packet.getMethodArguments().length;
            out.writeInt(len);
            for (int i = 0; i < len; i++) {
                byte[] bytes = serializer.encode(packet.getMethodArguments()[i]);
                out.writeInt(bytes.length);
                out.writeBytes(bytes);
            }
        }

    }
}
