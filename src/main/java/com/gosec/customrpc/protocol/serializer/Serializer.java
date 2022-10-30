package com.gosec.customrpc.protocol.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public interface Serializer {
    byte[] encode(Object object);
    Object decode(byte[] bytes, Class<?> targetClass);
}
