package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.util.ObjectUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.BrandPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 品牌
 * Created by Administrator on 2018/4/24 0024.
 */
@RestController
@RequestMapping("/brand")
@Validated
public class BrandController extends BaseController {

    @Autowired
    private BrandService brandService;

    /**
     * 新增品牌
     *
     * @param brandPO
     * @return
     */
    @RequestMapping("/add")
    public ResultInfo addBrand(@RequestBody @Validated BrandPO brandPO) {

        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //设置cid
        brandPO.setCompanyId(currentLoginStaff.getCompanyId());
        //对象参数去空
        ObjectUtil.objectStrParamTrim(brandPO);
        brandService.createBrand(brandPO);
        return ResultInfoUtil.success(TipMsgConstant.ADD_BRAND_SUCCESS);
    }

    /**
     *  删除品牌
     * @param id
     * @return
     */
    @RequestMapping("/del")
    public ResultInfo delBrand(@Id int id){

        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        brandService.delBrand(id,currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(TipMsgConstant.SUCCESS);
    }

    /**
     * 编辑品牌
     * @param brandPO
     * @return
     */
    @RequestMapping("/edit")
    public ResultInfo editBrand(@RequestBody BrandPO brandPO){
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //设置cid
        brandPO.setCompanyId(currentLoginStaff.getCompanyId());
        //对象参数去空
        ObjectUtil.objectStrParamTrim(brandPO);
        brandService.editBrand(brandPO);

        return ResultInfoUtil.success(TipMsgConstant.EDIT_SUCCESS);
    }

    /**
     * 获取品牌列表
     *
     * @return
     */
    @RequestMapping("/get_list")
    public ResultInfo getBrandList() {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        return ResultInfoUtil.success(TipMsgConstant.SUCCESS,
                brandService.getBrandList(currentLoginStaff.getCompanyId()));
    }
}
