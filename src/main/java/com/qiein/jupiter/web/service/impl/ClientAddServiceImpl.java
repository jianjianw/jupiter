package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.JsonFmtUtil;
import com.qiein.jupiter.util.MobileLocationUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.entity.vo.ShopVO;
import com.qiein.jupiter.web.service.ClientAddService;
import com.qiein.jupiter.web.service.ClientPushService;
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
            CompanyPO companyPO = companyDao.getById(staffPO.getId());
            clientPushService.pushLp(channelPO.getPushRule(), staffPO.getCompanyId(), jsInfo.getString("kzid"), shopVO.getId(), channelPO.getId()
                    , channelPO.getTypeId(), companyPO.getOvertime(), companyPO.getKzInterval());
        } else {
            throw new RException(ExceptionEnum.KZ_ADD_FAIL);
        }
    }


}
