package com.gosec.customrpc.protocol.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseMessagePacket extends BaseMessagePacket{

    /*
    * 错误码
    * */

    private long errorCode;

    /*
    * 消息描述
    * */

    private String message;

    /*
    * 消息载荷
    * */

    private Object payload;

}
