package com.test.bootpay.common;

import lombok.Getter;

@Getter
public enum ResponseEnum {

    BOOTPAY_SUBMIT_SUCCESS(0,"결제 승인에 성공했습니다."),
    BOOTPAY_SUBMIT_CANCEL(1,"결제가 취소되었습니다."),
    BOOTPAY_SUBMIT_FAIL(2,"결제에 실패했습니다."),
    BOOTPAY_CANCEL_SUCCESS(0,"결제 취소를 성공했습니다."),
    BOOTPAY_CANCEL_FAIL(1,"결제 취소를 실패했습니다."),
    ORDER_NOT_FOUND(2,"주문을 찾을 수 없습니다.");

    private final int code;
    private final String message;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
