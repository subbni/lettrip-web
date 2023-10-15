package com.lettrip.lettripbackend.controller.chat;

import com.lettrip.lettripbackend.controller.chat.dto.ChatDto;
import com.lettrip.lettripbackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

/*
    /sub/chat/12345          - 구독(roomId:12345)
    /pub//message                  - 메시지 발행
 */
@RestController
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/message")
    public void sendMessage(ChatDto.Request chatDto, SimpMessageHeaderAccessor accessor) {
        chatService.saveChat(chatDto);
        simpMessagingTemplate.convertAndSend("/sub/chat/"+chatDto.getRoomId(), chatDto);
        // /sub/chat/{roomId} 채널을 구독 중인 클라이언트에게 메시지를 전송
        // 메시지의 payload는 인자(chatDto)로 들어옴
    }
}
