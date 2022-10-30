package com.gosec.customrpc.server.methodMatch;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode
@Data
public class MethodMatchInput {
    private String interfaceName;
    private String methodName;
    private List<String> methodArgumentSignatures;
    private int methodArgumentArraySize;

}
