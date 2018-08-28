package com.devchloe.netty.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient {

    private final String host;
    private final int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new ChatClient("localhost", 8080).run();
    }

    public void run() throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientInitializer());

            // connect가 되면 서버와 통신할 수 있는 채널을 얻을 수 있다
            Channel channel = bootstrap.connect(host, port).sync().channel(); // sync의미
            BufferedReader messageFromConsole = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                channel.writeAndFlush(messageFromConsole.readLine() + "\r\n"); // flush 안하는 이유
            }
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
