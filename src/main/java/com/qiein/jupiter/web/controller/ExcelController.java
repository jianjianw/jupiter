package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.ClientExcelDTO;
import com.qiein.jupiter.web.entity.dto.ClientExportDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientExportVO;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.service.ExcelService;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.UCDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.undo.CannotUndoException;

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
    private StaffService staffService;

    /**
     * 客资导入
     *
     * @param file
     * @return
     */
    @PostMapping("/import_kz_excel")
    public ResultInfo importKzExcelLp(@RequestParam("file") MultipartFile file) throws Exception {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        excelService.importExcel(file, currentLoginStaff);
        return ResultInfoUtil.success(TigMsgEnum.IMPORT_SUCCESS);
    }

    /**
     * 获取上传客资的所有列表
     *
     * @return
     */
    @GetMapping("/get_all_upload_record")
    public ResultInfo getAllUploadRecord() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(excelService.getAllUploadRecord(currentLoginStaff.getCompanyId(), currentLoginStaff.getId()));
    }

    /**
     * 保存导入客资
     *
     * @return
     */
    @GetMapping("/save_excel_kz")
    public ResultInfo saveExcelKz() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        excelService.tempKzMoveToInfo(currentLoginStaff.getCompanyId(), currentLoginStaff.getId());
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
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
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        excelService.batchDeleteTemp(currentLoginStaff.getCompanyId(), currentLoginStaff.getId(), kzIds);
        return ResultInfoUtil.success(TigMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 批量编辑客资
     *
     * @return
     */
    @PostMapping("/batch_edit_temp")
    public ResultInfo batchEditTemp(@RequestBody ClientExcelDTO info) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        info.setOperaId(currentLoginStaff.getId());
        excelService.editKz(currentLoginStaff.getCompanyId(), info);
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 清空导入的客资列表
     *
     * @return
     */
    @GetMapping("/delete_temp_by_operaid")
    public ResultInfo deleteTempByOperaId() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        excelService.deleteTempByStaffId(currentLoginStaff.getCompanyId(), currentLoginStaff.getId());
        return ResultInfoUtil.success(TigMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 导出客资
     */
    @PostMapping("/export_client_list")
    public void exportClientList(HttpServletRequest request, HttpServletResponse response, @RequestBody ClientExportDTO clientExportDTO) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientExportDTO.setCompanyId(currentLoginStaff.getCompanyId());
        clientExportDTO.setUid(HttpUtil.getRequestParam(request, CommonConstant.UID));
        clientExportDTO.setSig(HttpUtil.getRequestParam(request, CommonConstant.TOKEN));
        try {
            String fileName = TimeUtil.intMillisToTimeStr(Integer.parseInt(clientExportDTO.getStart()), TimeUtil.ymdSDF_) + "--"
                    + TimeUtil.intMillisToTimeStr(Integer.parseInt(clientExportDTO.getEnd()), TimeUtil.ymdSDF_) + "客资（" + currentLoginStaff.getNickName() + "）";
            ExportExcelUtil.export(response, fileName,
                    excelService.Export(currentLoginStaff, clientExportDTO), ClientExportVO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
