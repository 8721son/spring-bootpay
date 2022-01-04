package com.test.bootpay.domain.dto.request;

import lombok.Data;

@Data
public class BootpayFeedbackDTO {
    private String receipt_id;
    private String pg;
    private String pg_name;
    private String method;
    private String method_name;
    private String application_id;
    private String name;
    private String private_key;
    private String order_id;
    private String receipt_url;
    private PaymentDataDTO payment_data;
    private int price;
    private int status;
}
