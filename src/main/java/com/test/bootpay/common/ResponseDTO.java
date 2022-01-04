package com.test.bootpay.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ResponseDTO<T> {

    private final Integer code;
    private final String message;
    private final T content;

    public ResponseDTO(ResponseEnum respEnum, T content) {
        this.code = respEnum.getCode();
        this.message = respEnum.getMessage();
        this.content = content;
    }

    public ResponseDTO(ResponseEnum respEnum) {
        this.code = respEnum.getCode();
        this.message = respEnum.getMessage();
        this.content = null;
    }
}
