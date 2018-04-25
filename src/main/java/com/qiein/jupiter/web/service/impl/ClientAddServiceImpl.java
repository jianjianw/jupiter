package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.JsonFmtUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.po.GroupPO;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.entity.vo.ShopVO;
import com.qiein.jupiter.web.service.ClientAddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static Map<String, Object> reqContent;

    @Override
    public void addDsClient(ClientVO clientVO, StaffPO staffPO) {
        reqContent = new HashMap<String, Object>();
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
        if (NumUtil.isNotNull(clientVO.getShopId())) {
            ShopVO shopVO = shopDao.getShowShopById(sourcePO.getCompanyId(), clientVO.getShopId());
            if (shopVO == null) {
                throw new RException(ExceptionEnum.SHOP_NOT_FOUND);
            }
            reqContent.put("shopname", shopVO.getShopName());
        }
        //获取邀约客服名称
        if (NumUtil.isNotNull(clientVO.getAppointId())) {
            StaffPO appoint = staffDao.getById(clientVO.getAppointId());
            if (appoint == null) {
                throw new RException(ExceptionEnum.APPOINT_NOT_FOUND);
            }
            reqContent.put("appointname", appoint.getNickName());
        }
        //获取邀约客服组名称
        if (StringUtil.isNotEmpty(clientVO.getGroupId())) {
            GroupPO groupPO = groupDao.getGroupById(sourcePO.getCompanyId(), clientVO.getGroupId());
            if (groupPO == null) {
                throw new RException(ExceptionEnum.APPOINT_GROUP_NOT_FOUND);
            }
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
        reqContent.put("shopid", clientVO.getShopId());
        reqContent.put("zxstyle", clientVO.getZxStyle());
        reqContent.put("keyword", clientVO.getKeyWord());
        reqContent.put("adaddress", clientVO.getAdAddress());
        reqContent.put("adid", clientVO.getAdId());
        reqContent.put("typeid", clientVO.getTypeId());

//TODO 如果地址为空，获取手机号地址
        reqContent.put("address", clientVO.getAddress());


        reqContent.put("remark", clientVO.getRemark());
        reqContent.put("appointid", clientVO.getAppointId());
        reqContent.put("groupid", clientVO.getGroupId());


        String addRstStr = crmBaseApi.doService(reqContent, "clientAddDs");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            System.out.println("录入成功");
        } else if ("130004".equals(jsInfo.getString("code")) || "130005".equals(jsInfo.getString("code"))
                || "130006".equals(jsInfo.getString("code"))) {
            throw new RException("录入失败");
        }

    }


}
