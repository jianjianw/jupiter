package com.qiein.jupiter.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.enums.OrderSuccessTypeEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.msg.websocket.WebSocketMsgUtil;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.JsonFmtUtil;
import com.qiein.jupiter.util.MobileLocationUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ChannelDao;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.NewsDao;
import com.qiein.jupiter.web.dao.ShopDao;
import com.qiein.jupiter.web.dao.SourceDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.dto.OrderSuccessMsg;
import com.qiein.jupiter.web.entity.po.ChannelPO;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.entity.vo.ShopVO;
import com.qiein.jupiter.web.service.ClientEditService;

/**
 * 客资编辑业务层
 */
@Service
public class ClientEditServiceImpl implements ClientEditService {

    @Autowired
    private CrmBaseApi crmBaseApi;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private SourceDao sourceDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private WebSocketMsgUtil webSocketMsgUtil;
    @Autowired
    private ClientInfoDao clientInfoDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private NewsDao newsDao;

    /**
     * 电商推广修改客资
     *
     * @param clientVO
     * @param staffPO
     */
    @Override
    public void editClientByDscj(ClientVO clientVO, StaffPO staffPO) {
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("companyid", staffPO.getCompanyId());
        reqContent.put("operaid", staffPO.getId());
        reqContent.put("operaname", staffPO.getNickName());
        reqContent.put("kzid", clientVO.getKzId());
        // 获取渠道名
        ChannelPO channelPO = channelDao.getShowChannelById(staffPO.getCompanyId(), clientVO.getChannelId());
        if (channelPO == null) {
            throw new RException(ExceptionEnum.CHANNEL_NOT_FOUND);
        }
        reqContent.put("channelname", channelPO.getChannelName());
        // 获取来源名
        SourcePO sourcePO = sourceDao.getShowSourceById(staffPO.getCompanyId(), clientVO.getSourceId());
        if (sourcePO == null) {
            throw new RException(ExceptionEnum.SOURCE_NOT_FOUND);
        }
        reqContent.put("sourcename", sourcePO.getSrcName());
        // 获取拍摄地名
        ShopVO shopVO = shopDao.getShowShopById(staffPO.getCompanyId(), clientVO.getShopId());
        if (shopVO == null) {
            throw new RException(ExceptionEnum.SHOP_NOT_FOUND);
        }
        reqContent.put("shopname", shopVO.getShopName());
        reqContent.put("yxlevel", clientVO.getYxLevel());
        reqContent.put("ysrange", clientVO.getYsRange());
        reqContent.put("marrytime", clientVO.getMarryTime());
        reqContent.put("yptime", clientVO.getYpTime());
        reqContent.put("sex", clientVO.getSex());
        reqContent.put("kzname", clientVO.getKzName());
        reqContent.put("kzphone", clientVO.getKzPhone());
        reqContent.put("kzwechat", clientVO.getKzWechat());
        reqContent.put("kzqq", clientVO.getKzQq());
        reqContent.put("kzww", clientVO.getKzWw());
        reqContent.put("channelid", clientVO.getChannelId());
        reqContent.put("sourceid", clientVO.getSourceId());
        reqContent.put("srctype", sourcePO.getTypeId());
        reqContent.put("shopid", clientVO.getShopId());
        reqContent.put("keyword", clientVO.getKeyWord());
        reqContent.put("adaddress", clientVO.getAdAddress());
        reqContent.put("adid", clientVO.getAdId());
        reqContent.put("typeid", clientVO.getTypeId());
        reqContent.put("zxstyle", clientVO.getZxStyle());
        reqContent.put("address",
                StringUtil.isNotEmpty(clientVO.getAddress()) ? clientVO.getAddress()
                        : MobileLocationUtil.getAddressByContactInfo(clientVO.getKzPhone(), clientVO.getKzWechat(),
                        clientVO.getKzQq()));
        reqContent.put("remark", clientVO.getRemark());

        String addRstStr = crmBaseApi.doService(reqContent, "clientEditDscjLp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            String flag = JsonFmtUtil.strContentToJsonObj(addRstStr).getString("flag");
            if (StringUtil.isNotEmpty(flag) && Boolean.parseBoolean(flag)) {
                ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(clientVO.getKzId(),
                        DBSplitUtil.getInfoTabName(staffPO.getCompanyId()),
                        DBSplitUtil.getDetailTabName(staffPO.getCompanyId()));
                if (NumUtil.isNull(info.getAppointorId())) {
                    return;
                }
                String msg = JsonFmtUtil.strContentToJsonObj(addRstStr).getString("msg");
                //推送备注修改推送
                GoEasyUtil.pushRemark(staffPO.getCompanyId(), info.getAppointorId(), msg, info.getKzId(), newsDao);
            }

        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }

    /**
     * 电商邀约修改客资
     *
     * @param clientVO
     * @param staffPO
     */
    public void editClientByDsyy(ClientVO clientVO, StaffPO staffPO) {
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("companyid", staffPO.getCompanyId());
        reqContent.put("operaid", staffPO.getId());
        reqContent.put("operaname", staffPO.getNickName());
        reqContent.put("kzid", clientVO.getKzId());
        // 客资基础信息
        reqContent.put("typeid", clientVO.getTypeId());
        reqContent.put("address",
                StringUtil.isNotEmpty(clientVO.getAddress()) ? clientVO.getAddress()
                        : MobileLocationUtil.getAddressByContactInfo(clientVO.getKzPhone(), clientVO.getKzWechat(),
                        clientVO.getKzQq()));
        reqContent.put("yxlevel", clientVO.getYxLevel());
        reqContent.put("ysrange", clientVO.getYsRange());
        reqContent.put("marrytime", clientVO.getMarryTime());
        reqContent.put("yptime", clientVO.getYpTime());
        reqContent.put("sex", clientVO.getSex());
        reqContent.put("kzname", clientVO.getKzName());
        reqContent.put("kzphone", clientVO.getKzPhone());
        reqContent.put("kzwechat", clientVO.getKzWechat());
        reqContent.put("kzqq", clientVO.getKzQq());
        reqContent.put("kzww", clientVO.getKzWw());
        if (NumUtil.isNotNull(clientVO.getShopId())) {
            // 获取拍摄地名
            ShopVO shopVO = shopDao.getShowShopById(staffPO.getCompanyId(), clientVO.getShopId());
            if (shopVO == null) {
                throw new RException(ExceptionEnum.SHOP_NOT_FOUND);
            }
            reqContent.put("shopid", clientVO.getShopId());
            reqContent.put("shopname", shopVO.getShopName());
        }
        // 邀约结果
        ShopVO shopVO = null;
        if (NumUtil.isNotNull(clientVO.getYyRst())) {
            reqContent.put("yyrst", clientVO.getYyRst());
            // 无效,或者流失
            if (ClientStatusConst.INVALID_BE_STAY == clientVO.getYyRst()
                    || ClientStatusConst.COME_SHOP_FAIL == clientVO.getYyRst()) {
                reqContent.put("invalidLabel",
                        clientVO.getInvalidLabel() + StringUtil.nullToStrTrim(clientVO.getInvalidMemo()));
            }
            // 下次追踪
            if (ClientStatusConst.TRACE_STATUS_RANGE.contains(clientVO.getYyRst())) {
                reqContent.put("tracktime", clientVO.getTrackTime());
            }
            // 订单
            if (ClientStatusConst.ONLINE_SUCCESS == clientVO.getYyRst()) {
                if (NumUtil.isNotNull(clientVO.getFilmingCode())) {
                    // 获取最终拍摄地名
                    shopVO = shopDao.getShowShopById(staffPO.getCompanyId(), clientVO.getFilmingCode());
                    if (shopVO == null) {
                        throw new RException(ExceptionEnum.SHOP_NOT_FOUND);
                    }
                    reqContent.put("filmingcode", clientVO.getFilmingCode());
                    reqContent.put("filmingname", shopVO.getShopName());
                }
                reqContent.put("amount", clientVO.getAmount());// 成交套系金额
                reqContent.put("stayamount", clientVO.getStayAmount());// 已收金额
                reqContent.put("paystyle", clientVO.getPayStyle());// 支付方式
                reqContent.put("htnum", clientVO.getHtNum());// 合同编号
                reqContent.put("successtime", clientVO.getSuccessTime());// 订单时间
                reqContent.put("packageCode", clientVO.getPackageCode());// 套系名称编码
            }
        }
        reqContent.put("memo", clientVO.getMemo());
        String addRstStr = crmBaseApi.doService(reqContent, "clientEditDsyyLp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(clientVO.getKzId(),
                    DBSplitUtil.getInfoTabName(staffPO.getCompanyId()),
                    DBSplitUtil.getDetailTabName(staffPO.getCompanyId()));
            if (ClientStatusConst.ONLINE_SUCCESS == clientVO.getYyRst()) {
                // 成功订单爆彩
                StaffPO appoint = staffDao.getByIdAndCid(info.getAppointorId(), staffPO.getCompanyId());
                if (appoint == null) {
                    return;
                }
                OrderSuccessMsg orderSuccessMsg = new OrderSuccessMsg();
                orderSuccessMsg.setCompanyId(staffPO.getCompanyId());
                orderSuccessMsg.setStaffName(appoint.getNickName());
                orderSuccessMsg.setShopName(info.getFilmingArea());
                orderSuccessMsg.setAmount(String.valueOf(clientVO.getAmount()));
                orderSuccessMsg.setType(OrderSuccessTypeEnum.TourShoot);
                orderSuccessMsg.setSrcImg(String.valueOf(info.getSourceId()));
                orderSuccessMsg.setHeadImg(appoint.getHeadImg());
                webSocketMsgUtil.pushOrderSuccessMsg(orderSuccessMsg);
                // 发送成功消息给录入人
                GoEasyUtil.pushSuccessOnline(info.getCompanyId(), info.getCollectorId(), info, newsDao);
            } else if (ClientStatusConst.INVALID_BE_STAY == clientVO.getYyRst()) {
                // 如果是无效，发送警告消息给录入人
                GoEasyUtil.pushYyValidReject(info.getCompanyId(), info.getCollectorId(), info, newsDao);
            }
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }

    /**
     * 主管纠错
     *
     * @param clientVO
     * @param staffPO
     */
    public void editClientByCwzx(ClientVO clientVO, StaffPO staffPO) {
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("companyid", staffPO.getCompanyId());
        reqContent.put("operaid", staffPO.getId());
        reqContent.put("operaname", staffPO.getNickName());
        reqContent.put("kzid", clientVO.getKzId());
        // 客资基础信息
        reqContent.put("typeid", clientVO.getTypeId());
        reqContent.put("address",
                StringUtil.isNotEmpty(clientVO.getAddress()) ? clientVO.getAddress()
                        : MobileLocationUtil.getAddressByContactInfo(clientVO.getKzPhone(), clientVO.getKzWechat(),
                        clientVO.getKzQq()));
        reqContent.put("yxlevel", clientVO.getYxLevel());
        reqContent.put("ysrange", clientVO.getYsRange());
        reqContent.put("marrytime", clientVO.getMarryTime());
        reqContent.put("yptime", clientVO.getYpTime());
        reqContent.put("sex", clientVO.getSex());
        reqContent.put("kzname", clientVO.getKzName());
        reqContent.put("kzphone", clientVO.getKzPhone());
        reqContent.put("kzwechat", clientVO.getKzWechat());
        reqContent.put("kzqq", clientVO.getKzQq());
        reqContent.put("kzww", clientVO.getKzWw());
        // 获取拍摄地名
        ShopVO shopVO = shopDao.getShowShopById(staffPO.getCompanyId(), clientVO.getShopId());
        if (shopVO == null) {
            throw new RException(ExceptionEnum.SHOP_NOT_FOUND);
        }
        reqContent.put("shopid", clientVO.getShopId());
        reqContent.put("shopname", shopVO.getShopName());

        // 纠错信息
        reqContent.put("sourceid", clientVO.getSourceId());
        // 获取来源名
        SourcePO sourcePO = sourceDao.getShowSourceById(staffPO.getCompanyId(), clientVO.getSourceId());
        if (sourcePO == null) {
            throw new RException(ExceptionEnum.SOURCE_NOT_FOUND);
        }
        reqContent.put("sourcename", sourcePO.getSrcName());

        // 获取渠道名
        ChannelPO channelPO = channelDao.getShowChannelById(staffPO.getCompanyId(), clientVO.getChannelId());
        if (channelPO == null) {
            throw new RException(ExceptionEnum.CHANNEL_NOT_FOUND);
        }
        reqContent.put("channelid", clientVO.getChannelId());
        reqContent.put("channelname", channelPO.getChannelName());
        reqContent.put("createtime", clientVO.getCreateTime());
        reqContent.put("statusid", clientVO.getStatusId());
        reqContent.put("receivetime", clientVO.getReceiveTime());

        if (NumUtil.isNotNull(clientVO.getFilmingCode())) {
            // 获取最终拍摄地名
            ShopVO shop = shopDao.getShowShopById(staffPO.getCompanyId(), clientVO.getFilmingCode());
            if (shop == null) {
                throw new RException(ExceptionEnum.SHOP_NOT_FOUND);
            }
            reqContent.put("filmingcode", clientVO.getFilmingCode());
            reqContent.put("filmingarea", shop.getShopName());
        }
        reqContent.put("successtime", clientVO.getSuccessTime());// 订单时间
        reqContent.put("stayamount", clientVO.getStayAmount());// 已收金额
        reqContent.put("paystyle", clientVO.getPayStyle());// 支付方式
        reqContent.put("htnum", clientVO.getHtNum());// 合同编号
        reqContent.put("amount", clientVO.getAmount());// 成交套系金额
        reqContent.put("memo", clientVO.getMemo());
        reqContent.put("packageCode", clientVO.getPackageCode());// 套系名称编码
        String addRstStr = crmBaseApi.doService(reqContent, "clientEditCwzxLp");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if (!"100000".equals(jsInfo.getString("code"))) {
            throw new RException(jsInfo.getString("msg"));
        }
    }
}
