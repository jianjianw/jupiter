package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.NumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.service.ClientAddService;

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
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq(),
                clientVO.getKzWw())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_CONTACT_INFORMATION);
        }
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientAddService.addDsClient(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TigMsgEnum.SAVE_SUCCESS);
    }

    /**
     * 批量录入
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/batch_add_ds_client")
    public ResultInfo batchAddDsClient(@RequestBody JSONObject jsonObject) {
        String list = StringUtil.nullToStrTrim(jsonObject.getString("list"));
        if (StringUtil.isEmpty(list)) {
            throw new RException(ExceptionEnum.INFO_IS_NULL);
        }
        int sourceId = jsonObject.getIntValue("sourceId");
        if (NumUtil.isNull(sourceId)) {
            throw new RException(ExceptionEnum.SOURCE_ID_NULL);
        }
        int channelId = jsonObject.getIntValue("channelId");
        if (NumUtil.isNull(channelId)) {
            throw new RException(ExceptionEnum.CHANNEL_ID_NULL);
        }
        int shopId = jsonObject.getIntValue("shopId");
        if (NumUtil.isNull(shopId)) {
            throw new RException(ExceptionEnum.SHOP_ID_NULL);
        }
        int typeId = jsonObject.getIntValue("typeId");
        if (NumUtil.isNull(typeId)) {
            throw new RException(ExceptionEnum.TYPEID_IS_NULL);
        }
        String adId = StringUtil.nullToStrTrim(jsonObject.getString("adId"));
        String adAddress = StringUtil.nullToStrTrim(jsonObject.getString("adAddress"));
        String groupId = StringUtil.nullToStrTrim(jsonObject.getString("groupId"));
        int appointId = jsonObject.getIntValue("appointid");
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        JSONObject result = clientAddService.batchAddDsClient(list, channelId, sourceId, shopId, typeId, currentLoginStaff, adId, adAddress, groupId, appointId);
        ResultInfo rep = new ResultInfo();
        rep.setCode(result.getInteger("code"));
        rep.setMsg(result.getString("msg"));
        rep.setData(result);
        return rep;
    }
}
