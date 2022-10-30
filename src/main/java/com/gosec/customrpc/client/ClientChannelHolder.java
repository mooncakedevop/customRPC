package com.gosec.customrpc.client;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicReference;

public class ClientChannelHolder {
    public static final AtomicReference<Channel> CHANNEL_ATOMIC_REFERENCE = new AtomicReference<>();
}
