package com.lettrip.lettripbackend.controller.chat.dto;

import lombok.Data;

@Data
public class ChatDto {
    private Integer channelId;
    private Integer writerId;
    private String message;
}
