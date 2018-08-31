package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.mzlion.easyokhttp.HttpClient;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.AdminLogPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.AdminShowVO;
import com.qiein.jupiter.web.entity.vo.AdminVO;
import com.qiein.jupiter.web.entity.vo.MsgTemplateVO;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 安全中心Controller
 */
@RestController
@RequestMapping("/admin")
@Validated
@PropertySource({"classpath:application-dev.properties"})
public class AdminController extends BaseController{
    @Autowired
    private StaffService staffService;
    //apollo获取操作日志接口
    @Value("${apollo.getAdminList}")
    private String getAdminListUrl;
    @Value("${apollo.deleteLog}")
    private String deteleLogUrl;
    /**
     * 操作日志页面
     * @return
     */
    @GetMapping("/get_admin_log")
    public ResultInfo getAdminList(@RequestParam String time){
        String adminLog = HttpClient
                .get(getAdminListUrl)
                .queryString("companyId", getCurrentLoginStaff().getCompanyId())
                .queryString("time",time)
                .asString();
        JSONObject json = JSONObject.parseObject(adminLog);
        AdminVO adminVO = JSONObject.parseObject(json.getString("data"), AdminVO.class);
        for(AdminLogPO adminLogPO:adminVO.getLogList()){
            StaffPO staff=staffService.getById(adminLogPO.getStaffId(),getCurrentLoginStaff().getCompanyId());
            adminLogPO.setStaffName(staff.getNickName());
        }
        return ResultInfoUtil.success(adminVO.getLogList());
    }

    /**
     * 删除操作日志
     * @param id
     * @return
     */
    @GetMapping("/delete_admin")
    public ResultInfo deleteAdmin(@RequestParam Integer id){
        String delete = HttpClient
                .get(deteleLogUrl)
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
