package com.qiein.jupiter.web.service;

import com.qiein.jupiter.web.entity.dto.ClientExcelDTO;
import com.qiein.jupiter.web.entity.dto.ClientExportDTO;
import com.qiein.jupiter.web.entity.vo.ClientExportVO;
import org.springframework.web.multipart.MultipartFile;

import com.qiein.jupiter.web.entity.po.StaffPO;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 导入导出业务层
 */
public interface ExcelService {
    /**
     * 导入客资
     *
     * @param file
     * @param currentLoginStaff
     * @throws Exception
     */
    void importExcel(MultipartFile file, StaffPO currentLoginStaff) throws Exception;

    /**
     * 获取所有上传客资记录
     *
     * @param companyId
     * @param staffId
     * @return
     */
    HashMap<String, List<ClientExcelDTO>> getAllUploadRecord(int companyId, int staffId);

    /**
     * 缓存表客资转移到info表
     *
     * @param companyId
     * @param staffId
     */
    void tempKzMoveToInfo(int companyId, int staffId);

    /**
     * 批量删除客资缓存记录
     *
     * @param companyId
     * @param operaId
     * @param kzIds
     */
    void batchDeleteTemp(int companyId, int operaId, String kzIds);

    /**
     * 编辑客资缓存记录
     *
     * @param companyId
     * @param info
     */
    void editKz(int companyId, ClientExcelDTO info);

    /**
     * 清空导入的客资列表
     *
     * @param companyId
     * @param operaId
     */
    void deleteTempByStaffId(int companyId, int operaId);

    /**
     * 导出客资
     * @param staffPO
     * @param clientExportDTO
     * @return
     */
    List<ClientExportVO>  Export(StaffPO staffPO, ClientExportDTO clientExportDTO);
}
