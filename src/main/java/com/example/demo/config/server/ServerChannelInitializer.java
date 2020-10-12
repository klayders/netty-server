package com.example.demo.config.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) {
    //Здесь указываем размер буфера (8192 байта) и символ-признак конца пакета.
    //Свои пакеты мы обычно терминируем символом с кодом 0, что соответствует nulDelimiter() в терминологии нетти
    ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.nulDelimiter()));
    ch.pipeline().addLast("decoder", new StringDecoder()); //Стандартный строковый декодер.
    ch.pipeline().addLast("encoder", new StringEncoder());
    ch.pipeline().addLast("hendler", new ServerHandler());
  }
}
