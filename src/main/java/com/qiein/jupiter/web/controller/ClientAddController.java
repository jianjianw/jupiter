package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.aop.validate.annotation.NotEmptyStr;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.service.ClientAddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 客资录入
 */
@RestController
@RequestMapping("/add")
public class ClientAddController extends BaseController {

    @Autowired
    private ClientAddService clientAddService;

    /**
     * 录入电商客资
     *
     * @return
     */
    @PostMapping("/add_ds_client")
    public ResultInfo addDsClient(@RequestBody ClientVO clientVO) {
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq(), clientVO.getKzWw())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_CONTACT_INFORMATION);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientAddService.addDsClient(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 批量录入
     *
     * @param list
     * @return
     */
    @PostMapping("/batch_add_ds_client")
    public ResultInfo batchAddDsClient(@NotEmptyStr @RequestParam("list") String list) {
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientAddService.batchAddDsClient(list, currentLoginStaff);
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
    }
}
