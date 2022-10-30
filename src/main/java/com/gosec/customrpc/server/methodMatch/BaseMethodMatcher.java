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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public abstract class BaseMethodMatcher implements MethodMatcher {
    private final ConcurrentMap<MethodMatchInput, MethodMatchOutput> cache = Maps.newConcurrentMap();

    @Override
    public MethodMatchOutput selectOneBestMatchMethod(MethodMatchInput input) throws MethodMatchException {
        return cache.computeIfAbsent(input, in -> {
            try {
                MethodMatchOutput output = new MethodMatchOutput();
                Class<?> interfaceClass = Class.forName(in.getInterfaceName());
                // 获取宿主类信息
                HostClassMethodInfo info = findHostMethodInfo(interfaceClass);
                List<Method> targetMethods = Lists.newArrayList();
                ReflectionUtils.doWithMethods(info.getHostUserklass(), targetMethods::add, method -> {
                    String methodName = method.getName();
                    Class<?> declaringClass = method.getDeclaringClass();
                    List<Class<?>> inputParameterTypes = Optional.ofNullable(in.getMethodArgumentSignatures())
                            .map(mas -> {
                                List<Class<?>> list = Lists.newArrayList();
                                mas.forEach(ma -> list.add(ClassUtils.resolveClassName(ma, null)));
                                return list;
                            }).orElse(Lists.newArrayList());
                    output.setParameterTypes(inputParameterTypes);
                    // 如果传入了参数签名列表，优先使用参数签名列表类型进行匹配
                    if (!inputParameterTypes.isEmpty()) {
                        List<Class<?>> parameterTypes = Lists.newArrayList(method.getParameterTypes());
                        return Objects.equals(methodName, in.getMethodName()) &&
                                Objects.equals(info.getHostUserklass(), declaringClass) &&
                                Objects.equals(parameterTypes, inputParameterTypes);
                    }
                    // 如果没有传入参数签名列表，那么使用参数的数量进行匹配
                    if (in.getMethodArgumentArraySize() > 0) {
                        List<Class<?>> parameterTypes = Lists.newArrayList(method.getParameterTypes());
                        return Objects.equals(methodName, in.getMethodName()) &&
                                Objects.equals(info.getHostUserklass(), declaringClass) &&
                                in.getMethodArgumentArraySize() == parameterTypes.size();

                    }
                    // 如果参数签名列表和参数列表都没有传入，那么只能通过方法名称和方法实例的宿主类型匹配
                    return Objects.equals(methodName, in.getMethodName()) &&
                            Objects.equals(info.getHostUserklass(), declaringClass);
                });
                if (targetMethods.size() != 1) {
                    throw new MethodMatchException(String.format("查找到目标方法数量不等于1,interface:%s,method:%s",
                            in.getInterfaceName(), in.getMethodName()));
                }
                Method targetMethod = targetMethods.get(0);
                output.setTargetKlass(info.getHostKlass());
                output.setTargetMethod(targetMethod);
                output.setTargetUserKlass(info.getHostUserklass());
                output.setTarget(info.getHostTarget());
                return output;
            } catch (Exception e) {
                log.error("查找匹配度最高的方法失败,输入参数:{}", JSON.toJSONString(in), e);
                e.printStackTrace();
//                if (e instanceof MethodMatchException) {
//                    throw (MethodMatchException) e;
//                } else {
//                    throw new MethodMatchException(e);
//                }
            }
            return null;
        });
    }

    public abstract HostClassMethodInfo findHostMethodInfo(Class<?> interfaceClass);
}
