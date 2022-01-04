package com.test.bootpay.domain.dto.request;

import lombok.Data;

@Data
public class ReceiptDataDTO {
    private String receipt_id;
    private String order_id;
    private String name;
    private String item_name;
    private int price;
    private int tax_free;
    private int remain_price;
    private int remain_tax_free;
    private int cancelled_price;
    private int cancelled_tax_free;
    private String receipt_url;
    private String unit;
    private String pg;
    private String method;
    private String pg_name;
    private String method_name;
    private PaymentDataDTO payment_data;
    private String requested_at;
    private String purchased_at;
    private String revoked_at;
    private int status;
    private String status_en;
    private String status_ko;

}
