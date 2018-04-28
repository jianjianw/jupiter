package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.Bool;
import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.ShopPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
@Validated
public class ShopController extends BaseController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/get_company_shop_list")
    public ResultInfo getCompanyShopList() {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(shopService.getCompanyShopList(currentLoginStaff.getCompanyId()));
    }

    @PostMapping("/add_shop")
    public ResultInfo addShop(@RequestBody @Validated ShopPO shopPO) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopPO.setCompanyId(currentLoginStaff.getCompanyId());
        shopService.addShop(shopPO);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }

    @PostMapping("/edit_shop")
    public ResultInfo editShop(@RequestBody @Validated ShopPO shopPO) {
        if (NumUtil.isNull(shopPO.getId())) {
            return ResultInfoUtil.error(ExceptionEnum.SHOP_ID_NULL);
        }
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopPO.setCompanyId(currentLoginStaff.getCompanyId());
        shopService.editShop(shopPO);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }

    @GetMapping("/edit_shop_show")
    public ResultInfo editShopShow(@Id @RequestParam("id") Integer id,
                                   @Bool @RequestParam("showFlag") Boolean showFlag) {
        if (NumUtil.isNull(id)) {
            return ResultInfoUtil.error(ExceptionEnum.SHOP_ID_NULL);
        }
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopService.editShopShowFlag(currentLoginStaff.getCompanyId(), id, showFlag);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }

    @GetMapping("/delete_shop")
    public ResultInfo deleteShop(@Id @RequestParam("id") Integer id) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopService.deleteShop(currentLoginStaff.getCompanyId(), id);
        return ResultInfoUtil.success(TipMsgConstant.DELETE_SUCCESS);
    }

    @GetMapping("/get_show_shop_list")
    public ResultInfo getShowShopList() {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(shopService.getShowShopList(currentLoginStaff.getCompanyId()));
    }

    /**
     * 编辑拍摄地排序，实际是交换两个拍摄地的排序
     * @param fId
     * @param fPriority
     * @param sId
     * @param sPriority
     * @return
     */
    @GetMapping("/priority")
    public ResultInfo editPriority(@Id Integer fId, @Id Integer fPriority,
                                   @Id Integer sId, @Id Integer sPriority){
        shopService.addShop();
        return ResultInfoUtil.success(TipMsgConstant.EDIT_SUCCESS);
    }

}
