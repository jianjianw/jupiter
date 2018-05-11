package com.qiein.jupiter.web.controller;


import com.qiein.jupiter.util.ExportExcelUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.StaffService;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/export")
@Controller
public class ExportController {

    @Autowired
    private StaffService staffService;

    /**
     * 导出示例
     */
    @GetMapping("/staff")
    public void exportStaff(HttpServletResponse response) {
        try {
            ExportExcelUtil.export(response, "员工信息",
                    staffService.exportStaff(), StaffPO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
