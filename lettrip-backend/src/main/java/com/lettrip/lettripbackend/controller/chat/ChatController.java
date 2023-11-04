package com.lettrip.lettripbackend.controller.chat;

import com.lettrip.lettripbackend.controller.ApiResponse;
import com.lettrip.lettripbackend.controller.chat.dto.ChatDto;
import com.lettrip.lettripbackend.controller.chat.dto.ChatRoomDto;
import com.lettrip.lettripbackend.security.CurrentUser;
import com.lettrip.lettripbackend.security.CustomUserDetails;
import com.lettrip.lettripbackend.service.ChatRoomService;
import com.lettrip.lettripbackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @PostMapping("/room/create")
    public ChatRoomDto.Response createChat(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody ChatRoomDto.Request request
            ) {
        return chatRoomService.saveChatRoom(request,customUserDetails.getId());
    }


    @PostMapping("/create")
    public ApiResponse createChat(
            @CurrentUser CustomUserDetails customUserDetails,
            @RequestBody ChatDto.Request chatDto
            ) {
        /* just for test, DO NOT USE THIS METHOD */
        chatService.saveChat(chatDto);
        return new ApiResponse(true,"채팅 전송 완료");
    }

    @GetMapping("/{roomId}")
    public Page<ChatDto.Response> showChatHistory(
            @CurrentUser CustomUserDetails customUserDetails,
            @PathVariable("roomId") String roomId,
            Pageable pageable
    ) {
        return chatService.getChatHistory(roomId, pageable);
    }

    @GetMapping("/my")
    public Page<ChatRoomDto.Response> showUserChatRoomPage(
            @CurrentUser CustomUserDetails customUserDetails,
            Pageable pageable
    ) {
        return chatRoomService.getUserChatRoomPage(customUserDetails.getId(), pageable);
    }

}
