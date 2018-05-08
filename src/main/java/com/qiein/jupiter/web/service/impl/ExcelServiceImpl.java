package com.qiein.jupiter.web.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.StatusConstant;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ExcelDao;
import com.qiein.jupiter.web.entity.dto.ClientExcelDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ExcelService;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private ExcelDao excelDao;

    @Override
    public void importExcel(MultipartFile file, StaffPO currentLoginStaff) throws Exception {
        ImportParams params = new ImportParams();
        //表格标题行数,默认0
        params.setTitleRows(0);
        //表头行数,默认1
        params.setHeadRows(1);
        List<ClientExcelDTO> clientList = ExcelImportUtil.importExcel(
                file.getInputStream(), ClientExcelDTO.class, params);
        if (CollectionUtils.isEmpty(clientList)) {
            throw new RException(ExceptionEnum.EXCEL_IS_NULL);
        }
        for (ClientExcelDTO clientExcelDTO : clientList) {
            String status = clientExcelDTO.getStatusName();
            clientExcelDTO.setStatusId((StringUtil.isNotEmpty(status) && status.contains("无")) ? StatusConstant.INVALID : StatusConstant.NOT_SET);
            clientExcelDTO.setRemark(StringUtil.isEmpty(clientExcelDTO.getRemark()) ? CommonConstant.EXCEL_DEFAULT_REMARK :
                    CommonConstant.RICH_TEXT_PREFIX + clientExcelDTO.getRemark() + CommonConstant.RICH_TEXT_SUFFIX);
            clientExcelDTO.setCompanyId(currentLoginStaff.getCompanyId());
            clientExcelDTO.setKzId(StringUtil.getRandom());
            clientExcelDTO.setOperaId(currentLoginStaff.getId());
            clientExcelDTO.setTypeName(CommonConstant.EXCEL_DEFAULT_PHOTO_TYPE_NAME);
            clientExcelDTO.setCreateTime(HSSFDateUtil.getJavaDate(clientExcelDTO.getTime()).getTime() / 1000);
        }
        //1.删除员工客资缓存记录
        excelDao.deleteTempByStaffId(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        /*-- 新增客资信息 --*/
        int back = excelDao.insertExcelClientInfo(clientList, DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()));
        if (back != clientList.size()) {
            /*-- 清空缓存表 --*/
            excelDao.truncateTempTable(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()));
            throw new RException(ExceptionEnum.EXCEL_ADD_FAIL);
        }

        // 设置企业ID
        excelDao.updateCompanyId(currentLoginStaff.getCompanyId(), DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()),
                currentLoginStaff.getId());

        // 没有客资ID的，设置UUID
        excelDao.updateKzid(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        // 设置咨询类型ID
        excelDao.updateType(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        // 设置状态名称
        excelDao.updateStatusName(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        // 设置客资当前分类ID
        excelDao.updateClassId(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        // 设置渠道ID
        excelDao.updateSrcId(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        // 更新最后跟进时间为当前系统时间
        excelDao.updateUpdateTime(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        // 更新提报人ID
        excelDao.updateCollectorId(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        // 更新提报人id是空的记录
        excelDao.updateEmptyCollector(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        // 更新邀约员ID
        excelDao.updateAppointId(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        // 更新邀约客服id是空的记录
        excelDao.updateEmptyAppoint(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        // 更新门店ID
        excelDao.updateShopId(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

        // 更新邀约小组ID
        excelDao.updateGroupId(DBSplitUtil.getTable(TableEnum.temp, currentLoginStaff.getCompanyId()), currentLoginStaff.getId());

    }
}
