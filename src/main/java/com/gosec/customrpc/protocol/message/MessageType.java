package com.gosec.customrpc.protocol.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MessageType {

    /*
    * 请求
    * */
    REQUEST((byte) 1),

    /*
    * 响应
    * */
    RESPONSE((byte) 2),

    /*
    * PING
    * */
    PING((byte) 3),

    /*
    * PONG
    * */
    PONG((byte) 4),

    /*
    * NULL
    * */
    NULL((byte) 5),

    ;

    @Getter
    private final Byte type;

    public static MessageType fromValue(byte value){
        for(MessageType messageType: MessageType.values()){
            if (messageType.getType() == value){
                return messageType;
            }
        }
        throw new IllegalArgumentException(String.format("value = %s", value));
    }


}
