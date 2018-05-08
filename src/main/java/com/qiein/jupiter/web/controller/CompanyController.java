package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.service.CompanyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 公司
 */
@RestController
@RequestMapping("/company")
public class CompanyController extends BaseController {

    @Resource
    private CompanyService companyService;

    /**
     * 编辑企业信息
     * @param companyPO
     * @return
     */
    @PostMapping("/edit")
    public ResultInfo editCompanyInfo (@RequestBody CompanyPO companyPO){
        companyService.update(companyPO);
        return ResultInfoUtil.success(TigMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 获取企业信息
     * @return
     */
    @GetMapping("/info")
    public ResultInfo getCompanyInfo(){
        return ResultInfoUtil.success(TigMsgEnum.SUCCESS,companyService.getById(getCurrentLoginStaff().getCompanyId()));
    }
}
