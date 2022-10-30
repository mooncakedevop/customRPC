package com.gosec.customrpc.contract;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gosec.customrpc.client.ClientChannelHolder;
import com.gosec.customrpc.client.extractor.DefaultRequestArgumentExtractor;
import com.gosec.customrpc.client.extractor.RequestArgumentExtractInput;
import com.gosec.customrpc.client.extractor.RequestArgumentExtractOutput;
import com.gosec.customrpc.client.extractor.RequestArgumentExtractor;
import com.gosec.customrpc.protocol.Constant;
import com.gosec.customrpc.protocol.message.MessageType;
import com.gosec.customrpc.protocol.message.RequestMessagePacket;
import io.netty.channel.Channel;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentMap;

public class ContractProxyFactory {
    private static final RequestArgumentExtractor EXTRACTOR = new DefaultRequestArgumentExtractor();
    private static final ConcurrentMap<Class<?>, Object> CACHE = Maps.newConcurrentMap();

    @SuppressWarnings("unchecked")
    public static <T> T ofProxy(Class<T> interfaceKlass) {
        return (T) CACHE.computeIfAbsent(interfaceKlass, x ->
            Proxy.newProxyInstance(interfaceKlass.getClassLoader(), new Class[]{interfaceKlass}, (target, method, args) -> {
                RequestArgumentExtractInput input = new RequestArgumentExtractInput();
                input.setInterfaceKlass(interfaceKlass);
                input.setMethod(method);
                RequestArgumentExtractOutput output = EXTRACTOR.extract(input);

                //封装请求参数
                RequestMessagePacket packet = new RequestMessagePacket();
                packet.setMagicNumber(Constant.magicNumber);
                packet.setVersion(Constant.version);
                packet.setSerialNumber("1");
                packet.setMessageType(MessageType.REQUEST);
                packet.setInterfaceName(output.getInterfaceName());
                packet.setMethodName(output.getMethodName());
                packet.setMethodArgumentSignatures(output.getMethodArgumentSignatures().toArray(new String[0]));
                packet.setMethodArguments(args);
                Channel channel = ClientChannelHolder.CHANNEL_ATOMIC_REFERENCE.get();
                channel.writeAndFlush(packet);
                return String.format("[%s#%s]调用成功， 发送了[%s]到Netty Server[%s]", output.getInterfaceName(), output.getMethodName(), JSON.toJSONString(packet), channel.remoteAddress());
            }));
    }
}
