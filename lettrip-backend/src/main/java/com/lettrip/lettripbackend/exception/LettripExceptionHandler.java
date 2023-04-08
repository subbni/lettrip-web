package com.lettrip.lettripbackend.exception;

import com.lettrip.lettripbackend.controller.LettripErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class LettripExceptionHandler {
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(LettripException.class)
    public LettripErrorResponse handleException(
            LettripException e, HttpServletRequest request
    ) {
        log.error("errorCode: {}, url: {}, message: {}",
                e.getLettripErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return LettripErrorResponse.builder()
                .errorCode(e.getLettripErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class, /* Mapping이 이상하게 들어왔을 때 */
            MethodArgumentNotValidException.class /* @Valid로 검증한 결과 오류가 발생했을 때 */
    })
    public LettripErrorResponse handleBadRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("url: {}, message: {}",
                request.getRequestURI(), e.getMessage());
        return LettripErrorResponse.builder()
                .errorCode(LettripErrorCode.INVALID_REQUEST)
                .errorMessage((LettripErrorCode.INVALID_REQUEST.getMessage()))
                .build();
    }

    @ExceptionHandler(Exception.class)
    public LettripErrorResponse handleException(
            Exception e, HttpServletRequest request /* 그 외 모든 Exception 처리 */
    ) {
        log.error("url: {}, message: {}",
                request.getRequestURI(), e.getMessage());
        return LettripErrorResponse.builder()
                .errorCode(LettripErrorCode.INTERNAL_SERVER_ERROR)
                .errorMessage((LettripErrorCode.INTERNAL_SERVER_ERROR.getMessage()))
                .build();
    }

}
