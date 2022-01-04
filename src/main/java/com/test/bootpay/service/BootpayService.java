package com.test.bootpay.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.test.bootpay.Bootpay;
import com.test.bootpay.common.ResponseDTO;
import com.test.bootpay.common.ResponseEnum;
import com.test.bootpay.domain.dto.request.BootpayFeedbackDTO;
import com.test.bootpay.domain.dto.request.Cancel;
import com.test.bootpay.domain.dto.request.ReceiptDTO;
import com.test.bootpay.domain.dto.request.ReceiptIdDTO;
import com.test.bootpay.domain.entity.BootPayEntity;
import com.test.bootpay.domain.entity.BootPayLogEntity;
import com.test.bootpay.repository.BootpayRepository;
import com.test.bootpay.repository.BootpayLogRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BootpayService {

    private final Gson gson;

    private final BootpayRepository bootpayRepository;
    private final BootpayLogRepository bootpayLogRepository;

    @Value("${property.bootpay.application-id}")
    private String application_id;

    @Value("${property.bootpay.private-key}")
    private String private_key;

    @Transactional
    public boolean save(BootpayFeedbackDTO feedback) throws Exception{
        Bootpay object = new Bootpay(application_id,private_key);
        object.getAccessToken();
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse verifiedData = object.verify(feedback.getReceipt_id());
        String verifiedStr = IOUtils.toString(verifiedData.getEntity().getContent(), StandardCharsets.UTF_8);
        ReceiptDTO verifiedReceiptDTO = mapper.readValue(verifiedStr, new TypeReference<>(){});

        if (feedback.getStatus()==20){
            Optional<BootPayEntity> optional = bootpayRepository.findByOrderId(feedback.getOrder_id());
            if(optional.isPresent()) {
                optional.get().setStatus(verifiedReceiptDTO.getData().getStatus_ko());
            }else {
                return false;
            }
        }else{
            bootpayRepository.save(BootPayEntity.builder()
                    .price((long) feedback.getPrice())
                    .receiptId(feedback.getReceipt_id())
                    .orderId(feedback.getOrder_id())
                    .name(feedback.getName())
                    .unit(verifiedReceiptDTO.getData().getUnit())
                    .pg(feedback.getPg())
                    .method(feedback.getMethod())
                    .pgName(feedback.getPg_name())
                    .methodName(feedback.getMethod_name())
                    .paymentData(gson.toJson(feedback.getPayment_data()))
                    .receiptUrl(feedback.getReceipt_url())
                    .requestAt(verifiedReceiptDTO.getData().getRequested_at())
                    .purchasedAt(verifiedReceiptDTO.getData().getPurchased_at())
                    .status(verifiedReceiptDTO.getData().getStatus_ko())
                    .build());
        }


        bootpayLogRepository.save(BootPayLogEntity.builder()
                            .price((long) feedback.getPrice())
                            .receiptId(feedback.getReceipt_id())
                            .orderId(feedback.getOrder_id())
                            .name(feedback.getName())
                            .unit(verifiedReceiptDTO.getData().getUnit())
                            .pg(feedback.getPg())
                            .method(feedback.getMethod())
                            .pgName(feedback.getPg_name())
                            .methodName(feedback.getMethod_name())
                            .paymentData(gson.toJson(feedback.getPayment_data()))
                            .receiptUrl(feedback.getReceipt_url())
                            .requestAt(verifiedReceiptDTO.getData().getRequested_at())
                            .purchasedAt(verifiedReceiptDTO.getData().getPurchased_at())
                            .status(verifiedReceiptDTO.getData().getStatus_ko())
                            .build());
        return true;
    }

    @Transactional
    public HttpEntity<Object> submit(ReceiptIdDTO dto) throws Exception{
        Bootpay object = new Bootpay(application_id,private_key);
        object.getAccessToken();
        try {
            HttpResponse res = object.verify(dto.getReceipt_id());
            StatusLine statusLine = res.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                String str = IOUtils.toString(res.getEntity().getContent(), StandardCharsets.UTF_8);
                ObjectMapper mapper = new ObjectMapper();
                ReceiptDTO receiptDTO = mapper.readValue(str, new TypeReference<>(){});
                bootpayLogRepository.save(BootPayLogEntity.builder()
                                    .price((long) receiptDTO.getData().getPrice())
                                    .receiptId(receiptDTO.getData().getReceipt_id())
                                    .orderId(receiptDTO.getData().getOrder_id())
                                    .name(receiptDTO.getData().getName())
                                    .unit(receiptDTO.getData().getUnit())
                                    .pg(receiptDTO.getData().getPg())
                                    .method(receiptDTO.getData().getMethod())
                                    .pgName(receiptDTO.getData().getPg_name())
                                    .methodName(receiptDTO.getData().getMethod_name())
                                    .paymentData(gson.toJson(receiptDTO.getData().getPayment_data()))
                                    .receiptUrl(receiptDTO.getData().getReceipt_url())
                                    .requestAt(receiptDTO.getData().getRequested_at())
                                    .purchasedAt(receiptDTO.getData().getPurchased_at())
                                    .status(receiptDTO.getData().getStatus_ko())
                                    .build());
                // TODO : 주문금액, 수량 등 검증 추가
                if(receiptDTO.getData().getStatus()==2 && receiptDTO.getStatus()==200) {
                    object.submit(dto.getReceipt_id());
                    return new ResponseEntity<>(new ResponseDTO<>(ResponseEnum.BOOTPAY_SUBMIT_SUCCESS, dto.getReceipt_id()), HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(new ResponseDTO<>(ResponseEnum.BOOTPAY_SUBMIT_CANCEL), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ResponseDTO<>(ResponseEnum.BOOTPAY_SUBMIT_FAIL), HttpStatus.OK);
    }

    public HttpEntity<Object> cancel(String order_id,String reason) throws Exception{
        Bootpay object = new Bootpay(application_id,private_key);
        object.getAccessToken();
        Optional<BootPayEntity> optional = bootpayRepository.findByOrderId(order_id);
        if (optional.isPresent()) {

            BootPayEntity entity = optional.get();

            Cancel cancel = new Cancel();
            cancel.receiptId = entity.getReceiptId();
            cancel.name = entity.getName();
            cancel.reason = reason;

            try {
                HttpResponse res = object.receiptCancel(cancel);
                String str = IOUtils.toString(res.getEntity().getContent(), StandardCharsets.UTF_8);
                return new ResponseEntity<>(new ResponseDTO<>(ResponseEnum.BOOTPAY_CANCEL_SUCCESS), HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(new ResponseDTO<>(ResponseEnum.BOOTPAY_CANCEL_FAIL), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseDTO<>(ResponseEnum.ORDER_NOT_FOUND), HttpStatus.OK);
        }
    }
}
