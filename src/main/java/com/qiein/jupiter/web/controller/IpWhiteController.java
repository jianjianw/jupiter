package com.qiein.jupiter.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.aop.validate.annotation.Id;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.IpWhitePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.CompanyService;
import com.qiein.jupiter.web.service.IpWhiteService;
import com.qiein.jupiter.web.service.StaffService;

/**
 * ip白名单
 *
 * @author XiangLiang 2018/05/16
 **/
@RestController
@RequestMapping("/ipwhite")
@Validated
public class IpWhiteController extends BaseController {

    @Autowired
    private IpWhiteService ipwhiteService;

    @Autowired
    private CompanyService companyService;
    
    @Autowired
    private StaffService staffService;

    /*
     * 新增
     */
    @PostMapping("/insert")
    public ResultInfo insert(@Validated @RequestBody IpWhitePO ipWhitePo) {
        //获取当前登录用户
        StaffPO staff = getCurrentLoginStaff();

        //判断ip 输入是否正确
        String[] ips = ipWhitePo.getIp().split("\\.");
        if (ips.length == 0) {
            ips = ipWhitePo.getIp().split("。");
        }
        if (ips.length != 4) {
            return ResultInfoUtil.error(ExceptionEnum.IP_ERROR);
        }
        for (int i = 0; i < ips.length - 1; i++) {
            int number = Integer.parseInt(ips[i]);
            if (!(number >= 0 && number <= 255)) {
                return ResultInfoUtil.error(ExceptionEnum.IP_ERROR);

            }
        }
        if (!ips[3].equals("*")) {
            return ResultInfoUtil.error(ExceptionEnum.IP_ERROR);
        }
        ipWhitePo.setCreateTime(new Date());
        ipWhitePo.setCompanyId(staff.getCompanyId());
        ipWhitePo.setCreatoerId(staff.getId());
        ipWhitePo.setCreatoerName(staff.getUserName());
        ipwhiteService.insert(ipWhitePo);
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);

    }

    /*
     * 删除
     */
    @GetMapping("/delete")
    public ResultInfo delete(@RequestParam @Id Integer id) {
        ipwhiteService.delete(id);
        return ResultInfoUtil.success(TigMsgEnum.DELETE_SUCCESS);
    }
    /*
     * 修改iplimit
     */

    @GetMapping("/editIpLimit")
    public ResultInfo editIpLimit(@RequestParam Integer iplimit) {
        StaffPO staff = getCurrentLoginStaff();
        if (iplimit == 0) {
            iplimit = 1;
        } else {
            iplimit = 0;
        }
        companyService.editIpLimit(iplimit, staff.getCompanyId());
        return ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
    }

    /*
     * 显示页面
     */
    @GetMapping("/get_all_ip_by_companyId")
    public ResultInfo getAllIpByCompanyId() {
        StaffPO staff = getCurrentLoginStaff();
        List<IpWhitePO> list = ipwhiteService.getAllIpByCompanyId(staff.getCompanyId());
        Map map = new HashMap<>();
        map.put("list", list);
        map.put("state", companyService.getIpLimit(staff.getCompanyId()));
        return ResultInfoUtil.success(map);
    }

    /*
     * 修改
     */
    @PostMapping("/update")
    public ResultInfo update(@Validated @RequestBody IpWhitePO ipWhitePo) {
        //判断ip 格式
        String[] ips = ipWhitePo.getIp().split("\\.");
        if (ips.length == 0) {
            ips = ipWhitePo.getIp().split("。");
        }
        if (ips.length != 4) {
            return ResultInfoUtil.error(ExceptionEnum.IP_ERROR);
        }
        for (int i = 0; i < ips.length - 1; i++) {
            int number = Integer.parseInt(ips[i]);
            if (!(number >= 0 && number <= 255)) {
                return ResultInfoUtil.error(ExceptionEnum.IP_ERROR);

            }
        }
        if (!ips[3].equals("*")) {
            return ResultInfoUtil.error(ExceptionEnum.IP_ERROR);
        }
        ipwhiteService.update(ipWhitePo);
        return ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
    }
    /*
     * 添加到白名单
     */
    @GetMapping("/addIpWhite")
    public ResultInfo addIpWhite(@RequestParam int staffId){
    	staffService.addIpWhite(staffId);
    	return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
    }

}
