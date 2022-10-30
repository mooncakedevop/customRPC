package com.gosec.customrpc.client.extractor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

public class DefaultRequestArgumentExtractor implements RequestArgumentExtractor{
    private final ConcurrentMap<CacheKey, Object> cache = Maps.newConcurrentMap();
    @Override
    public RequestArgumentExtractOutput extract(RequestArgumentExtractInput input) {
        Class<?> interfaceKlass = input.getInterfaceKlass();
        Method method = input.getMethod();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();

        return (RequestArgumentExtractOutput) cache.computeIfAbsent(new CacheKey(interfaceKlass.getName(), methodName, Lists.newArrayList(parameterTypes)),
                x -> {
                    RequestArgumentExtractOutput output = new RequestArgumentExtractOutput();
                    output.setInterfaceName(interfaceKlass.getName());
                    List<String> methodArgumentSignatures = Lists.newArrayList();
                    for (Class<?> klass: parameterTypes) {
                        methodArgumentSignatures.add(klass.getName());
                    }
                    output.setMethodArgumentSignatures(methodArgumentSignatures);
                    output.setMethodName(methodName);
                    return output;
                });
    }
    @RequiredArgsConstructor
    private static class CacheKey{
        private final String interfaceName;
        private final String methodName;
        private final List<Class<?>> parameterTypes;

        public CacheKey(String interfaceName, String methodName, ArrayList<Class<?>> parameterTypes) {
            this.interfaceName = interfaceName;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(interfaceName,cacheKey.interfaceName) &&
                    Objects.equals(methodName, cacheKey.methodName) &&
                    Objects.equals(parameterTypes, cacheKey.parameterTypes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(interfaceName, methodName, parameterTypes);
        }
    }
}
