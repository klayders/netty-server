package com.example.demo.config;

import com.example.demo.config.server.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Slf4j
@Configuration
public class ServerConfiguration {

  @Value("${netty.port}")
  private int nettyPort;

  @Bean
  EventLoopGroup parentEventLoopGroup() {
    return new NioEventLoopGroup();
  }

  @Bean
  EventLoopGroup childEventLoopGroup() {
    return new NioEventLoopGroup();
  }

  @Bean
  ServerChannelInitializer serverChannelInitializer() {
    return new ServerChannelInitializer();
  }

  @Bean
  ChannelFuture serverBootstrap(EventLoopGroup parentEventLoopGroup,
                                EventLoopGroup childEventLoopGroup,
                                ServerChannelInitializer serverChannelInitializer) throws InterruptedException, UnknownHostException {
    log.info("serverBootstrap: starting Listen to users on={}:{} ", InetAddress.getLocalHost().toString(), nettyPort);

    return new ServerBootstrap()
      .group(parentEventLoopGroup, childEventLoopGroup)
      .childHandler(serverChannelInitializer)
      .channel(NioServerSocketChannel.class)
      .option(ChannelOption.TCP_NODELAY, true)
      .option(ChannelOption.SO_KEEPALIVE, true)
      .bind(new InetSocketAddress(nettyPort))
      .sync();
  }

  // Wait until the server socket is closed.
  // In this example, this does not happen, but you can do that to gracefully
  // shut down your server.
//        channelFuture.channel().closeFuture().sync();

}
