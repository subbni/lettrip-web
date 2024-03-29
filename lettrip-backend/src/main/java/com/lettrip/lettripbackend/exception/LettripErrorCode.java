package com.lettrip.lettripbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LettripErrorCode {
    DUPLICATED_EMAIL("이미 해당 Email로 가입된 계정이 존재합니다."),
    DUPLICATED_NICKNAME("이미 존재하는 닉네임입니다."),
    USER_NOT_FOUND("해당 Email로 가입된 계정이 존재하지 않습니다."),
    EMAIL_CODE_NOT_MATCH("이메일 인증 코드가 일치하지 않습니다."),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다."),
    PASSWORD_CAN_NOT_BE_CHANGED("비밀번호를 변경할 수 없습니다."),
    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    INVALID_CITY_NAME("행정구역 내에 존재하지 않는 지역명입니다."),
    RESOURCE_NOT_FOUND("존재하지 않는 자원에 대한 요청입니다."),
    CANNOT_BE_CREATED_MULTIPLE_TIMES("중복 생성 불가능한 데이터입니다."),
    UNAUTHORIZED_ACCESS("권한이 없는 작업입니다.");

    private final String message;
}
