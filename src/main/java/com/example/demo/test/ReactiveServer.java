package com.example.demo.test;

import com.example.demo.config.server.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Slf4j
public class ReactiveServer {

    public static final int PORT = 6666;
    public static EventLoopGroup bossGroup = new NioEventLoopGroup();
    public static EventLoopGroup workerGroup = new NioEventLoopGroup();

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //Здесь указываем размер буфера (8192 байта) и символ-признак конца пакета.
                        //Свои пакеты мы обычно терминируем символом с кодом 0, что соответствует nulDelimiter() в терминологии нетти
                        ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.nulDelimiter()));
                        ch.pipeline().addLast("decoder", new StringDecoder()); //Стандартный строковый декодер.
                        ch.pipeline().addLast("encoder", new StringEncoder());
                        ch.pipeline().addLast("hendler", new ServerHandler());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(PORT)).sync();



    }
}
