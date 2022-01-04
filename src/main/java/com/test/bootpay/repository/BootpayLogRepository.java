package com.test.bootpay.repository;

import com.test.bootpay.domain.entity.BootPayLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BootpayLogRepository extends JpaRepository<BootPayLogEntity,Integer> {
}
