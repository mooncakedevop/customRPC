package com.gosec.customrpc.protocol.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public enum FastJsonSerializer implements Serializer {
    X;

    @Override
    public byte[] encode(Object object) {
        return JSONObject.toJSONBytes(object);
    }

    @Override
    public Object decode(byte[] bytes, Class<?> targetClass) {
        return JSON.parseObject(bytes, targetClass);
    }
}
