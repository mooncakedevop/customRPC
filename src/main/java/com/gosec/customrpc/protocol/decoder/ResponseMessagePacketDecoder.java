package com.gosec.customrpc.protocol.decoder;

import com.google.common.collect.Maps;
import com.gosec.customrpc.protocol.message.MessageType;
import com.gosec.customrpc.protocol.message.ResponseMessagePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ResponseMessagePacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> list) throws Exception {
        ResponseMessagePacket packet = new ResponseMessagePacket();

        // 魔数
        packet.setMagicNumber(in.readInt());

        // 版本
        packet.setVersion(in.readInt());

        // 流水号
        int serialNumberLength = in.readInt();
        packet.setSerialNumber(in.readCharSequence(serialNumberLength, StandardCharsets.UTF_8).toString());

        // 消息类型
        packet.setMessageType(MessageType.fromValue(in.readByte()));

        // 附件
        int attachmentSize = in.readInt();
        Map<String, String> attachment = Maps.newHashMap();

        for (int i = 0; i < attachmentSize; i++) {
            int keyLength = in.readInt();
            String key = in.readCharSequence(keyLength, StandardCharsets.UTF_8).toString();

            int valueLength = in.readInt();
            String value = in.readCharSequence(valueLength, StandardCharsets.UTF_8).toString();
            attachment.put(key, value);
        }
        packet.setAttachments(attachment);

        // error code
        packet.setErrorCode(in.readLong());

        // message
        int messageLength = in.readInt();
        packet.setMessage(in.readCharSequence(messageLength, StandardCharsets.UTF_8).toString());

        // payload

        int payloadLength = in.readInt();
        packet.setPayload(in.readBytes(payloadLength));
        list.add(packet);

    }
}
