package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.ExportExcelUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ExcelService;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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
