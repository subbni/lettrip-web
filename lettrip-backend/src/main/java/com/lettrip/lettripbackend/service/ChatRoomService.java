package com.lettrip.lettripbackend.service;

import com.lettrip.lettripbackend.controller.chat.dto.ChatDto;
import com.lettrip.lettripbackend.controller.chat.dto.ChatRoomDto;
import com.lettrip.lettripbackend.domain.meetup.MeetUpPost;
import com.lettrip.lettripbackend.domain.user.User;
import com.lettrip.lettripbackend.exception.LettripErrorCode;
import com.lettrip.lettripbackend.exception.LettripException;
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
    private final MeetUpPostService meetUpPostService;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoomDto.Response saveChatRoom(ChatRoomDto.Request request, Long userId) {
        User user = userService.findUserById(userId);
        User participant = userService.findUserById(request.getRequestUserId());
        MeetUpPost meetUpPost = meetUpPostService.findMeetUpPostById(request.getMeetUpPostId());

        checkProcessBeforeChatRoomCreation(user,participant,meetUpPost);
        ChatRoom chatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .meetUpPostId(request.getMeetUpPostId())
                        .writeUserId(request.getWriteUserId())
                        .requestUserId(request.getRequestUserId())
                        .build()
        );

        return ChatRoomDto.Response.fromEntity(chatRoom).setParticipant(participant);
    }

    public void checkProcessBeforeChatRoomCreation(User user, User participant, MeetUpPost meetUpPost) {
        // chatRoom 생성 요청은 MeetUpPost 작성자만 가능하다고 가정하며, 이는 반드시 지켜져야 한다.
        if(!meetUpPost.getUser().equals(user)) {
            throw new LettripException(LettripErrorCode.UNAUTHORIZED_ACCESS,"채팅방 생성은 매칭글 작성자만 가능한 작업입니다.");
        }
        // 같은 MeetUpPost에 대한 두 사람의 채팅방이 존재하는지 확인
        if(hasChatRoom(user,participant,meetUpPost)) {
            throw new LettripException(LettripErrorCode.CANNOT_BE_CREATED_MULTIPLE_TIMES,"이미 해당 매칭글에 대한 채팅방이 존재합니다.");
        }
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
        return ChatRoomDto.Response.fromEntity(chatRoom).setParticipant(participantUser).setCurrentUserId(userId);

    }

    private Boolean hasChatRoom(User user, User participant, MeetUpPost meetUpPost) {
        ChatRoom chatRoom = chatRoomRepository.findByWriteUserIdAndRequestUserIdAndMeetUpPostId(
                user.getId(), participant.getId(), meetUpPost.getId()
        ).orElse(null);
        return chatRoom!=null;
    }
}
