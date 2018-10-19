package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.Bool;
import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.dto.ShopTargetDTO;
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

    /**
     * 获取企业所有拍摄地列表，包含停用
     *
     * @return
     */
    @GetMapping("/get_company_shop_list")
    public ResultInfo getCompanyShopList() {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(shopService.getCompanyShopList(currentLoginStaff.getCompanyId()));
    }

    /**
     * 添加拍摄地
     *
     * @param shopPO
     * @return
     */
    @PostMapping("/add_shop")
    public ResultInfo addShop(@RequestBody @Validated ShopPO shopPO) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopPO.setCompanyId(currentLoginStaff.getCompanyId());
        shopService.addShop(shopPO);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 编辑拍摄地
     *
     * @param shopPO
     * @return
     */
    @PostMapping("/edit_shop")
    public ResultInfo editShop(@RequestBody @Validated ShopPO shopPO) {
        if (NumUtil.isNull(shopPO.getId())) {
            return ResultInfoUtil.error(ExceptionEnum.SHOP_ID_NULL);
        }
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopPO.setCompanyId(currentLoginStaff.getCompanyId());
        shopService.editShop(shopPO);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 编辑拍摄地是否启用
     *
     * @param id
     * @param showFlag
     * @return
     */
    @GetMapping("/edit_shop_show")
    public ResultInfo editShopShow(@Id @RequestParam("id") Integer id,
                                   @Bool @RequestParam("showFlag") Boolean showFlag) {
        if (NumUtil.isNull(id)) {
            return ResultInfoUtil.error(ExceptionEnum.SHOP_ID_NULL);
        }
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopService.editShopShowFlag(currentLoginStaff.getCompanyId(), id, showFlag);
        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 校验拍摄地是否可删除
     *
     * @param id
     * @return
     */
    @GetMapping("/shop_can_delete")
    public ResultInfo shopCanDelete(@Id @RequestParam("id") Integer id) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(shopService.shopCanDelete(currentLoginStaff.getCompanyId(), id));
    }

    /**
     * 删除拍摄地
     *
     * @param id
     * @return
     */
    @GetMapping("/delete_shop")
    public ResultInfo deleteShop(@Id @RequestParam("id") Integer id) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        shopService.deleteShop(currentLoginStaff.getCompanyId(), id);
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }


    /**
     * 获取所有启用的拍摄地列表
     *
     * @return
     */
    @GetMapping("/get_show_shop_list")
    public ResultInfo getShowShopList() {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(shopService.getShowShopList(currentLoginStaff.getCompanyId()));
    }

    /**
     * 编辑拍摄地排序，实际是交换两个拍摄地的排序
     *
     * @param fId
     * @param fPriority
     * @param sId
     * @param sPriority
     * @return
     */
    @GetMapping("/priority")
    public ResultInfo editPriority(@Id Integer fId, @Id Integer fPriority,
                                   @Id Integer sId, @Id Integer sPriority) {
        shopService.editPriority(fId, fPriority, sId, sPriority, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }

    /**
     * 获取所在小组承接拍摄地
     *
     * @return
     */
    @GetMapping("/get_my_group_shop_list")
    public ResultInfo getShopListByStaffGroup(String groupId) {
        StaffPO staffPO = getCurrentLoginStaff();
        return ResultInfoUtil.success(shopService.getShopListByStaffGroup(staffPO.getCompanyId(), groupId));
    }

    /**
     * 获取所在小组承接拍摄地
     *
     * @return
     */
    @GetMapping("/get_shop_staff_list")
    public ResultInfo getShopStaffList() {
        StaffPO staffPO = getCurrentLoginStaff();
        return ResultInfoUtil.success(shopService.getShopAndStaffList(staffPO.getCompanyId()));
    }

    /**
     * 修改门店报表 目标
     */
    @GetMapping("/edit_target")
    public ResultInfo editTarget(@RequestBody ShopTargetDTO shopTargetDTO){
        shopTargetDTO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        shopService.editTarget(shopTargetDTO);
        return ResultInfoUtil.success(TipMsgEnum.EDIT_SUCCESS);
    }
}
