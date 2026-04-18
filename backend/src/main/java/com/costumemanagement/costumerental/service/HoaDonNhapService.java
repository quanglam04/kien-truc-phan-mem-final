package com.costumemanagement.costumerental.service;

import com.costumemanagement.costumerental.entity.HoaDonNhap;
import java.util.List;

public interface HoaDonNhapService {

    List<HoaDonNhap> findAll();
    HoaDonNhap findById(String id);
    HoaDonNhap createImport(HoaDonNhap hoaDonNhap);       
    HoaDonNhap payImport(String id, String phuongThuc);    
}