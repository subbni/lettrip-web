package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.chat.dto.ChatDto;
import com.lettrip.lettripbackend.mongo.domain.Chat;
import com.lettrip.lettripbackend.mongo.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomService chatRoomService;

    @Transactional
    public void saveChat(ChatDto.Request request) {
        Chat chat = chatRepository.save(
                Chat.builder()
                        .roomId(request.getRoomId())
                        .message(request.getMessage())
                        .sendUserId(request.getSenderId())
                        .receiveUserId(request.getReceiverId())
                        .isImage(request.getIsImage())
                        .build()
        );
        chatRoomService.updateLastMessage(chat.getRoomId(), chat.getMessage(), chat.getCreatedAt());
    }

    public Page<ChatDto.Response> getChatHistory(String roomId, Pageable pageable) {
        Page<Chat> page = chatRepository.findAllByRoomId(roomId, pageable);
        return new PageImpl<ChatDto.Response>(
                chatToDto(page.getContent()),
                pageable,
                page.getTotalElements()
        );
    }

    private List<ChatDto.Response> chatToDto(List<Chat> chatList) {
        return chatList.stream()
                .map(ChatDto.Response::fromEntity)
                .collect(Collectors.toList());
    }
}
