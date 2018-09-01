package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.constant.AppolloUrlConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.MD5Util;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.AdminLogPO;
import com.qiein.jupiter.web.entity.po.SitePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.AdminShowVO;
import com.qiein.jupiter.web.entity.vo.AdminVO;
import com.qiein.jupiter.web.entity.vo.MsgTemplateVO;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 安全中心Controller
 */
@RestController
@RequestMapping("/safe_center")
@Validated
@PropertySource({"classpath:application-dev.properties"})
public class SafeCenterController extends BaseController{
    @Autowired
    private StaffService staffService;
    @Value("${apollo.baseUrl}")
    private String appoloBaseUrl;
    /**
     * 操作日志页面
     * @return
     */
    @GetMapping("/get_admin_log")
    public ResultInfo getAdminList(@RequestParam String time,@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        String adminLog = HttpClient
                .get(appoloBaseUrl.concat(AppolloUrlConst.GET_ADMIN_LOG))
                .queryString("companyId", getCurrentLoginStaff().getCompanyId())
                .queryString("time",time)
                .queryString("pageNum",pageNum)
                .queryString("pageSize",pageSize)
                .asString();
        JSONObject json = JSONObject.parseObject(adminLog);
        AdminVO adminVO = JSONObject.parseObject(json.getString("data"), AdminVO.class);
        for(AdminLogPO adminLogPO:adminVO.getLogList()){
            StaffPO staff=staffService.getById(adminLogPO.getStaffId(),getCurrentLoginStaff().getCompanyId());
            adminLogPO.setStaffName(staff.getNickName());
        }
        PageHelper.startPage(pageNum, pageSize);
        return ResultInfoUtil.success(new PageInfo<>(adminVO.getLogList()));
    }

    /**
     * 删除操作日志
     * @param id
     * @return
     */
    @GetMapping("/delete_admin_log")
    public ResultInfo deleteAdminLog(@RequestParam Integer id){
        String delete = HttpClient
                .get(appoloBaseUrl.concat(AppolloUrlConst.DELETE_ADMIN_LOG))
                .queryString("id", id)
                .asString();
        JSONObject getBack = JSONObject.parseObject(delete);
        Integer code = (Integer) getBack.get("code");
        if (code != 100000) {
            throw new RException((String) getBack.get("msg"));
        }
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }

    /**
     * 添加场地
     * */
    @PostMapping("/add_site")
    public ResultInfo addSite(@RequestBody SitePO sitePO){
        String sign = MD5Util.getApolloMd5(JSONObject.toJSONString(sitePO));
        sitePO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        String result = HttpClient.textBody(appoloBaseUrl.concat(AppolloUrlConst.ADD_SITE))
                .json(sitePO)
                .queryString("sign", sign)
                .asString();
        return ResultInfoUtil.error(JSONObject.parseObject(result).getIntValue("code"),JSONObject.parseObject(result).getString("msg"));
    }

    /**
     * 编辑场地
     * */
    @PostMapping("/edit_site")
    public ResultInfo editSite(@RequestBody SitePO sitePO){
        String sign = MD5Util.getApolloMd5(JSONObject.toJSONString(sitePO));
        sitePO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        String result = HttpClient.textBody(appoloBaseUrl.concat(AppolloUrlConst.EDIT_SITE))
                .json(sitePO)
                .queryString("sign", sign)
                .asString();
        return ResultInfoUtil.error(JSONObject.parseObject(result).getIntValue("code"),JSONObject.parseObject(result).getString("msg"));
    }

    /**
     * 删除场地
     * */
    @PostMapping("/del_site")
    public ResultInfo delSite(@RequestBody SitePO sitePO){
        String sign = MD5Util.getApolloMd5(JSONObject.toJSONString(sitePO));
        sitePO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        String result = HttpClient.textBody(appoloBaseUrl.concat(AppolloUrlConst.DEL_SITE))
                .json(sitePO)
                .queryString("sign", sign)
                .asString();
        System.out.println(result);
        return ResultInfoUtil.error(JSONObject.parseObject(result).getIntValue("code"),JSONObject.parseObject(result).getString("msg"));
    }

    /**
     * 场地列表
     * */
    @GetMapping("/site_list")
    public ResultInfo siteList(){
        int companyId = getCurrentLoginStaff().getCompanyId();
        String result = HttpClient.get(appoloBaseUrl.concat(AppolloUrlConst.SITE_LIST)).queryString("companyId",companyId).asString();
        return ResultInfoUtil.success(JSONObject.parseObject(result).get("data"));
    }


    /**
     * 管理中心页面
     */
    @GetMapping("/get_admin_list")
    public ResultInfo getList(){
        String adminLog = HttpClient
                .get(appoloBaseUrl.concat(AppolloUrlConst.GET_ADMIN_LIST))
                .queryString("companyId", getCurrentLoginStaff().getCompanyId())
                .asString();
        JSONObject json = JSONObject.parseObject(adminLog);
        AdminVO adminVO = JSONObject.parseObject(json.getString("data"), AdminVO.class);
        for(AdminLogPO adminLogPO:adminVO.getLogList()){
            StaffPO staff=staffService.getById(adminLogPO.getStaffId(),getCurrentLoginStaff().getCompanyId());
            adminLogPO.setStaffName(staff.getNickName());
        }
        for(AdminShowVO adminShowVO:adminVO.getList()){
            StaffPO staff=staffService.getById(adminShowVO.getStaffId(),getCurrentLoginStaff().getCompanyId());
            adminShowVO.setStaffName(staff.getNickName());
        }
        return ResultInfoUtil.success(adminVO);
    }
    /**
     * 删除设备
     */
    @GetMapping("/delete_computer")
    public ResultInfo deleteComputer(@RequestParam Integer id){
        String delete = HttpClient
                .get(appoloBaseUrl.concat(AppolloUrlConst.DELETE_COMPUTER))
                .queryString("id", id)
                .asString();
        JSONObject getBack = JSONObject.parseObject(delete);
        Integer code = (Integer) getBack.get("code");
        if (code != 100000) {
            throw new RException((String) getBack.get("msg"));
        }
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }
    /**
     * 删除设备
     */
    @GetMapping("/delete_admin")
    public ResultInfo deleteAdmin(@RequestParam Integer id){
        String delete = HttpClient
                .get(appoloBaseUrl.concat(AppolloUrlConst.DELETE_ADMIN))
                .queryString("id", id)
                .asString();
        JSONObject getBack = JSONObject.parseObject(delete);
        Integer code = (Integer) getBack.get("code");
        if (code != 100000) {
            throw new RException((String) getBack.get("msg"));
        }
        return ResultInfoUtil.success(TipMsgEnum.DELETE_SUCCESS);
    }
}
