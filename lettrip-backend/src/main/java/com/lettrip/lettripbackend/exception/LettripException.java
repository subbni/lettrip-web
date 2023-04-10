package com.lettrip.lettripbackend.exception;

import lombok.Getter;

@Getter
public class LettripException extends RuntimeException{
    private final LettripErrorCode lettripErrorCode;
    private final String detailMessage;

    public LettripException(LettripErrorCode lettripErrorCode) {
        super(lettripErrorCode.getMessage());
        this.lettripErrorCode = lettripErrorCode;
        this.detailMessage = lettripErrorCode.getMessage();
    }

    public LettripException(LettripErrorCode lettripErrorCode, String detailMessage) {
        super(detailMessage);
        this.lettripErrorCode = lettripErrorCode;
        this.detailMessage = detailMessage;
    }
}