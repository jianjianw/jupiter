package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.ExportExcelUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.dto.ClientExcelDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ExcelService;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.UCDecoder;

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
    public ResultInfo batchDeleteTemp(@NotEmptyStr @RequestBody String kzIds) {
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
    public ResultInfo batchEditTemp(@NotEmptyStr @RequestBody String kzIds, @RequestBody ClientExcelDTO info) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        excelService.editKz(currentLoginStaff.getCompanyId(), kzIds, info);
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 导出示例
     */
    @GetMapping("/export_staff")
    public void exportStaff(HttpServletResponse response) {
        try {
//            ExportExcelUtil.export(response, "员工信息",
//                    staffService.exportStaff(), StaffPO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
