package com.example.demo.service;

import com.example.demo.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Random;

import static com.example.demo.config.server.ServerHandler.CHANNELS;

@Slf4j
@Component
public class SenderToClientService {

  private static final ObjectMapper objectMapper = new ObjectMapper();


  @Scheduled(fixedDelay = 100)
  public void send() throws JsonProcessingException {
    if (!CollectionUtils.isEmpty(CHANNELS)) {
      var ogogo = CHANNELS.get(0);

      var message = objectMapper.writeValueAsString(
        Message.of(
          new Random().nextFloat(),
          new Random().nextFloat()
        )
      );

      ogogo.writeAndFlush(message + "\0");
    } else {
      log.info("empty");
    }
  }
}
