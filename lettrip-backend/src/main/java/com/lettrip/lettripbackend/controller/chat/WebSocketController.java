package com.lettrip.lettripbackend.controller.chat;

import com.lettrip.lettripbackend.controller.chat.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

/*
    /sub/channel/12345          - 구독(channelId:12345)
    /pub//message                  - 메시지 발행
 */
@RestController
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    public void sendMessage(ChatDto chatDto, SimpMessageHeaderAccessor accessor) {
        simpMessagingTemplate.convertAndSend("/sub/channel/"+chatDto.getChannelId(), chatDto);
        // /sub/channel/{channelId} 채널을 구독 중인 클라이언트에게 메시지를 전송
        // 메시지의 payload는 인자(chatDto)로 들어옴
    }
}
