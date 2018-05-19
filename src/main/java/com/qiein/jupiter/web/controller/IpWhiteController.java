package com.qiein.jupiter.web.controller;

import java.util.LinkedList;
import java.util.List;

import com.qiein.jupiter.web.entity.vo.IpWhitePageVO;
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
import com.qiein.jupiter.util.HttpUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
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
        if (!HttpUtil.isIp(ipWhitePo.getIp())) {
            return ResultInfoUtil.error(ExceptionEnum.IP_ERROR);
        }
        ipWhitePo.setCompanyId(staff.getCompanyId());
        ipWhitePo.setCreatoerId(staff.getId());
        ipWhitePo.setCreatoerName(staff.getNickName());
        ipwhiteService.insert(ipWhitePo);
        return ResultInfoUtil.success(TigMsgEnum.SAFETY_IP_INSERT_SUCCESS);

    }

    /*
     * 删除
     */
    @GetMapping("/delete")
    public ResultInfo delete(@RequestParam @Id Integer id) {
        ipwhiteService.delete(id);
        return ResultInfoUtil.success(TigMsgEnum.SAFETY_IP_DEL_SUCCESS);
    }

    /*
     * 修改iplimit
     */
    @GetMapping("/edit_ip_limit")
    public ResultInfo editIpLimit(@RequestParam Integer limitFlag) {
        StaffPO staff = getCurrentLoginStaff();
        companyService.editIpLimit(limitFlag, staff.getCompanyId());
        if (limitFlag == 0) {
            return ResultInfoUtil.success(TigMsgEnum.IP_LIMIT_CLOSE_SUCCESS);
        } else {
            return ResultInfoUtil.success(TigMsgEnum.IP_LIMIT_OPEN_SUCCESS);
        }

    }

    /*
     * 显示页面
     */
    @GetMapping("/get_all_ip_by_cid")
    public ResultInfo getAllIpByCompanyId() {
        StaffPO staff = getCurrentLoginStaff();
        List<IpWhitePO> list = ipwhiteService.getAllIpByCompanyId(staff.getCompanyId());
        IpWhitePageVO ipWhitePageVO = new IpWhitePageVO();
        ipWhitePageVO.setIpWhiteList(list);
        ipWhitePageVO.setLimitFlag(companyService.getIpLimit(staff.getCompanyId()));
        return ResultInfoUtil.success(ipWhitePageVO);
    }

    /*
     * 修改
     */
    @PostMapping("/update")
    public ResultInfo update(@Validated @RequestBody IpWhitePO ipWhitePo) {
        //判断ip 格式
        if (!HttpUtil.isIp(ipWhitePo.getIp())) {
            return ResultInfoUtil.success(ExceptionEnum.IP_ERROR);
        }
        ipwhiteService.update(ipWhitePo);
        return ResultInfoUtil.success(TigMsgEnum.SAFETY_IP_UPDATE_SUCCESS);
    }

    /*
     * 添加到白名单
     */
    @GetMapping("/add_ip_white_staff")
    public ResultInfo addIpWhiteStaff(@RequestParam String staffIds) {
        staffService.addIpWhite(staffIds);
        return ResultInfoUtil.success(TigMsgEnum.IP_WHITE_ADD_SUCCESS);
    }

    /*
     * 从ip白名单删除
     */
    @GetMapping("/del_ip_white_staff")
    public ResultInfo delIpWhiteStaff(@RequestParam int staffId) {
        staffService.delIpWhite(staffId);
        return ResultInfoUtil.success(TigMsgEnum.IP_WHITE_DEL_SUCCESS);
    }

    /*
     * 批量从ip白名单删除
     */
    @GetMapping("/batch_del_ip_white_staff")
    public ResultInfo batchDelIpWhiteStaff(@RequestParam String staffIds) {
        String[] StringIds = staffIds.split(",");
        List<Integer> ids = new LinkedList<>();
        for (String s : StringIds) {
            ids.add(Integer.parseInt(s));
        }
        staffService.delListIpWhite(ids);
        return ResultInfoUtil.success(TigMsgEnum.IP_WHITE_DEL_SUCCESS);
    }

    /*
     * 在ip白名单的员工信息
     */
    @PostMapping("/find_ip_white_staff")
    public ResultInfo findIpWhiteStaff(@RequestBody QueryMapDTO queryMapDTO) {
        StaffPO staff = getCurrentLoginStaff();
        return ResultInfoUtil.success(ipwhiteService.findIpWhite(queryMapDTO, staff.getCompanyId()));
    }

}
