package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.constant.ClientZjsMenuConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.AddTypeEnum;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.dto.RequestInfoDTO;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.po.SystemLog;
import com.qiein.jupiter.web.entity.vo.ChannelVO;
import com.qiein.jupiter.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.enums.TipMsgEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private StaffService staffService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private ChannelService channelService;

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
        clientVO.setOperaId(currentLoginStaff.getId());
        clientVO.setOperaName(currentLoginStaff.getNickName());
        clientVO.setCollectorId(currentLoginStaff.getId());
        clientVO.setCollectorName(currentLoginStaff.getNickName());
        clientVO.setCompanyId(currentLoginStaff.getCompanyId());
        //什么录入的
        clientVO.setAddType(isPc() ? 1 : 2);
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
        clientVO.setOperaId(currentLoginStaff.getId());
        clientVO.setOperaName(currentLoginStaff.getNickName());
        clientVO.setCompanyId(currentLoginStaff.getCompanyId());
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
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq()))
            throw new RException(ExceptionEnum.KZ_CONTACT_WAY_WRITE_ONE);

        if (StringUtil.isNotEmpty(clientVO.getKzPhone()) && !StringUtil.isPhone(clientVO.getKzPhone())) {
            throw new RException(ExceptionEnum.PHONE_ERROR);
        }
//        if (!StringUtil.isPhone(clientVO.getKzPhone())) {
//            clientVO.setKzWechat(clientVO.getKzPhone());
//            clientVO.setKzPhone("");
//        }

        if (StringUtil.isNotEmpty(clientVO.getOldKzPhone()) && !StringUtil.isPhone(clientVO.getOldKzPhone()))
            throw new RException(ExceptionEnum.OLD_CLIENT_PHONE_IS_NOT_LEGAL);

        //如果有简单和这个名字全匹配的员工，则获取到这个人的id
        if (StringUtil.isNotEmpty(clientVO.getCollectorName())) {
            Integer i = staffService.getStaffIdByName(clientVO.getCollectorName(), clientVO.getCompanyId());
            if (i != null) {
                clientVO.setCollectorId(i);
            }
        }

        clientAddService.addOutZjsClient(clientVO);

        return ResultInfoUtil.success(TipMsgEnum.SAVE_SUCCESS);
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
    public ResultInfo OutZjsDorpDownMenu(@RequestParam(name = "channelId", required = false) Integer channelId,
                                         @RequestParam("companyId") Integer companyId) {
        if (companyId == null)
            throw new RException(ExceptionEnum.COMPANY_ID_NULL);
        Map<String, Object> map = new HashMap<>();
        map.put("dic", dictionaryService.getDictMapByCid(companyId));
//        List<SourcePO> list =sourceService.getSourceListByChannelId(channelId, companyId);
//        if (!list.isEmpty()){
//            Iterator<SourcePO> it = list.iterator();
//            while(it.hasNext()){
//                SourcePO s = it.next();
//                if(!s.getIsShow()){
//                    it.remove();
//                }
//            }
//        }
        List<ChannelVO> list = channelService.getAllShowChannelSourceList(companyId, channelId);
        map.put("srcList", list);
        return ResultInfoUtil.success(map);
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
        clientVO.setOperaId(currentLoginStaff.getId());
        clientVO.setOperaName(currentLoginStaff.getNickName());
        clientVO.setCompanyId(currentLoginStaff.getCompanyId());
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
        Integer sourceId = jsonObject.getInteger("sourceId");
        if (NumUtil.isNull(sourceId)) {
            throw new RException(ExceptionEnum.SOURCE_ID_NULL);
        }
        Integer channelId = jsonObject.getInteger("channelId");
        if (NumUtil.isNull(channelId)) {
            throw new RException(ExceptionEnum.CHANNEL_ID_NULL);
        }
        Integer shopId = jsonObject.getInteger("shopId");
        Integer typeId = jsonObject.getInteger("typeId");
        if (NumUtil.isNull(typeId)) {
            throw new RException(ExceptionEnum.TYPEID_IS_NULL);
        }
        String adId = StringUtil.nullToStrTrim(jsonObject.getString("adId"));
        String adAddress = StringUtil.nullToStrTrim(jsonObject.getString("adAddress"));
        String groupId = StringUtil.nullToStrTrim(jsonObject.getString("groupId"));
        String address = StringUtil.nullToStrTrim(jsonObject.getString("address"));
        Integer yxLevel = jsonObject.getInteger("yxLevel");
        Integer ysRange = jsonObject.getInteger("ysRange");
        Integer marryTime = jsonObject.getInteger("marryTime");

        Integer appointId = jsonObject.getInteger("appointId");
        Integer zxStyle = jsonObject.getInteger("zxStyle");
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


    /**
     * 录入钉钉客资
     *
     * @return
     */
    @PostMapping("/add_ding_client_info")
    public ResultInfo addDingClientInfo(@RequestBody ClientVO clientVO) {
        if (StringUtil.isAllEmpty(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq(),
                clientVO.getKzWw())) {
            throw new RException(ExceptionEnum.KZ_CONTACT_INFORMATION);
        }
        // 获取当前登录账户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        clientVO.setOperaId(currentLoginStaff.getId());
        clientVO.setOperaName(currentLoginStaff.getNickName());
        clientVO.setCompanyId(currentLoginStaff.getCompanyId());
        //手机端录入
        clientVO.setAddType(AddTypeEnum.MOBILE.getTypeId());
        clientAddService.addDingClientInfo(clientVO);
        return ResultInfoUtil.success(TipMsgEnum.ENTERING_SUNCCESS);
    }
}
