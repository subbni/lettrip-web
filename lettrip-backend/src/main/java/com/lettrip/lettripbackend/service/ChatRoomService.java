package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.chat.dto.ChatDto;
import com.lettrip.lettripbackend.controller.chat.dto.ChatRoomDto;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.ResourceNotFoundException;
import com.lettrip.lettripbackend.mongo.domain.ChatRoom;
import com.lettrip.lettripbackend.mongo.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final UserService userService;
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
    public void updateLastMessage(String roomId, String lastMessage, LocalDateTime lastMessageTime) {
        ChatRoom chatRoom = findChatRoomById(roomId);
        chatRoom.updateLastMessageAndTime(lastMessage, lastMessageTime);
        chatRoomRepository.save(chatRoom);
    }

    public ChatRoom findChatRoomById(String roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(()-> new ResourceNotFoundException("ChatRoom","chatRoomId",roomId));
    }

    /* 사용자별 채팅 목록 */
    public Page<ChatRoomDto.Response> getUserChatRoomPage(Long userId, Pageable pageable) {
        User user = userService.findUserById(userId);
        Page<ChatRoom> page = chatRoomRepository.findAllByWriteUserIdOrRequestUserId(userId,userId, pageable);
        return new PageImpl<>(
                chatRoomToDto(page.getContent(), userId),
                pageable,
                page.getTotalElements()
        );
    }

    private List<ChatRoomDto.Response> chatRoomToDto(List<ChatRoom> chatRoomList, long userId) {
        return chatRoomList.stream()
                .map((chatRoom) -> getChatRoomDto(chatRoom, userId))
                .collect(Collectors.toList());
    }

    // 상대 User의 프로필을 찾아 ChatRoomDto의 participant에 넣는다.
    private ChatRoomDto.Response getChatRoomDto(ChatRoom chatRoom, long userId) {
        long participantId = (chatRoom.getWriteUserId() != userId) ? chatRoom.getWriteUserId() : chatRoom.getRequestUserId();
        User participantUser = userService.findUserById(participantId);
        return ChatRoomDto.Response.fromEntity(chatRoom).setParticipant(participantUser);

    }



}
