package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
public class ShopController extends BaseController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/get_company_shop_list")
    public ResultInfo getCompanyShopList() {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(shopService.getCompanyShopList(currentLoginStaff.getCompanyId()));
    }
}
