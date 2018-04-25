package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/add")
public class ClientAddController extends BaseController {

    @Autowired
    private CrmBaseApi crmBaseApi;

    /**
     * 录入电商客资
     *
     * @return
     */
    @PostMapping("/add_ds_client")
    public ResultInfo addDsClient() {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();



        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }
}
