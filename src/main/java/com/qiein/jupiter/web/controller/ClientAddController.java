package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.service.ClientAddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        if (NumUtil.isNull(clientVO.getChannelId())) {
            return ResultInfoUtil.error(ExceptionEnum.CHANNEL_ID_NULL);
        }
        if (NumUtil.isNull(clientVO.getSourceId())) {
            return ResultInfoUtil.error(ExceptionEnum.SOURCE_ID_NULL);
        }
        if (NumUtil.isNull(clientVO.getTypeId())) {
            return ResultInfoUtil.error(ExceptionEnum.INVALID_REASON_TYPE_NULL);
        }
        //获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientAddService.addDsClient(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgConstant.SAVE_SUCCESS);
    }
}
