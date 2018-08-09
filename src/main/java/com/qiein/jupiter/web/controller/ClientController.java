package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.web.entity.vo.ClientStatusVoteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 客资有效，无效，待定的判定
     */
    @PostMapping("update_kz_valid_status")
    public ResultInfo updateKzValidStatus(@RequestBody ClientStatusVoteVO clientStatusVoteVO) {
        clientStatusVoteVO.setCompanyId(getCurrentLoginStaff().getCompanyId());
        clientStatusVoteVO.setOperaId(getCurrentLoginStaff().getId());
        clientStatusVoteVO.setOperaName(getCurrentLoginStaff().getNickName());
        clientService.updateKzValidStatus(clientStatusVoteVO);
        return ResultInfoUtil.success();
    }

    /**
     * 查询客资收款修改日志
     *
     * @return
     */
    @GetMapping("/get_cash_edit_log")
    public ResultInfo getCashEditLog(@NotEmptyStr @RequestParam("kzId") String kzId) {
        return ResultInfoUtil.success(clientService.getCashEditLog(getCurrentLoginStaff().getCompanyId(), kzId));
    }
}
