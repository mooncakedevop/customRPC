package com.gosec.customrpc.transform;


import com.alibaba.fastjson.JSON;
import com.gosec.customrpc.client.ClientChannelHolder;
import com.gosec.customrpc.client.extractor.DefaultRequestArgumentExtractor;
import com.gosec.customrpc.client.extractor.RequestArgumentExtractInput;
import com.gosec.customrpc.client.extractor.RequestArgumentExtractOutput;
import com.gosec.customrpc.client.extractor.RequestArgumentExtractor;
import com.gosec.customrpc.protocol.message.MessageType;
import com.gosec.customrpc.protocol.message.RequestMessagePacket;
import com.gosec.customrpc.server.service.HelloService;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloServiceImplTransform implements HelloService {
    private static final RequestArgumentExtractor EXTRACTOR = new DefaultRequestArgumentExtractor();

    public HelloServiceImplTransform() {
    }

    public String sayHello(String name) {
        RequestArgumentExtractInput input = new RequestArgumentExtractInput();
        input.setInterfaceKlass(HelloService.class);
        input.setMethod(HelloService.class.getMethods()[0]);
        RequestArgumentExtractOutput output = EXTRACTOR.extract(input);
        RequestMessagePacket packet = new RequestMessagePacket();
        packet.setMagicNumber(1);
        packet.setVersion(1);
        packet.setSerialNumber("1");
        packet.setMessageType(MessageType.REQUEST);
        packet.setInterfaceName(output.getInterfaceName());
        packet.setMethodName(output.getMethodName());
        packet.setMethodArgumentSignatures((String[]) output.getMethodArgumentSignatures().toArray(new String[0]));
        packet.setMethodArguments(new Object[]{name});
//        return "success";
        Channel channel = ClientChannelHolder.CHANNEL_ATOMIC_REFERENCE.get();
        channel.writeAndFlush(packet);
        return String.format("[%s#%s]调用成功， 发送了[%s]到Netty Server[%s]", output.getInterfaceName(), output.getMethodName(), JSON.toJSONString(packet), channel.remoteAddress());
    }

    public static long ReflectHello() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        long before = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Class<?> tClass = Class.forName("com.gosec.customrpc.transform.HelloServiceImplTransform");
            Object r = tClass.newInstance();
            Method m = tClass.getDeclaredMethod("sayHello", String.class);
            m.invoke(r, "h");
        }

        long after = System.currentTimeMillis();
        return after - before;
    }

    public static long invokeHello() {
        long before = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            HelloServiceImplTransform h = new HelloServiceImplTransform();
            h.sayHello("h");
        }
        long after = System.currentTimeMillis();
        return after - before;
    }

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
//        System.out.println(ReflectHello());
        System.out.println(invokeHello());

    }
}


