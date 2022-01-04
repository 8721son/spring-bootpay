package com.test.bootpay.domain.dto.request;

import lombok.Data;

@Data
public class CancelDTO {
    private String order_id;
    private String reason;
}
