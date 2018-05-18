package com.qiein.jupiter.web.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.qiein.jupiter.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.web.dao.ChannelDao;
import com.qiein.jupiter.web.dao.CompanyDao;
import com.qiein.jupiter.web.dao.GroupDao;
import com.qiein.jupiter.web.dao.ShopDao;
import com.qiein.jupiter.web.dao.SourceDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.entity.vo.ShopVO;
import com.qiein.jupiter.web.service.ClientAddService;
import com.qiein.jupiter.web.service.ClientPushService;

@Service
public class ClientAddServiceImpl implements ClientAddService {
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private SourceDao sourceDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private CrmBaseApi crmBaseApi;
    @Autowired
    private ClientPushService clientPushService;
    @Autowired
    private CompanyDao companyDao;

    /**
     * 添加电商客资
     *
     * @param clientVO
     * @param staffPO
     */
    public void addDsClient(ClientVO clientVO, StaffPO staffPO) {
        Map<String, Object> reqContent = new HashMap<String, Object>();
        reqContent.put("companyid", staffPO.getCompanyId());
        reqContent.put("collectorid", staffPO.getId());
        reqContent.put("collectorname", staffPO.getNickName());
        //获取渠道名
        ChannelPO channelPO = channelDao.getShowChannelById(staffPO.getCompanyId(), clientVO.getChannelId());
        if (channelPO == null) {
            throw new RException(ExceptionEnum.CHANNEL_NOT_FOUND);
        }
        reqContent.put("channelname", channelPO.getChannelName());
        //获取来源名
        SourcePO sourcePO = sourceDao.getShowSourceById(staffPO.getCompanyId(), clientVO.getSourceId());
        if (sourcePO == null) {
            throw new RException(ExceptionEnum.SOURCE_NOT_FOUND);
        }
        reqContent.put("sourcename", sourcePO.getSrcName());
        //获取拍摄地名
        ShopVO shopVO = shopDao.getShowShopById(sourcePO.getCompanyId(), clientVO.getShopId());
        if (shopVO == null) {
            throw new RException(ExceptionEnum.SHOP_NOT_FOUND);
        }
        reqContent.put("shopname", shopVO.getShopName());
        //获取邀约客服名称
        if (NumUtil.isNotNull(clientVO.getAppointId())) {
            StaffPO appoint = staffDao.getById(clientVO.getAppointId());
            if (appoint == null) {
                throw new RException(ExceptionEnum.APPOINT_NOT_FOUND);
            }
            reqContent.put("appointid", clientVO.getAppointId());
            reqContent.put("appointname", appoint.getNickName());
        }
        //获取邀约客服组名称
        if (StringUtil.isNotEmpty(clientVO.getGroupId())) {
            GroupPO groupPO = groupDao.getGroupById(sourcePO.getCompanyId(), clientVO.getGroupId());
            if (groupPO == null) {
                throw new RException(ExceptionEnum.APPOINT_GROUP_NOT_FOUND);
            }
            reqContent.put("groupid", clientVO.getGroupId());
            reqContent.put("groupname", groupPO.getGroupName());
        }

        reqContent.put("sex", clientVO.getSex());
        reqContent.put("kzname", clientVO.getKzName());
        reqContent.put("kzphone", clientVO.getKzPhone());
        reqContent.put("kzwechat", clientVO.getKzWechat());
        reqContent.put("kzqq", clientVO.getKzQq());
        reqContent.put("kzww", clientVO.getKzWw());
        reqContent.put("channelid", clientVO.getChannelId());
        reqContent.put("sourceid", clientVO.getSourceId());
        reqContent.put("srctype", ChannelConstant.DS_ONLY);
        reqContent.put("shopid", clientVO.getShopId());
        reqContent.put("zxstyle", clientVO.getZxStyle());
        reqContent.put("keyword", clientVO.getKeyWord());
        reqContent.put("adaddress", clientVO.getAdAddress());
        reqContent.put("adid", clientVO.getAdId());
        reqContent.put("typeid", clientVO.getTypeId());
        reqContent.put("address", StringUtil.isNotEmpty(clientVO.getAddress()) ? clientVO.getAddress() :
                MobileLocationUtil.getAddressByContactInfo(clientVO.getKzPhone(), clientVO.getKzWechat(), clientVO.getKzQq()));
        reqContent.put("remark", clientVO.getRemark());

        String addRstStr = crmBaseApi.doService(reqContent, "addClientInfoPcDsLp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            System.out.println("录入成功");
            CompanyPO companyPO = companyDao.getById(staffPO.getCompanyId());
            clientPushService.pushLp(channelPO.getPushRule(), staffPO.getCompanyId(), jsInfo.getString("kzid"), clientVO.getShopId(), channelPO.getId()
                    , channelPO.getTypeId(), companyPO.getOvertime(), companyPO.getKzInterval());
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }

    @Override
    public JSONArray changeStrToInfo(String text) {
        String[] strArr = text.split("\\n");
        JSONArray jsonArr = new JSONArray();
        JSONObject json = null;
        String[] infoArrOld = null;
        List<String> infoArr = new LinkedList<String>();
        StringBuffer sb = null;
        for (String str : strArr) {
            if (StringUtil.isEmpty(str)) {
                continue;
            }
            sb = new StringBuffer();
            infoArrOld = str.split("\\s");
            infoArr.clear();
            for (String s : infoArrOld) {
                if (StringUtil.isNotEmpty(s)) {
                    infoArr.add(s);
                }
            }
            json = new JSONObject();
            Iterator<String> it = infoArr.iterator();
            while (it.hasNext()) {
                String info = it.next();
                //1.客资姓名
                if (StringUtil.isChinese(info) && info.length() < 6 && !json.containsKey("name")) {
                    json.put("name", info);
                    it.remove();
                    continue;
                }
                // 2.客资手机号
                if (RegexUtil.checkMobile(info) && !json.containsKey("phone")) {
                    json.put("phone", info);
                    it.remove();
                    continue;
                }
                // 3.客资微信号
                if (StringUtil.checkWeChat(info) && !json.containsKey("wechat")) {
                    json.put("wechat", info);
                    it.remove();
                    continue;
                }
                // 4.客资QQ号
                if (StringUtil.isQQCorrect(info) && !json.containsKey("qq")) {
                    json.put("qq", info);
                    it.remove();
                    continue;
                }
                //5.客资旺旺号
                if (StringUtil.checkWeChat(info) && !json.containsKey("ww")) {
                    json.put("ww", info);
                    it.remove();
                    continue;
                }
            }
            // 其余放入备注
            for (String memo : infoArr) {
                sb.append(memo);
            }
            json.put("memo", StringUtil.nullToStrTrim(sb.toString()));
            jsonArr.add(json);
        }
        return jsonArr;
    }

    /**
     * 批量录入
     *
     * @param list
     */
    public JSONObject batchAddDsClient(String list, int channelId, int sourceId, int shopId, int typeId, StaffPO staffPO) {
        JSONArray jsonArr = JSONArray.parseArray(list);
        int successCount = 0;
        int errorCount = 0;
        StringBuffer sb = null;
        JSONObject error = null;
        JSONObject result = new JSONObject();
        JSONArray rep = new JSONArray();
        for (int i = 0; i < jsonArr.size(); i++) {
            ClientVO clientVO = new ClientVO();
            clientVO.setChannelId(channelId);
            clientVO.setSourceId(sourceId);
            clientVO.setShopId(shopId);
            clientVO.setTypeId(typeId);
            clientVO.setKzName(String.valueOf(JSONObject.parseObject(jsonArr.getString(i)).get("name")));
            clientVO.setKzPhone(String.valueOf(JSONObject.parseObject(jsonArr.getString(i)).get("phone")));
            clientVO.setKzWechat(String.valueOf(JSONObject.parseObject(jsonArr.getString(i)).get("wechat")));
            clientVO.setKzQq(String.valueOf(JSONObject.parseObject(jsonArr.getString(i)).get("qq")));
            clientVO.setKzWw(String.valueOf(JSONObject.parseObject(jsonArr.getString(i)).get("matephone")));
            if (StringUtil.isEmpty(clientVO.getKzPhone()) && StringUtil.isEmpty(clientVO.getKzWechat()) && StringUtil.isEmpty(clientVO.getKzQq())
                    && StringUtil.isEmpty(clientVO.getKzWw())) {
                continue;
            }
            sb = new StringBuffer();
            sb.append("<p>");
            sb.append(
                    StringUtil.nullToStrTrim(String.valueOf(JSONObject.parseObject(jsonArr.getString(i)).get("memo"))));
            sb.append("</p>");
            clientVO.setRemark(sb.toString());
            try {
                addDsClient(clientVO, staffPO);
                successCount++;
            } catch (RException e) {
                error = new JSONObject();
                error.put("name", clientVO.getKzName());
                error.put("phone", clientVO.getKzPhone());
                error.put("wechat", clientVO.getKzWechat());
                error.put("qq", clientVO.getKzQq());
                error.put("memo", StringUtil.replaceAllHTML(sb.toString()));
                error.put("msg", e.getMsg());
                rep.add(error);
                errorCount++;
            }
        }
        result.put("error", rep);
        result.put("msg", "errorCount == 0 ? \"录入成功，共录入：\" + successCount + \" 个客资\" : \"有录入失败的信息；录入成功：\" + successCount + \" 个，错误：\" + errorCount + \" 个\"");
        return result;
    }

}
