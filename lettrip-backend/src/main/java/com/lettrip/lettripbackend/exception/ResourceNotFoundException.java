package com.lettrip.lettripbackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public ResourceNotFoundException(
            String resourceName,
            String fieldName,
            Object fieldValue
    ) {
        super(String.format("%s : '%s' 인 %s가 존재하지 않습니다.",fieldName,fieldValue,resourceName));
        this.resourceName=resourceName;
        this.fieldName=fieldName;
        this.fieldValue=fieldValue;
    }

}
