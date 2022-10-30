package com.gosec.customrpc.protocol.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RequestMessagePacket extends BaseMessagePacket{

    /*
    * 接口全类名
    * */

    private String interfaceName;

    /*
    * 方法名
    * */

    private String methodName;

    /*
    * 方法参数签名
    * */

    private  String[] methodArgumentSignatures;

    /*
    * 方法参数
    * */

    private Object[] methodArguments;
}
