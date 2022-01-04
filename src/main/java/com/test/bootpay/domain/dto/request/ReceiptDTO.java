package com.test.bootpay.domain.dto.request;

import lombok.Data;

@Data
public class ReceiptDTO {
    private int status;
    private int code;
    private String message;
    private ReceiptDataDTO data;
}
