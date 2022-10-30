package com.gosec.customrpc.server.methodConvert;

public interface MethodArgumentConverter {
    ArgumentConvertOutput convert(ArgumentConvertInput input) throws ArgumentConvertException;
}
