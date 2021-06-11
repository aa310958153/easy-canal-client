package com.wine.easy.canal.web;

import com.wine.easy.canal.config.ELKConfig;
import com.wine.easy.canal.core.CanalListenerWorker;
import com.wine.easy.canal.core.ProcessListenerRegister;
import com.wine.easy.canal.interfaces.Register;
import com.wine.easy.canal.yaml.CanalClientConfigurationLoader;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.web
 * @ClassName HttpServerLoad
 * @Author qiang.li
 * @Date 2021/6/1 4:20 下午
 * @Description TODO
 */

public class HttpServerLoader implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(HttpServerLoader.class);

    private  CanalClientELKController canalClientELKController;
    private Register register;
    public HttpServerLoader(Register register){
      this.register=register;
    }

    public void start() throws IOException {
          new Thread(this).start();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        ELKConfig   elkConfig= null;
        try {
            elkConfig = CanalClientConfigurationLoader.loadElkConfig();
        } catch (IOException e) {
            log.error("配置文件加载失败",e);
        }
        canalClientELKController = new CanalClientELKController(register,elkConfig);
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer(canalClientELKController));
            int port = elkConfig.getPort();
            Channel channel = bootstrap.bind(port).sync().channel();
            log.info("canal-client is server on http://127.0.0.1:{}/", port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动服务异常",e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
