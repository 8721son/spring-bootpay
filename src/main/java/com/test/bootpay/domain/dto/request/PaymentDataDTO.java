package com.test.bootpay.domain.dto.request;

import lombok.Data;

@Data
public class PaymentDataDTO {
    private String receipt_id;

    //카드결제
   private String card_name;
    private String card_no;
    private String card_quota;
    private String card_code;
    private String card_auth_no;

    // 가상계좌
    private String bankname;
    private String accountholder;
    private String account;
    private String expiredate;
    private String username;
    private String cash_result;

    // 네이버페이
    private String naver_point;
    private String card_other_pay_type;

    // 휴대폰 결제
    private String ph;

    private String n;
    private int p;
    private String pg;
    private String pm;
    private String pg_a;
    private String pm_a;
    private String tid;
    private String o_id;
    private String p_at;
    private String r_at;
    private int s;
    private int g;
}
