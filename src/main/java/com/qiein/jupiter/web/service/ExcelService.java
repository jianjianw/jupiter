package com.qiein.jupiter.web.service;

import org.springframework.web.multipart.MultipartFile;

import com.qiein.jupiter.web.entity.po.StaffPO;

/**
 * 导入导出业务层
 */
public interface ExcelService {

    void importExcel(MultipartFile file, StaffPO currentLoginStaff) throws Exception;
}
