package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.chat.dto.ChatRoomDto;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.mongo.domain.ChatRoom;
import com.lettrip.lettripbackend.mongo.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoomDto.Response saveChatRoom(ChatRoomDto.Request request) {
        ChatRoom chatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .meetUpPostId(request.getMeetUpPostId())
                        .writeUserId(request.getWriteUserId())
                        .requestUserId(request.getRequestUserId())
                        .build()
        );
        return ChatRoomDto.Response.fromEntity(chatRoom);
    }

    @Transactional
    public void updateLastMessage(String roomId, String lastMessage) {
        ChatRoom chatRoom = findChatRoomById(roomId);
        chatRoom.updateLastMessage(lastMessage);
        chatRoomRepository.save(chatRoom);
    }

    public ChatRoom findChatRoomById(String roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(()-> new ResourceNotFoundException("ChatRoom","chatRoomId",roomId));
    }
}
