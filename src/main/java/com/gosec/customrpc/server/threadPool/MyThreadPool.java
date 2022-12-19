package com.gosec.customrpc.server.threadPool;

import sun.nio.ch.ThreadPool;

import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPool {
    private ThreadPoolExecutor executor;

    public void init() {
        executor = new ThreadPoolExecutor(60, 100, 60, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    public ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            init();
        }
        return executor;
    }

}