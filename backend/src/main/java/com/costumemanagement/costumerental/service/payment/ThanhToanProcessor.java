package com.costumemanagement.costumerental.service.payment;

import com.costumemanagement.costumerental.constant.PaymentMethod;
import com.costumemanagement.costumerental.entity.HoaDonNhap;

public interface ThanhToanProcessor {

    boolean pay(HoaDonNhap hoaDon);

    PaymentMethod getPaymentMethod();
}
