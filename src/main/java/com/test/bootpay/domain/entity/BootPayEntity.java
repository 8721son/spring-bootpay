package com.test.bootpay.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_bootpay")
public class BootPayEntity {

    /**
     * idx : 고유 번호
     * price : 가격
     * receiptId : 부트페이에서 발급하는 고유 영수증 ID
     * orderId : 부트페이로 부터 결제 요청시 보냈던 주문번호
     * name : 판매된 대표 상품명
     * unit : 판매된 결제 단위 (ex, 한국 원, usd - 미국 달러)
     * pg : 결제된 PG의 Alias(ex, dana, incis, kcp)
     * method : 결제된 수단 Alias(ex. car, vbank, bank,phone)
     * pgName : 결제된 PG사의 명칭
     * methodName : 결제된 수단의 명칭
     * paymentData : PG사에서 보내온 결제 raw 데이터
     * receiptUrl : 영수증 경로
     * requestAt : 결제가 처음 요ㅓㅇ된 시각 (한국 기준시 +9:00)
     * purchasedAt : 결제 승인이 된 시각 (한국 기준시 +9:00)
     * status : 결제 상태
     * createDate : 생성일
     * updateDate : 수정일
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private Long price;
    private String receiptId;
    private String orderId;
    private String name;
    private String unit;
    private String pg;
    private String method;
    private String pgName;
    private String methodName;
    private String paymentData;
    private String receiptUrl;
    private String requestAt;
    private String purchasedAt;
    private String status;

    @CreationTimestamp
    private LocalDateTime createDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;

}
