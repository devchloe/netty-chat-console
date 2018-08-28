package com.devchloe.netty.chat.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatClientInitializer extends ChannelInitializer<SocketChannel> {

    // 생성된 SocketChannel에서 발생한 이벤트를 처리할 핸들러를 정의한다 (셋업)
    // 채널이 생성될 때 채널 초기화 작업 시 호출된다

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder()); // receivedBytes into String
        pipeline.addLast(new StringEncoder()); // String into sendingBytes
        pipeline.addLast(new ChatClientHandler());
    }
}
