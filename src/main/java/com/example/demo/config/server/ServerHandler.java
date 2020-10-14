package com.example.demo.config.server;

import com.example.demo.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

  public static final List<Channel> CHANNELS = new ArrayList<>();
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {

    //Вызывается когда от клиента приходит очередной пакет.
    try {
      var channel = ctx.channel();
      CHANNELS.add(channel);
      var receiveMessage = msg.toString();
      log.info("channelRead: receiveMessage={}", receiveMessage);

//      var responseMessage = "Hello from server";       //XML

      var message = objectMapper.writeValueAsString(
        Message.of(
          new Random().nextInt(10),
          new Random().nextInt(10)
        )
      );

      channel.writeAndFlush(message + "\0");

    } catch (Exception ex) {
      log.error("channelRead: exception", ex);
    }

    // Discard the received data silently.
//        ((ByteBuf) msg).release(); // (3)
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
  }
}
