package com.gosec.customrpc.server.methodMatch;

public class MethodMatchException extends Exception{
    public MethodMatchException(String message) {
        super(message);
    }

    public MethodMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodMatchException(Throwable cause) {
        super(cause);
    }
}
