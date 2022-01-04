package com.test.bootpay.controller;

import com.test.bootpay.domain.dto.request.BootpayFeedbackDTO;
import com.test.bootpay.domain.dto.request.CancelDTO;
import com.test.bootpay.domain.dto.request.ReceiptIdDTO;
import com.test.bootpay.service.BootpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/pay")
@RequiredArgsConstructor
public class BootpayRestApiV1Controller {

    private final BootpayService bootpayService;

    /**
     * 부트페이 서버와 통신
     */
    @PostMapping("/feedback")
    public String test(@RequestBody BootpayFeedbackDTO feedback) throws Exception{
        boolean save = bootpayService.save(feedback);
        if(save){
            return "OK";
        }else{
            return null;
        }
    }

    @PostMapping("/cancel")
    public HttpEntity<Object> cancel(@RequestBody CancelDTO dto) throws Exception{
        return bootpayService.cancel(dto.getOrder_id(),dto.getReason());
    }

    @PostMapping("/confirm")
    public HttpEntity<Object> confirm(@RequestBody ReceiptIdDTO dto) throws Exception{
        return bootpayService.submit(dto);
    }

}
