package com.gosec.customrpc.client.extractor;

import lombok.Data;

import java.util.List;

@Data
public class RequestArgumentExtractOutput {
    private String interfaceName;
    private String methodName;
    private List<String> methodArgumentSignatures;
}
