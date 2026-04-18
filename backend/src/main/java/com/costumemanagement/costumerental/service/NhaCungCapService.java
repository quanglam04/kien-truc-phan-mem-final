package com.costumemanagement.costumerental.service;

import com.costumemanagement.costumerental.entity.NhaCungCap;
import java.util.List;
import java.util.Map;

public interface NhaCungCapService {

    List<NhaCungCap> findAll();
    NhaCungCap findById(String id);
    NhaCungCap save(NhaCungCap ncc);
    NhaCungCap update(String id, NhaCungCap ncc);
    boolean delete(String id);
    List<Map<String, Object>> getStats();           
    Map<String, Object> getStatsById(String maNCC);
}