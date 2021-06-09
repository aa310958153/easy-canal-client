package com.wine.easy.canal.web;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.web
 * @ClassName HttpServerInitializer
 * @Author qiang.li
 * @Date 2021/6/1 4:21 下午
 * @Description TODO
 */
public final class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private CanalClientELKController canalClientELKController=null;
    public HttpServerInitializer(CanalClientELKController canalClientELKController){
        this.canalClientELKController=canalClientELKController;
    }
    @Override
    protected void initChannel(final SocketChannel socketChannel) {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast(new HttpServerCodec());
        channelPipeline.addLast(new HttpObjectAggregator(65536));
        channelPipeline.addLast(new HttpServerHandler(canalClientELKController));
    }
}
