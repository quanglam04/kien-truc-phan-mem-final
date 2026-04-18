package com.costumemanagement.costumerental.service.payment;

import com.costumemanagement.costumerental.constant.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ThanhToanFactory {

    private final Map<PaymentMethod, ThanhToanProcessor> processorMap;

    public ThanhToanFactory(List<ThanhToanProcessor> processors) {
        this.processorMap = processors.stream()
                .collect(Collectors.toMap(
                        ThanhToanProcessor::getPaymentMethod,
                        processor -> processor
                ));
    }

    public ThanhToanProcessor getProcessor(PaymentMethod method) {
        ThanhToanProcessor processor = processorMap.get(method);
        if (processor == null) {
            throw new IllegalArgumentException("Phuong thuc thanh toan khong hop le: " + method);
        }
        return processor;
    }
}