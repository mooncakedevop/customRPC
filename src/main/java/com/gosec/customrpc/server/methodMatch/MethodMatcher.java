package com.gosec.customrpc.server.methodMatch;

public interface MethodMatcher {
    /**
     * 查找一个匹配度最高的方法信息
     *
     * @param input input
     * @return output
     */
    MethodMatchOutput selectOneBestMatchMethod(MethodMatchInput input) throws MethodMatchException;
}
