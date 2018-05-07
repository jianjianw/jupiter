package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.dto.ClientExcelDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 导入导出
 */
@RestController
@RequestMapping("/excel")
@Validated
public class ExcelController extends BaseController {

    @Autowired
    private ExcelService excelService;

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
        return ResultInfoUtil.success(TipMsgConstant.IMPORT_SUCCESS);
    }
}
