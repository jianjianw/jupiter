package com.qiein.jupiter.web.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.ClientExcelNewsDTO;
import com.qiein.jupiter.web.entity.dto.ClientSortCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.web.entity.dto.ClientExportDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientExportVO;
import com.qiein.jupiter.web.entity.vo.CompanyVO;
import com.qiein.jupiter.web.service.ChannelService;
import com.qiein.jupiter.web.service.DictionaryService;
import com.qiein.jupiter.web.service.ExcelService;
import com.qiein.jupiter.web.service.SourceService;
import com.qiein.jupiter.web.service.StatusService;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * 表格
 */
@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private ExcelDao excelDao;
    @Autowired
    private CrmBaseApi crmBaseApi;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private DictionaryDao dictionaryDao;
    @Autowired
    private SourceDao sourceDao;

    /**
     * 导入客资
     *
     * @param file
     * @param currentLoginStaff
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file, StaffPO currentLoginStaff) throws Exception {
        ImportParams params = new ImportParams();
        // 表格标题行数,默认0
        params.setTitleRows(0);
        // 表头行数,默认1
        params.setHeadRows(1);
        List<ClientExcelNewsDTO> clientList = ExcelImportUtil.importExcel(file.getInputStream(), ClientExcelNewsDTO.class,
                params);
        if (CollectionUtils.isEmpty(clientList)) {
            throw new RException(ExceptionEnum.EXCEL_IS_NULL);
        }
        for (ClientExcelNewsDTO clientExcelDTO : clientList) {
            ObjectUtil.objectStrParamTrim(clientExcelDTO);
            //此处进行特殊换行符处理
            //TODO 封装到工具类中
            String kzPhone = StringUtil.nullToStrTrim(clientExcelDTO.getKzPhone()).replace("/r", "").replace("/n", "");
            clientExcelDTO.setStatusName(clientExcelDTO.getClassName());
            clientExcelDTO.setKzPhone(StringUtil.isEmpty(kzPhone) ? null : kzPhone);
            clientExcelDTO.setRemark(StringUtil.isEmpty(clientExcelDTO.getRemark())
                    ? CommonConstant.EXCEL_DEFAULT_REMARK
                    : CommonConstant.RICH_TEXT_PREFIX + clientExcelDTO.getRemark() + CommonConstant.RICH_TEXT_SUFFIX);
            clientExcelDTO.setCompanyId(currentLoginStaff.getCompanyId());
            clientExcelDTO.setKzId(StringUtil.getRandom());
            clientExcelDTO.setOperaId(currentLoginStaff.getId());
            clientExcelDTO.setTypeName(CommonConstant.EXCEL_DEFAULT_PHOTO_TYPE_NAME);
            clientExcelDTO.setCreateTime(clientExcelDTO.getTime() == null ? 0 : clientExcelDTO.getTime().getTime() / 1000);
            clientExcelDTO.setSuccessTime(clientExcelDTO.getSuccessTimeDate() == null ? 0 : clientExcelDTO.getSuccessTimeDate().getTime() / 1000);
            clientExcelDTO.setAppointTime(clientExcelDTO.getAppointTimeDate() == null ? 0 : clientExcelDTO.getAppointTimeDate().getTime() / 1000);
            clientExcelDTO.setComeShopTime(clientExcelDTO.getComeShopTimeDate() == null ? 0 : clientExcelDTO.getComeShopTimeDate().getTime() / 1000);
            clientExcelDTO.setMarryTime(CommonConstant.DEFAULT_ZERO);
            clientExcelDTO.setYpTime(CommonConstant.DEFAULT_ZERO);
            clientExcelDTO.setYxLevel(CommonConstant.DEFAULT_ZERO);
            clientExcelDTO.setYsRange(CommonConstant.DEFAULT_ZERO);
            clientExcelDTO.setZxStyle(CommonConstant.DEFAULT_ZERO);
            try {
                if (StringUtil.isNotEmpty(clientExcelDTO.getAmountStr())) {
                    clientExcelDTO.setAmount(Integer.valueOf(clientExcelDTO.getAmountStr()));
                }
                if (StringUtil.isNotEmpty(clientExcelDTO.getStayaMountStr())) {
                    clientExcelDTO.setStayaMount(Integer.valueOf(clientExcelDTO.getStayaMountStr()));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
        // 1.删除员工客资缓存记录
        excelDao.deleteTempByStaffId(currentLoginStaff.getId());

        /*-- 新增客资信息 --*/
        int back = excelDao.insertExcelClientInfo(clientList);
        if (back != clientList.size()) {
            /*-- 清空缓存表 --*/
            excelDao.truncateTempTable();
            throw new RException(ExceptionEnum.EXCEL_ADD_FAIL);
        }

        // 设置企业ID
        excelDao.updateCompanyId(currentLoginStaff.getCompanyId(), currentLoginStaff.getId());

        // 没有客资ID的，设置UUID
        excelDao.updateKzid(currentLoginStaff.getId());

        // 设置咨询类型ID
        excelDao.updateType(currentLoginStaff.getId());

        //设置状态id
        excelDao.updateStatusIdAndClassId(currentLoginStaff.getId(), currentLoginStaff.getCompanyId());

        // 设置来源ID,和来源类型
        excelDao.updateSrcIdAndType(currentLoginStaff.getId(), currentLoginStaff.getCompanyId());


        // 更新最后跟进时间为当前系统时间
        excelDao.updateUpdateTime(currentLoginStaff.getId());

        // 更新提报人ID
        excelDao.updateCollectorId(currentLoginStaff.getId());

        // 更新提报人id是空的记录
        excelDao.updateEmptyCollector(currentLoginStaff.getId());

        // 更新邀约员ID
        excelDao.updateAppointId(currentLoginStaff.getId());

        // 更新邀约客服id是空的记录
        excelDao.updateEmptyAppoint(currentLoginStaff.getId());

        // 更新门店ID
        excelDao.updateShopId(currentLoginStaff.getId(), currentLoginStaff.getCompanyId());

        // 更新邀约小组ID
        excelDao.updateGroupId(currentLoginStaff.getId(), currentLoginStaff.getCompanyId());

        // 更新门市ID
        excelDao.updateReceptorId(currentLoginStaff.getId(), currentLoginStaff.getCompanyId());

        //设置来源和渠道
        excelDao.updateSrcAndChannel(currentLoginStaff.getId(), currentLoginStaff.getCompanyId());

        //设置意向等级，预算范围，结婚时间，预拍时间的Code
        excelDao.updateZxStyleDictionaryCode(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(), DictionaryConstant.ZX_STYLE);
        excelDao.updateYxLevelDictionaryCode(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(), DictionaryConstant.YX_RANK);
        excelDao.updateYsRangeDictionaryCode(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(), DictionaryConstant.YS_RANGE);
        excelDao.updateYpTimeDictionaryCode(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(), DictionaryConstant.YP_TIME);
        excelDao.updateMarryTimeDictionaryCode(currentLoginStaff.getId(), currentLoginStaff.getCompanyId(), DictionaryConstant.MARRY_TIME);
    }


    /**
     * 获取错误记录
     **/
    public HashMap<String, List<ClientExcelNewsDTO>> getAllUploadRecord(int companyId, int staffId) {
        HashMap<String, List<ClientExcelNewsDTO>> map = new HashMap<>();
        List<ClientExcelNewsDTO> wrongs = new LinkedList<>();
        List<ClientExcelNewsDTO> rights = new LinkedList<>();
        List<ClientExcelNewsDTO> all = excelDao.getAllRecordByStaffId(staffId);
        List<ClientExcelNewsDTO> dbRepeats = excelDao.getRepeatRecord(staffId);
        String repeatIds = "";
        if (CollectionUtils.isNotEmpty(dbRepeats)) {
            for (ClientExcelNewsDTO info : dbRepeats) {
                //1.时间格式化
                info.setComeShopTimeStr(TimeUtil.intMillisToTimeStr(Integer.valueOf(String.valueOf(info.getComeShopTime()))));
                info.setSuccessTimeStr(TimeUtil.intMillisToTimeStr(Integer.valueOf(String.valueOf(info.getSuccessTime()))));
                info.setAppointTimeStr(TimeUtil.intMillisToTimeStr(Integer.valueOf(String.valueOf(info.getAppointTime()))));
                repeatIds += info.getKzId() + CommonConstant.STR_SEPARATOR;
            }
        }
        List<ClientExcelNewsDTO> excelEepeats = excelDao.getExcelRepeatRecord(staffId);
        if (CollectionUtils.isNotEmpty(excelEepeats)) {
            dbRepeats.addAll(excelEepeats);
            for (ClientExcelNewsDTO info : excelEepeats) {
                repeatIds += info.getKzId() + CommonConstant.STR_SEPARATOR;
            }
        }

        for (ClientExcelNewsDTO info : all) {
            // 1.格式化时间
            info.setComeShopTimeStr(TimeUtil.intMillisToTimeStr(Integer.valueOf(String.valueOf(info.getComeShopTime()))));
            info.setSuccessTimeStr(TimeUtil.intMillisToTimeStr(Integer.valueOf(String.valueOf(info.getSuccessTime()))));
            info.setAppointTimeStr(TimeUtil.intMillisToTimeStr(Integer.valueOf(String.valueOf(info.getAppointTime()))));
            // 2.查询重复记录
            if (repeatIds.contains(info.getKzId())) {
                continue;
            }
            // 3.查询格式错误
            if (info.checkWrongInfo()) {
                wrongs.add(info);
            } else {
                rights.add(info);
            }
        }

        map.put("wrongs", wrongs);
        map.put("rights", rights);
        map.put("repeats", dbRepeats);
        return map;
    }


    /**
     * 缓存表客资转移到info表
     *
     * @param companyId
     * @param staffId   gaoxiaoli
     */
    @Transactional(rollbackFor = Exception.class)
    public void tempKzMoveToInfo(int companyId, int staffId) {
        //TODO 程序来做校验 去除重复客资
        //添加客资基本表
        excelDao.insertBaseInfoByStaffId(staffId);
        //添加客资详情表
        excelDao.insertDetailInfoByStaffId(staffId);
        //添加备注
        excelDao.addExcelKzRemark(staffId);
        // 删除缓存表记录
        excelDao.deleteTempByStaffId(staffId);
    }

    /**
     * 批量删除客资缓存记录
     *
     * @param companyId
     * @param operaId
     * @param kzIds
     */
    public void batchDeleteTemp(int companyId, int operaId, String kzIds) {
        String[] kzIdArr = kzIds.split(CommonConstant.STR_SEPARATOR);
        excelDao.batchDeleteTemp(kzIdArr, operaId);
    }

    /**
     * 编辑客资缓存记录
     *
     * @param companyId
     * @param info
     */
    @Transactional(rollbackFor = Exception.class)
    public void editKz(int companyId, ClientExcelNewsDTO info) {
        String[] kzIdArr = info.getKzIds().split(CommonConstant.STR_SEPARATOR);
        excelDao.batchEditTemp(kzIdArr, info);
        if (StringUtil.isNotEmpty(info.getTypeName())) {
            // 设置咨询类型ID
            excelDao.updateType(info.getOperaId());// 导入后执行脚本补全数据
        }
        if (StringUtil.isNotEmpty(info.getSourceName())) {
            //设置来源ID，来源类型,渠道ID,渠道名称
            excelDao.updateSrcAndChannel(info.getOperaId(), companyId);
        }
        if (StringUtil.isNotEmpty(info.getStatusName())) {
            // 设置状态ID和classId
            excelDao.updateStatusIdAndClassId(info.getOperaId(), companyId);
        }
        if (StringUtil.isNotEmpty(info.getShopName())) {
            // 更新门店ID
            excelDao.updateShopId(info.getOperaId(), companyId);
        }
        if (StringUtil.isNotEmpty(info.getCollectorName())) {
            // 更新提报人ID
            excelDao.updateCollectorId(info.getOperaId());
        }
        if (StringUtil.isNotEmpty(info.getAppointName())) {
            // 更新邀约员ID
            excelDao.updateAppointId(info.getOperaId());
        }
        if (StringUtil.isNotEmpty(info.getYsRangeStr())) {
            //更新预算范围
            excelDao.updateYsRangeDictionaryCode(info.getOperaId(), companyId, DictionaryConstant.YS_RANGE);
        }
        if (StringUtil.isNotEmpty(info.getYpTimeStr())) {
            //更新预拍时间
            excelDao.updateYpTimeDictionaryCode(info.getOperaId(), companyId, DictionaryConstant.YP_TIME);
        }
        if (StringUtil.isNotEmpty(info.getMarryTimeStr())) {
            //更新结婚时间
            excelDao.updateMarryTimeDictionaryCode(info.getOperaId(), companyId, DictionaryConstant.MARRY_TIME);
        }
        if (StringUtil.isNotEmpty(info.getYxLevelStr())) {
            //更新意向等级
            excelDao.updateYxLevelDictionaryCode(info.getOperaId(), companyId, DictionaryConstant.YX_RANK);
        }
        if (StringUtil.isNotEmpty(info.getZxStyleStr())) {
            //更新咨询方式
            excelDao.updateZxStyleDictionaryCode(info.getOperaId(), companyId, DictionaryConstant.ZX_STYLE);
        }
    }

    /**
     * 清空导入的客资列表
     *
     * @param companyId
     * @param operaId
     */
    public void deleteTempByStaffId(int companyId, int operaId) {
        excelDao.deleteTempByStaffId(operaId);
    }

    /**
     * 导出客资
     *
     * @param staffPO
     * @param clientExportDTO
     * @return
     */
    public List<ClientExportVO> Export(StaffPO staffPO, ClientExportDTO clientExportDTO) {
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("cid", staffPO.getCompanyId());
        reqContent.put("uid", clientExportDTO.getUid());
        reqContent.put("sig", clientExportDTO.getSig());
        reqContent.put("action", clientExportDTO.getAction());
        reqContent.put("role", clientExportDTO.getRole());
        reqContent.put("timetype", clientExportDTO.getTimeType());
        reqContent.put("start", clientExportDTO.getStart());
        reqContent.put("end", clientExportDTO.getEnd());
        reqContent.put("channelid", clientExportDTO.getChannelId());
        reqContent.put("sourceid", clientExportDTO.getSourceId());
        reqContent.put("shopid", clientExportDTO.getShopId());
        reqContent.put("staffid", clientExportDTO.getStaffId());
        reqContent.put("typeid", clientExportDTO.getTypeId());
        reqContent.put("yxlevel", clientExportDTO.getYxLevel());
        reqContent.put("appointids", clientExportDTO.getAppointIds());
        reqContent.put("pmslimit", clientExportDTO.getPmsLimit());
        reqContent.put("linklimit", clientExportDTO.getLinkLimit());
        reqContent.put("sparesql", clientExportDTO.getSpareSql());
        reqContent.put("filtersql", clientExportDTO.getFilterSql());
        reqContent.put("supersql", clientExportDTO.getSuperSql());
        CompanyVO companyVO = companyDao.getVOById(staffPO.getCompanyId());
        String addRstStr = crmBaseApi.doService(reqContent, "excel_export_lp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            JSONArray jsArr = JsonFmtUtil.strContentToJsonObj(addRstStr).getJSONArray("infoList");
            List<ClientExportVO> clientList = JsonFmtUtil.jsonArrToClientExportVO(jsArr, staffPO,
                    sourceService, statusService, channelService, dictionaryService, companyVO, permissionDao);
            return clientList;
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }

    @Override
    public ClientSortCountDTO getMultipleKzStatusCount(StaffPO staffPO) {
        //错误个数
        ClientSortCountDTO clientSortCount = excelDao.getMultipleKzStatusCount(staffPO.getId());
        return clientSortCount;
    }

    @Override
    public PageInfo getUploadRecordByType(StaffPO staffPO, Integer type, Integer page, Integer pageSize) {
        List<ClientExcelNewsDTO> clientExcelNewsDTOS = null;
        switch (type) {
            case 1:
                //错误客资
                PageHelper.startPage(page, pageSize);
                clientExcelNewsDTOS = excelDao.getExcelErrorClient(staffPO.getId());
                break;
            case 2:
                //正常客资
                PageHelper.startPage(page, pageSize);
                clientExcelNewsDTOS = excelDao.getExcelSuccessClient(staffPO.getId());
                break;
            case 3:
                //重复客资
                PageHelper.startPage(page, pageSize);
                clientExcelNewsDTOS = excelDao.getRepeatRecord(staffPO.getId());
                break;
            default:
                break;
        }
        for (ClientExcelNewsDTO info : clientExcelNewsDTOS) {
            //1.时间格式化
            info.setComeShopTimeStr(TimeUtil.intMillisToTimeStr(Integer.valueOf(String.valueOf(info.getComeShopTime()))));
            info.setSuccessTimeStr(TimeUtil.intMillisToTimeStr(Integer.valueOf(String.valueOf(info.getSuccessTime()))));
            info.setAppointTimeStr(TimeUtil.intMillisToTimeStr(Integer.valueOf(String.valueOf(info.getAppointTime()))));
        }

        return new PageInfo<>(clientExcelNewsDTOS);
    }

}
