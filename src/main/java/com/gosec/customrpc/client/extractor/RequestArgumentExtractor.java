package com.gosec.customrpc.client.extractor;

public interface RequestArgumentExtractor {
    RequestArgumentExtractOutput extract(RequestArgumentExtractInput input);
}
