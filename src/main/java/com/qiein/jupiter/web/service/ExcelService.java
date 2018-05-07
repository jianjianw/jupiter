package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.dto.ClientExcelDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 导入导出业务层
 */
public interface ExcelService {

    public void importExcel(MultipartFile file, StaffPO currentLoginStaff) throws Exception;
}
