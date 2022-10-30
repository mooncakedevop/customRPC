package com.gosec.customrpc.protocol.decoder;

import com.google.common.collect.Maps;
import com.gosec.customrpc.protocol.message.MessageType;
import com.gosec.customrpc.protocol.message.RequestMessagePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class RequestMessagePacketDecoder extends ByteToMessageDecoder {

    public void decode(ChannelHandlerContext context, ByteBuf in, List<Object> list){
        RequestMessagePacket packet = new RequestMessagePacket();
        //魔数
        packet.setMagicNumber(in.readInt());

        //版本
        packet.setVersion(in.readInt());

        //流水号
        int serialNumberLength = in.readInt();
        packet.setSerialNumber(in.readCharSequence(serialNumberLength, StandardCharsets.UTF_8).toString());

        //消息类型
        byte messageType = in.readByte();
        packet.setMessageType(MessageType.fromValue(messageType));

        //附件
        Map<String, String> attachments = Maps.newHashMap();

        packet.setAttachments(attachments);

        int size = in.readInt();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                int keyLength = in.readInt();
                String key = in.readCharSequence(keyLength, StandardCharsets.UTF_8).toString();

                int valueLength = in.readInt();

                String value = in.readCharSequence(valueLength, StandardCharsets.UTF_8).toString();

                packet.getAttachments().put(key, value);
            }
        }

        int interfaceNameLength = in.readInt();
        packet.setInterfaceName(in.readCharSequence(interfaceNameLength, StandardCharsets.UTF_8).toString());
        int methodNameLength = in.readInt();
        packet.setMethodName(in.readCharSequence(methodNameLength,StandardCharsets.UTF_8).toString());
        int methodArgumentSignaturesLength = in.readInt();
        if (methodArgumentSignaturesLength > 0) {
            String[] methodArgumentSignatures = new String[methodArgumentSignaturesLength];
            for (int i = 0; i < methodArgumentSignaturesLength; i++) {
                int methodArgumentSignatureLength = in.readInt();
                methodArgumentSignatures[i] = in.readCharSequence(methodArgumentSignatureLength, StandardCharsets.UTF_8).toString();
            }
            packet.setMethodArgumentSignatures(methodArgumentSignatures);
        }

        int methodArgumentsLength = in.readInt();
        if (methodArgumentsLength > 0){
            Object[] methodArguments = new Object[methodArgumentsLength];
            for (int i = 0; i < methodArgumentsLength; i++) {
                int methodArgumentLength = in.readInt();
                methodArguments[i] = in.readBytes(methodArgumentLength);
            }
            packet.setMethodArguments(methodArguments);
        }

        list.add(packet);


    }
}
