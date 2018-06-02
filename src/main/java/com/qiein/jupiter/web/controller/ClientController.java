package com.qiein.jupiter.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.vo.ClientStatusVO;
import com.qiein.jupiter.web.service.ClientService;

/**
 * Created by Tt on 2018/5/15 0015.
 */
@RestController
@RequestMapping("/client")
public class ClientController extends BaseController {

    @Autowired
    private ClientService clientService;

    /**
     * 修改客资性别
     *
     * @param clientStatusVO
     * @return
     */
    @PostMapping("/edit_sex")
    public ResultInfo editClientSex(@RequestBody ClientStatusVO clientStatusVO) {
        clientStatusVO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        clientStatusVO.setOperaId(getCurrentLoginStaff().getId());
        clientStatusVO.setOperaName(getCurrentLoginStaff().getNickName());
        clientService.editClientSex(clientStatusVO);
        return ResultInfoUtil.success();
    }

    /**
     * 修改客资微信状态
     *
     * @param clientStatusVO
     * @return
     */
    @PostMapping("/edit_wc")
    public ResultInfo editClientWCFlag(@RequestBody ClientStatusVO clientStatusVO) {
        clientStatusVO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        clientStatusVO.setOperaId(getCurrentLoginStaff().getId());
        clientStatusVO.setOperaName(getCurrentLoginStaff().getNickName());
        clientService.editClientWCFlag(clientStatusVO);
        return ResultInfoUtil.success();
    }

    /**
     * 获取未分配客资数量，未接入客资数量
     *
     * @return
     */
    @GetMapping("/get_not_allot_kz_num")
    public ResultInfo editClientWCFlag() {
        return ResultInfoUtil.success(clientService.getKzNumByStatusId(getCurrentLoginStaff().getCompanyId()));
    }


}
