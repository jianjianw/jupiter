package com.qiein.jupiter.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.ClientExcelNewsDTO;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.entity.vo.QueryVO;
import com.qiein.jupiter.web.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.entity.dto.ClientExportDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientExportVO;
import com.qiein.jupiter.web.service.ExcelService;

import java.util.HashMap;

/**
 * 导入导出
 */
@RestController
@RequestMapping("/excel")
@Validated
public class ExcelController extends BaseController {

    @Autowired
    private ExcelService excelService;
    @Autowired
    private SystemLogService logService;

    /**
     * 客资导入
     *
     * @param file
     * @return
     */
    @PostMapping("/import_kz_excel")
    public ResultInfo importKzExcelLp(@RequestParam("file") MultipartFile file) throws Exception {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        excelService.importExcel(file, currentLoginStaff);
        try {
            RequestInfoDTO requestInfo = getRequestInfo();
            // 日志记录
            SystemLog log = new SystemLog(SysLogUtil.LOG_TYPE_CLIENT, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), SysLogUtil.getImportLog(null),
                    currentLoginStaff.getCompanyId());
            logService.addLog(log);
        } catch (Exception e) {

        }
        return ResultInfoUtil.success(TipMsgEnum.IMPORT_SUCCESS);
    }

    /**
     * 获取上传客资的所有列表
     *
     * @return
     */
    @GetMapping("/get_all_upload_record")
    public ResultInfo getAllUploadRecord(Integer type) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil
                .success(excelService.getAllUploadRecord(currentLoginStaff.getCompanyId(), currentLoginStaff.getId()));
    }


    /**
     * 保存导入客资
     *
     * @return
     */
    @GetMapping("/save_excel_kz")
    public ResultInfo saveExcelKz() {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        excelService.tempKzMoveToInfo(currentLoginStaff.getCompanyId(), currentLoginStaff.getId(), currentLoginStaff.getNickName());
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 批量删除客资缓存记录
     *
     * @return
     */
    @PostMapping("/batch_delete_temp")
    public ResultInfo batchDeleteTemp(@RequestBody JSONObject jsonObject) {
        String kzIds = StringUtil.nullToStrTrim(jsonObject.getString("kzIds"));
        if (StringUtil.isEmpty(kzIds)) {
            throw new RException(ExceptionEnum.KZ_ID_IS_NULL);
        }
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        excelService.batchDeleteTemp(currentLoginStaff.getCompanyId(), currentLoginStaff.getId(), kzIds);
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 批量编辑客资
     *
     * @return
     */
    @PostMapping("/batch_edit_temp")
    public ResultInfo batchEditTemp(@RequestBody ClientExcelNewsDTO info) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        info.setOperaId(currentLoginStaff.getId());
        excelService.editKz(currentLoginStaff.getCompanyId(), info);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 清空导入的客资列表
     *
     * @return
     */
    @GetMapping("/delete_temp_by_operaid")
    public ResultInfo deleteTempByOperaId() {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        excelService.deleteTempByStaffId(currentLoginStaff.getCompanyId(), currentLoginStaff.getId());
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 导出客资
     */
//    @PostMapping("/export_client_list")
//    public void exportClientList(HttpServletRequest request, HttpServletResponse response,
//                                 @RequestBody ClientExportDTO clientExportDTO) {
//        // 获取当前登录账户
//        StaffPO currentLoginStaff = getCurrentLoginStaff();
//        clientExportDTO.setCompanyId(currentLoginStaff.getCompanyId());
//        clientExportDTO.setUid(HttpUtil.getRequestParam(request, CommonConstant.UID));
//        clientExportDTO.setSig(HttpUtil.getRequestParam(request, CommonConstant.TOKEN));
//        try {
//            String fileName = TimeUtil.intMillisToTimeStr(Integer.parseInt(clientExportDTO.getStart()),
//                    TimeUtil.ymdSDF_) + "--"
//                    + TimeUtil.intMillisToTimeStr(Integer.parseInt(clientExportDTO.getEnd()), TimeUtil.ymdSDF_) + "客资（"
//                    + currentLoginStaff.getNickName() + "）";
//            ExportExcelUtil.export(response, fileName, excelService.export(currentLoginStaff, clientExportDTO),
//                    ClientExportVO.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //添加导出日志
//        try {
//            RequestInfoDTO requestInfo = getRequestInfo();
//            HashMap<String, String> map = new HashMap<>();
//            if ("1".equals(clientExportDTO.getTimeType())) {
//                map.put("录入时间", TimeUtil.intMillisToTimeStr(Integer.parseInt(clientExportDTO.getStart()), TimeUtil.ymdSDFLeft) + "-" +
//                        TimeUtil.intMillisToTimeStr(Integer.parseInt(clientExportDTO.getEnd()), TimeUtil.ymdSDFLeft));
//            }
//            // 日志记录
//            SystemLog log = new SystemLog(SysLogUtil.LOG_TYPE_CLIENT, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
//                    currentLoginStaff.getNickName(), SysLogUtil.getExportLog(map), currentLoginStaff.getCompanyId());
//            logService.addLog(log);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    @PostMapping("/export_client_list")
    public void exportClientList(HttpServletResponse response,
                                 @RequestBody JSONObject content) {
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();

        QueryVO queryVO = PlatController.initQueryVo(currentLoginStaff.getCompanyId(), currentLoginStaff.getId(), content);
        queryVO.setClassId(ClientStatusConst.getClassByAction(queryVO.getAction()));

        try {
            String fileName = TimeUtil.intMillisToTimeStr(queryVO.getStart(),
                    TimeUtil.ymdSDF_) + "--"
                    + TimeUtil.intMillisToTimeStr(queryVO.getEnd(), TimeUtil.ymdSDF_) + "客资（"
                    + currentLoginStaff.getNickName() + "）";
            ExportExcelUtil.export(response, fileName, excelService.export(currentLoginStaff, queryVO),
                    ClientExportVO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //添加导出日志
        try {
            RequestInfoDTO requestInfo = getRequestInfo();
            HashMap<String, String> map = new HashMap<>();
            if ("1".equals(queryVO.getTimeType())) {
                map.put("录入时间", TimeUtil.intMillisToTimeStr(queryVO.getStart(), TimeUtil.ymdSDFLeft) + "-" +
                        TimeUtil.intMillisToTimeStr(queryVO.getEnd(), TimeUtil.ymdSDFLeft));
            }
            // 日志记录
            SystemLog log = new SystemLog(SysLogUtil.LOG_TYPE_CLIENT, requestInfo.getIp(), requestInfo.getUrl(), currentLoginStaff.getId(),
                    currentLoginStaff.getNickName(), SysLogUtil.getExportLog(map), currentLoginStaff.getCompanyId());
            logService.addLog(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计客制状况及个数
     */
    @GetMapping("/get_multiple_status_kz_count")
    public ResultInfo getMultipleKzStatusCount() {
        return ResultInfoUtil.success(excelService.getMultipleKzStatusCount(getCurrentLoginStaff()));
    }

    /**
     * 根据type获取客资列表
     */
    @GetMapping("/get_upload_record_by_type")
    public ResultInfo getUploadRecordByType(Integer type, Integer page, Integer pageSize) {
        if (NumUtil.isInValid(page) || NumUtil.isInValid(pageSize)) {
            page = 1;
            pageSize = 20;
        }
        if (pageSize > CommonConstant.MAX_PAGE_SIZE) {
            return ResultInfoUtil.error(ExceptionEnum.PAGESIZE_MAX_SIZE_ERROR);
        }
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(excelService.getUploadRecordByType(currentLoginStaff, type, page, pageSize));
    }


}
