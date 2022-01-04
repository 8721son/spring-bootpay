package com.test.bootpay.repository;

import com.test.bootpay.domain.entity.BootPayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BootpayRepository extends JpaRepository<BootPayEntity,Integer> {
    Optional<BootPayEntity> findByOrderId(String order_id);
}
