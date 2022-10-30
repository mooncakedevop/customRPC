package com.gosec.customrpc.protocol.encoder;

import com.gosec.customrpc.protocol.message.ResponseMessagePacket;
import com.gosec.customrpc.protocol.serializer.FastJsonSerializer;
import com.gosec.customrpc.protocol.serializer.Serializer;
import com.gosec.customrpc.util.ByteBufferUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class ResponseMessagePacketEncoder extends MessageToByteEncoder<ResponseMessagePacket> {
    
    private final Serializer serializer;

    public ResponseMessagePacketEncoder(Serializer x) {
        serializer = x;
    }

    @Override
    protected void encode(ChannelHandlerContext context, ResponseMessagePacket packet, ByteBuf out) throws Exception {
        // 魔数
        out.writeInt(packet.getMagicNumber());

        // 版本
        out.writeInt(packet.getVersion());

        // 流水号
        out.writeInt(packet.getSerialNumber().length());
        out.writeCharSequence(packet.getSerialNumber(), StandardCharsets.UTF_8);

        // 消息类型
        out.writeByte(packet.getMessageType().getType());

        // 附件
        int size = packet.getAttachments().size();
        out.writeInt(size);

        packet.getAttachments().forEach((k, v) -> {
            int keyLength = k.length();
            out.writeInt(keyLength);
            out.writeCharSequence(k, StandardCharsets.UTF_8);

            int valueLength = v.length();
            out.writeInt(valueLength);
            out.writeCharSequence(v, StandardCharsets.UTF_8);
        });

        // error code
        out.writeLong(packet.getErrorCode());

        // message
        String message = packet.getMessage();
        ByteBufferUtils.X.encodeUtf8CharSequence(out, message);

        // payload
        byte[] bytes = serializer.encode(packet.getPayload());
        out.writeInt(bytes.length);
        out.writeBytes(bytes);

    }


}
