package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.ClientZjsMenuConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.service.CompanyService;
import com.qiein.jupiter.web.service.DictionaryService;
import com.qiein.jupiter.web.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
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

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SystemLogService logService;

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
        return ResultInfoUtil.success(TipMsgEnum.ENTERING_SUNCCESS);
    }

    /**
     * 录入转介绍客资
     *
     * @return
     */
    @PostMapping("/add_zjs_client")
    public ResultInfo addZjsClient(@RequestBody ClientVO clientVO) {
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq(),
                clientVO.getKzWw())) {
            return ResultInfoUtil.error(ExceptionEnum.KZ_CONTACT_INFORMATION);
        }
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        //转介绍录入时，判断自定义的字段是否为空
        String zjsFields = companyService.getZjsRequiredField(currentLoginStaff.getCompanyId());
        zjsFilter(clientVO, zjsFields);
        clientAddService.addZjsClient(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.ENTERING_SUNCCESS);
    }

    /**
     * 转介绍录入时，判断自定义的字段是否为空
     *
     * @param clientVO
     * @param zjsSet
     */
    private static void zjsFilter(ClientVO clientVO, String zjsSet) {
        String[] fieldNames = zjsSet.split(CommonConstant.STR_SEPARATOR);
        for (String fieldName : fieldNames) {
            Object obj = null;
            if (StringUtil.isNotEmpty(fieldName)) {
                try {
                    obj = ObjectUtil.getObjField(clientVO, fieldName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //属性为空 或者 sex属性等于0时抛出异常
                if (ObjectUtil.isEmpty(obj)) {
                    throw new RException(ClientZjsMenuConst.LK_ZJS_MENU.get(fieldName) + "不能为空");
                }
            }
        }
    }


    /**
     * 外部转介绍录入
     *
     * @param clientVO
     * @return
     */
    @PostMapping("/add_out_zjs_client")
    public ResultInfo addOutZjsClient(@RequestBody ClientVO clientVO) {
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat()))
            throw new RException(ExceptionEnum.KZ_CONTACT_INFORMATION);

        if (!StringUtil.isPhone(clientVO.getKzPhone())) {
            clientVO.setKzWechat(clientVO.getKzPhone());
            clientVO.setKzPhone("");
        }

        if (clientVO.getOldKzPhone() != null && !StringUtil.isPhone(clientVO.getOldKzPhone()))
            throw new RException(ExceptionEnum.OLD_CLIENT_PHONE_IS_NOT_LEGAL);

        clientAddService.addOutZjsClient(clientVO);

        return ResultInfoUtil.success();
    }

    /**
     * 功能描述:
     * 外部转介绍下拉菜单
     *
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    @GetMapping("/out_zjs_menu")
    public ResultInfo OutZjsDorpDownMenu(Integer companyId) {
        if (companyId == null)
            throw new RException(ExceptionEnum.COMPANY_ID_NULL);
        return ResultInfoUtil.success(dictionaryService.getDictMapByCid(companyId));
    }

    /**
     * 录入门市客资
     *
     * @return
     */
    @PostMapping("/add_ms_client")
    public ResultInfo addMsClient(@RequestBody ClientVO clientVO) {
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq(),
                clientVO.getKzWw())) {
            throw new RException(ExceptionEnum.KZ_CONTACT_INFORMATION);
        }
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientAddService.addMsClient(clientVO, currentLoginStaff);
        return ResultInfoUtil.success(TipMsgEnum.ENTERING_SUNCCESS);
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
        int typeId = jsonObject.getIntValue("typeId");
        if (NumUtil.isNull(typeId)) {
            throw new RException(ExceptionEnum.TYPEID_IS_NULL);
        }
        String adId = StringUtil.nullToStrTrim(jsonObject.getString("adId"));
        String adAddress = StringUtil.nullToStrTrim(jsonObject.getString("adAddress"));
        String groupId = StringUtil.nullToStrTrim(jsonObject.getString("groupId"));
        String address = StringUtil.nullToStrTrim(jsonObject.getString("address"));
        int yxLevel = jsonObject.getIntValue("yxLevel");
        int ysRange = jsonObject.getIntValue("ysRange");
        int marryTime = jsonObject.getIntValue("marryTime");

        int appointId = jsonObject.getIntValue("appointId");
        int zxStyle = jsonObject.getIntValue("zxStyle");
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        JSONObject result = clientAddService.batchAddDsClient(list, channelId, sourceId, shopId, typeId, currentLoginStaff, adId,
                adAddress, groupId, appointId, zxStyle, yxLevel, ysRange, marryTime, address);
        ResultInfo rep = new ResultInfo();
        rep.setCode(result.getInteger("code"));
        rep.setMsg(result.getString("msg"));
        rep.setData(result);
        return rep;
    }

    /**
     * 发送重复录入消息
     *
     * @param kzId
     */
    @GetMapping("/push_repeat_msg")
    public void pushRepeatMsg(@RequestParam("kzId") String kzId) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientAddService.pushRepeatMsg(kzId, currentLoginStaff);
    }
}
