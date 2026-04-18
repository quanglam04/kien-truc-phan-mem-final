package com.costumemanagement.costumerental.service.payment;

import com.costumemanagement.costumerental.constant.PaymentMethod;
import com.costumemanagement.costumerental.entity.HoaDonNhap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ThanhToanTheProcessor implements ThanhToanProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ThanhToanTheProcessor.class);

    @Override
    public boolean pay(HoaDonNhap hoaDon) {
        logger.info("Xu ly thanh toan THE - Hoa don: {} - So tien: {}",
                hoaDon.getMaHDNhap(), hoaDon.getTongTien());
        return true;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.THE;
    }
}
