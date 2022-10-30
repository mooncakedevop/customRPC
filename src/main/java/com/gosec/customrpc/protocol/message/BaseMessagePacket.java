package com.gosec.customrpc.protocol.message;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class BaseMessagePacket implements Serializable {
    /*
     * 魔数
     * */

    private int magicNumber;

    /*
     * 版本
     * */

    private int version;

    /*
     * 流水号
     * */

    private String serialNumber;

    /*
     * 消息类型
     * */

    private MessageType messageType;

    /*
     * 附件 K-V存储
     * */

    private Map<String, String> attachments = new HashMap<>();

    /*
     * 添加附件
     * */

    public void addAttachment(String key, String value) {
        attachments.put(key, value);
    }

}
