package com.gosec.customrpc.server.methodConvert;

public class ArgumentConvertException extends Exception{
    public ArgumentConvertException(String message) {
        super(message);
    }

    public ArgumentConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentConvertException(Throwable cause) {
        super(cause);
    }
}
