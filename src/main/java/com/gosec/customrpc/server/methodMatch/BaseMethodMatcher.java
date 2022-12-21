package com.gosec.customrpc.server.methodMatch;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.util.internal.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.sound.sampled.Line;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public abstract class BaseMethodMatcher implements MethodMatcher {
    private final ConcurrentMap<MethodMatchInput, MethodMatchOutput> cache = Maps.newConcurrentMap();

    @Override
    public MethodMatchOutput selectOneBestMatchMethod(MethodMatchInput in) throws MethodMatchException {

        MethodMatchOutput output = new MethodMatchOutput();
        Class<?> interfaceClass = null;
        try {
            interfaceClass = Class.forName(in.getInterfaceName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // 获取宿主类信息
        HostClassMethodInfo info = findHostMethodInfo(interfaceClass);
        Method[] targetMethods = info.getHostUserklass().getMethods();
        for (Method method: targetMethods) {
            if (method.getName().equals(in.getMethodName())) {
                if (method.getParameterTypes().length != in.getMethodArgumentArraySize()) continue;
                boolean match = true;
                for (int i = 0; i < method.getParameterTypes().length; i++) {
                    String inType = in.getMethodArgumentSignatures().get(i);
                    String parameterType = method.getParameterTypes()[i].getName();
                    if (!inType.equals(parameterType)){
                        match = false;
                        break;
                    }
                }
                if (match){
                    output.setParameterTypes(Arrays.asList(method.getParameterTypes()));
                    output.setTargetMethod(method);
                    output.setTargetKlass(info.getHostKlass());
                    output.setTargetUserKlass(info.getHostUserklass());
                    output.setTarget(info.getHostTarget());
                    return output;
                }
            }
        }

        Method targetMethod = targetMethods[0];
        output.setTargetKlass(info.getHostKlass());
        output.setTargetMethod(targetMethod);
        output.setTargetUserKlass(info.getHostUserklass());
        output.setTarget(info.getHostTarget());
        return output;
    }

    public abstract HostClassMethodInfo findHostMethodInfo(Class<?> interfaceClass);
}
