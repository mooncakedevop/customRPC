package com.gosec.customrpc.server;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.gosec.customrpc.protocol.message.MessageType;
import com.gosec.customrpc.protocol.message.RequestMessagePacket;
import com.gosec.customrpc.protocol.message.ResponseMessagePacket;
import com.gosec.customrpc.server.methodConvert.*;
import com.gosec.customrpc.server.methodMatch.*;
import com.gosec.customrpc.server.service.DefaultHelloService;
import com.gosec.customrpc.server.service.HelloService;
import com.gosec.customrpc.server.threadPool.MyThreadPool;
import com.sun.xml.internal.ws.api.message.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<RequestMessagePacket> {

    private MethodMatcher methodMatcher = new BaseMethodMatcher() {
        @Override
        public HostClassMethodInfo findHostMethodInfo(Class<?> interfaceClass) {
            HostClassMethodInfo info = new HostClassMethodInfo();
            info.setHostTarget(new DefaultHelloService());
            info.setHostUserklass(DefaultHelloService.class);
            info.setHostKlass(HelloService.class);
            return info;
        }
    };

    private MethodArgumentConverter methodArgumentConverter = new DefaultMethodArgumentConverter();

    private MyThreadPool pool = new MyThreadPool();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessagePacket packet) {
        pool.getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    resolvePacket(ctx, packet);
                } catch (MethodMatchException | ArgumentConvertException | InvocationTargetException |
                         IllegalAccessException e) {
                    log.error("resolve err: ",e.getMessage());
                }
            }
        });



    }
    public void resolvePacket(ChannelHandlerContext ctx,RequestMessagePacket packet) throws MethodMatchException, ArgumentConvertException, InvocationTargetException, IllegalAccessException {
        log.info("服务端收到： ", packet.getMethodName());
        MethodMatchInput input = new MethodMatchInput();
        input.setInterfaceName(packet.getInterfaceName());
        input.setMethodArgumentSignatures(Optional.ofNullable(packet.getMethodArgumentSignatures()).map(Lists::newArrayList).orElse(Lists.newArrayList()));
        input.setMethodName(packet.getMethodName());
        Object[] methodArguments = packet.getMethodArguments();
        input.setMethodArgumentArraySize(null != methodArguments ? methodArguments.length: 0);
        MethodMatchOutput output = methodMatcher.selectOneBestMatchMethod(input);
        log.info("查找目标方法执行成功， 目标类：{}, 宿主类: {}, 宿主方法: {}", output.getTargetKlass().getCanonicalName(),
                output.getTargetUserKlass().getCanonicalName(),
                output.getTargetMethod().getName()
        );
        Method method = output.getTargetMethod();
        ArgumentConvertInput convertInput = new ArgumentConvertInput();
        convertInput.setArguments(input.getMethodArgumentArraySize() > 0 ? Lists.newArrayList(methodArguments): Lists.newArrayList());
        convertInput.setMethod(output.getTargetMethod());
        convertInput.setParameterTypes(output.getParameterTypes());

        ArgumentConvertOutput convertOutput =  methodArgumentConverter.convert(convertInput);
        ReflectionUtils.makeAccessible(method);

        //反射调用

        Object result = method.invoke(output.getTarget(), convertOutput.getArguments());
        ResponseMessagePacket  response = new ResponseMessagePacket();
        response.setMagicNumber(packet.getMagicNumber());
        response.setVersion(packet.getVersion());
        response.setSerialNumber(packet.getSerialNumber());
        response.setAttachments(packet.getAttachments());
        response.setMessageType(MessageType.RESPONSE);
        response.setErrorCode(200L);
        response.setMessage("success");
        response.setPayload(JSON.toJSONString(result));
        log.info("服务端输出： {}", JSON.toJSONString(response));
        ctx.writeAndFlush(response);
    }
}
