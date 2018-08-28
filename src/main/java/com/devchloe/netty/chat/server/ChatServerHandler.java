package com.devchloe.netty.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.ImmediateEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private final ChannelGroup channels;

    public ChatServerHandler(ChannelGroup channelGroup) {
        this.channels = channelGroup;
    }
//    private static final ChannelGroup channels = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.writeAndFlush("[SERVER] " + incoming.remoteAddress() + " has joined\r\n");
        channels.add(incoming);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.writeAndFlush("[SERVER] " + incoming.remoteAddress() + " has left\r\n");
        channels.remove(incoming);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        incoming.writeAndFlush("Welcome! " + incoming.remoteAddress() + "\r\n");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String receivedMsg) throws Exception {
        Channel incoming = ctx.channel();
        incoming.writeAndFlush("[you] " + receivedMsg + "\r\n");

        String sendingMsg = "[" + incoming.remoteAddress() + "] " + receivedMsg + "\r\n";
        channels.writeAndFlush(sendingMsg, getChannelMatcher(incoming));
    }

    private ChannelMatcher getChannelMatcher(final Channel incoming) {
        return new ChannelMatcher() {
            @Override
            public boolean matches(Channel channel) {
                return !channel.equals(incoming);
            }
        };
    }
}
