package com.qiein.jupiter.web.service;

import java.util.HashMap;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.web.entity.dto.ClientExcelNewsDTO;
import com.qiein.jupiter.web.entity.dto.ClientSortCountDTO;
import org.springframework.web.multipart.MultipartFile;

import com.qiein.jupiter.web.entity.dto.ClientExcelDTO;
import com.qiein.jupiter.web.entity.dto.ClientExportDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientExportVO;

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
	HashMap<String, List<ClientExcelNewsDTO>> getAllUploadRecord(int companyId, int staffId);

	/**
	 * 缓存表客资转移到info表
	 *
	 * @param companyId
	 * @param staffId
	 */
	void tempKzMoveToInfo(int companyId, int staffId,String nickName);

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
	void editKz(int companyId, ClientExcelNewsDTO info);

	/**
	 * 清空导入的客资列表
	 *
	 * @param companyId
	 * @param operaId
	 */
	void deleteTempByStaffId(int companyId, int operaId);

	/**
	 * 导出客资
	 * 
	 * @param staffPO
	 * @param clientExportDTO
	 * @return
	 */
	List<ClientExportVO> Export(StaffPO staffPO, ClientExportDTO clientExportDTO);

	/**
	 * 获取分类客资个数
	 * @param staffPO
	 * @return
	 * */
	ClientSortCountDTO getMultipleKzStatusCount(StaffPO staffPO);

	/**
	 * 根据类型获取上传的客资
	 * @param currentLoginStaff
	 * @param type
	 * @param page
	 * @param pageSize
	 * @return
	 * */
	PageInfo getUploadRecordByType(StaffPO currentLoginStaff, Integer type,Integer page,Integer pageSize);
}
