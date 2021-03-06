package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.enums.OrderSuccessTypeEnum;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.msg.websocket.WebSocketMsgUtil;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.*;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.entity.vo.CompanyVO;
import com.qiein.jupiter.web.entity.vo.ShopVO;
import com.qiein.jupiter.web.entity.vo.StaffDetailVO;
import com.qiein.jupiter.web.service.ClientEditService;
import com.qiein.jupiter.web.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private CashLogDao cashLogDao;
    @Autowired
    private ClientStatusDao clientStatusDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private ClientLogDao clientLogDao;
    @Autowired
    private StaffService staffService;

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
        ChannelPO channelPO = channelDao.getByIdAndCid(clientVO.getChannelId(), staffPO.getCompanyId());
        if (channelPO == null) {
            throw new RException(ExceptionEnum.CHANNEL_NOT_FOUND);
        }
        reqContent.put("channelname", channelPO.getChannelName());
        // 获取来源名
        SourcePO sourcePO = sourceDao.getByIdAndCid(clientVO.getSourceId(), staffPO.getCompanyId());
        if (sourcePO == null) {
            throw new RException(ExceptionEnum.SOURCE_NOT_FOUND);
        }
        reqContent.put("sourcename", sourcePO.getSrcName());
        if (NumUtil.isValid(clientVO.getShopId())) {
            // 获取拍摄地名
            ShopVO shopVO = shopDao.getShowShopById(staffPO.getCompanyId(), clientVO.getShopId());
            if (shopVO == null) {
                throw new RException(ExceptionEnum.SHOP_NOT_FOUND);
            }
            reqContent.put("shopid", clientVO.getShopId());
            reqContent.put("shopname", shopVO.getShopName());
        }
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
        reqContent.put("remarkform", clientVO.getRemarkForm());
        System.out.println(clientVO.getRemarkForm());
        String addRstStr = crmBaseApi.doService(reqContent, "clientEditDscjHs");
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
                GoEasyUtil.pushRemark(staffPO.getCompanyId(), info.getAppointorId(), msg, info.getKzId(), newsDao, staffDao);
            }

        } else if ("130019".equals(jsInfo.getString("code"))) {
            //重复客资，给邀约推送消息
            ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(jsInfo.getString("data"),
                    DBSplitUtil.getInfoTabName(staffPO.getCompanyId()),
                    DBSplitUtil.getDetailTabName(staffPO.getCompanyId()));
            if (info == null || NumUtil.isNull(info.getAppointorId())) {
                throw new RException("存在重复客资");
            }
            GoEasyUtil.pushRepeatClient(staffPO.getCompanyId(), info.getAppointorId(), info, staffPO.getNickName(), newsDao, staffDao);
            GoEasyUtil.pushRepeatClient(staffPO.getCompanyId(), info.getCollectorId(), info, staffPO.getNickName(), newsDao, staffDao);
            throw new RException("存在重复客资");
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
        // 邀约结果
        if (NumUtil.isValid(clientVO.getYyRst())) {
            reqContent.put("yyrst", clientVO.getYyRst());
            // 无效,或者流失
            if (ClientStatusConst.INVALID_BE_STAY == clientVO.getYyRst()
                    || ClientStatusConst.COME_SHOP_FAIL == clientVO.getYyRst()) {
                reqContent.put("invalidcode", clientVO.getInvalidCode());
                reqContent.put("invalidlabel", clientVO.getInvalidLabel());
                reqContent.put("invalidmemo", StringUtil.nullToStrTrim(clientVO.getInvalidMemo()));
            }
            // 下次追踪
            if (ClientStatusConst.TRACE_STATUS_RANGE.contains(clientVO.getYyRst())) {
                reqContent.put("tracktime", clientVO.getTrackTime());
                reqContent.put("trackmemo", clientVO.getTrackMemo());
            }
            //预约到店
            if (ClientStatusConst.BE_COMFIRM == clientVO.getYyRst()) {
                reqContent.put("appointtime", clientVO.getAppointTime());
                ShopVO shopVO = shopDao.getShowShopById(staffPO.getCompanyId(), clientVO.getShopId());
                if (shopVO == null) {
                    throw new RException(ExceptionEnum.SHOP_NOT_FOUND);
                }
                reqContent.put("shopid", clientVO.getShopId());
                reqContent.put("shopname", shopVO.getShopName());
            }
            // 在线订单
            if (ClientStatusConst.ONLINE_SUCCESS == clientVO.getYyRst()) {
                reqContent.put("amount", clientVO.getAmount());// 成交套系金额
                reqContent.put("htnum", clientVO.getHtNum());// 合同编号
                reqContent.put("payamount", clientVO.getPayAmount());// 本次收款金额
//                reqContent.put("stayamount", clientVO.getStayAmount());// 已收金额
                reqContent.put("successtime", clientVO.getSuccessTime());// 订单时间
                reqContent.put("paystyle", clientVO.getPayStyle());// 付款方式
                reqContent.put("paytime", clientVO.getSuccessTime());// 收款时间,就是订单时间
                reqContent.put("payreceiptid", staffPO.getId());// 收款人id
                reqContent.put("payreceiptname", staffPO.getNickName());// 收款人姓名
            }
            //在线保留
            if (ClientStatusConst.ONLINE_STAY == clientVO.getYyRst()) {
//                reqContent.put("stayamount", clientVO.getStayAmount());// 已收金额
                reqContent.put("payamount", clientVO.getPayAmount());// 本次收款金额
                reqContent.put("paystyle", clientVO.getPayStyle());// 付款方式
                reqContent.put("paytime", clientVO.getPayTime());// 收款时间
                reqContent.put("payreceiptid", staffPO.getId());// 收款人id
                reqContent.put("payreceiptname", staffPO.getNickName());// 收款人姓名
            }
        }
        reqContent.put("memo", clientVO.getMemo());
        String addRstStr = crmBaseApi.doService(reqContent, "clientEditDsyyHs");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(clientVO.getKzId(),
                    DBSplitUtil.getInfoTabName(staffPO.getCompanyId()),
                    DBSplitUtil.getDetailTabName(staffPO.getCompanyId()));
            if (NumUtil.isValid(clientVO.getYyRst())) {
                if (ClientStatusConst.ONLINE_SUCCESS == clientVO.getYyRst()) {
                    // 成功订单爆彩
                    StaffDetailVO appoint = staffDao.getStaffDetailVO(info.getAppointorId(), staffPO.getCompanyId());
                    if (appoint == null) {
                        return;
                    }
                    OrderSuccessMsg orderSuccessMsg = new OrderSuccessMsg();
                    orderSuccessMsg.setCompanyId(staffPO.getCompanyId());
                    orderSuccessMsg.setStaffName(appoint.getNickName());
                    orderSuccessMsg.setShopName(info.getFilmingArea());
                    orderSuccessMsg.setAmount(String.valueOf(clientVO.getAmount()));
                    orderSuccessMsg.setType(OrderSuccessTypeEnum.ArrivalShop);
                    orderSuccessMsg.setSrcImg(String.valueOf(info.getSourceId()));
                    orderSuccessMsg.setHeadImg(appoint.getHeadImg());
                    webSocketMsgUtil.pushOrderSuccessMsg(orderSuccessMsg);
                    // 发送成功消息给录入人
                    GoEasyUtil.pushSuccessOnline(info.getCompanyId(), info.getCollectorId(), info, newsDao, staffDao);
                    GoEasyUtil.pushSuccessOnline(info.getCompanyId(), info.getAppointorId(), info, newsDao, staffDao);
                    //TODO 这个地方会NPE 修复一下
                } else if (ClientStatusConst.INVALID_BE_STAY == clientVO.getYyRst() && staffService.getMsgSetByStaffId(info.getCompanyId(), info.getCollectorId()).isAllowWxDingMsg()) {
                    // 如果是无效，发送警告消息给录入人
                    GoEasyUtil.pushYyValidReject(info.getCompanyId(), info.getCollectorId(), info, newsDao, staffDao);
                } else if (ClientStatusConst.BE_COMFIRM == clientVO.getYyRst()) {
                    //TODO 金夫人不要，你懂得
                    if(staffPO.getCompanyId()!=3){
                        GoEasyUtil.pushAppointShop(info.getCompanyId(), info.getCollectorId(), info, newsDao, staffDao);
                    }
                }
            }
        } else if ("130019".equals(jsInfo.getString("code"))) {
            //重复客资，给邀约推送消息
            ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(jsInfo.getString("data"),
                    DBSplitUtil.getInfoTabName(staffPO.getCompanyId()),
                    DBSplitUtil.getDetailTabName(staffPO.getCompanyId()));
            if (info == null || NumUtil.isNull(info.getAppointorId())) {
                throw new RException("存在重复客资");
            }
            GoEasyUtil.pushRepeatClient(staffPO.getCompanyId(), info.getAppointorId(), info, staffPO.getNickName(), newsDao, staffDao);
            GoEasyUtil.pushRepeatClient(staffPO.getCompanyId(), info.getCollectorId(), info, staffPO.getNickName(), newsDao, staffDao);
            throw new RException("存在重复客资");
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }

    /**
     * 门市修改客资
     *
     * @param clientVO
     * @param staffPO
     */
    public void editClientByMsjd(ClientVO clientVO, StaffPO staffPO) {
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
        reqContent.put("remarkform", clientVO.getRemarkForm());
        // 接待结果
        if (NumUtil.isValid(clientVO.getYyRst())) {
            reqContent.put("yyrst", clientVO.getYyRst());
            ShopVO shopVO = shopDao.getShowShopById(staffPO.getCompanyId(), clientVO.getShopId());
            if (shopVO == null) {
                throw new RException(ExceptionEnum.SHOP_NOT_FOUND);
            }
            reqContent.put("shopid", clientVO.getShopId());
            reqContent.put("shopname", shopVO.getShopName());
            StaffPO receptor = staffDao.getById(clientVO.getReceptorId());
            if (receptor == null) {
                throw new RException(ExceptionEnum.STAFF_IS_NOT_EXIST);
            }
            reqContent.put("receptorid", clientVO.getReceptorId());
            reqContent.put("receptorname", receptor.getNickName());
            //进店未定
            if (ClientStatusConst.BE_RUN_OFF == clientVO.getYyRst()) {
                reqContent.put("appointtime", clientVO.getAppointTime());
                reqContent.put("comeshoptime", clientVO.getComeShopTime());
                reqContent.put("invalidLabel",
                        clientVO.getInvalidLabel() + StringUtil.nullToStrTrim(clientVO.getInvalidMemo()));
            }
            // 进店成交
            if (ClientStatusConst.BE_SUCCESS == clientVO.getYyRst() || ClientStatusConst.BE_SUCCESS_STAY == clientVO.getYyRst()) {
                reqContent.put("amount", clientVO.getAmount());// 成交套系金额
                reqContent.put("payamount", clientVO.getPayAmount());// 本次收款金额
                reqContent.put("successtime", clientVO.getSuccessTime());// 订单时间
                reqContent.put("htnum", clientVO.getHtNum());// 合同编号
                reqContent.put("paystyle", clientVO.getPayStyle());// 付款方式
                reqContent.put("paytime", clientVO.getSuccessTime());// 收款时间
                reqContent.put("payreceiptid", NumUtil.isValid(clientVO.getReceiptId()) ? clientVO.getReceiptId() : clientVO.getReceptorId());// 收款人id
                reqContent.put("payreceiptname", StringUtil.isNotEmpty(clientVO.getReceiptName()) ? clientVO.getReceiptName() : receptor.getNickName());// 收款人姓名
            }
        }
        reqContent.put("memo", clientVO.getMemo());
        String addRstStr = crmBaseApi.doService(reqContent, "clientEditMsjdHs");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("100000".equals(jsInfo.getString("code"))) {
            ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(clientVO.getKzId(),
                    DBSplitUtil.getInfoTabName(staffPO.getCompanyId()),
                    DBSplitUtil.getDetailTabName(staffPO.getCompanyId()));
            if (NumUtil.isValid(clientVO.getYyRst())) {
                if (ClientStatusConst.BE_SUCCESS == clientVO.getYyRst()) {
                    // 成功订单爆彩
                    StaffDetailVO appoint = staffDao.getStaffDetailVO(info.getAppointorId(), staffPO.getCompanyId());
                    if (appoint == null) {
                        return;
                    }
                    OrderSuccessMsg orderSuccessMsg = new OrderSuccessMsg();
                    orderSuccessMsg.setCompanyId(staffPO.getCompanyId());
                    orderSuccessMsg.setStaffName(appoint.getNickName());
                    orderSuccessMsg.setShopName(info.getFilmingArea());
                    orderSuccessMsg.setAmount(String.valueOf(clientVO.getAmount()));
                    orderSuccessMsg.setType(OrderSuccessTypeEnum.ArrivalShop);
                    orderSuccessMsg.setSrcImg(String.valueOf(info.getSourceId()));
                    orderSuccessMsg.setHeadImg(appoint.getHeadImg());
                    webSocketMsgUtil.pushOrderSuccessMsg(orderSuccessMsg);
                    // 发送成功消息给录入人，邀约人
                    GoEasyUtil.pushSuccessShop(info.getCompanyId(), info.getAppointorId(), info, newsDao, staffDao);
                    GoEasyUtil.pushSuccessShop(info.getCompanyId(), info.getCollectorId(), info, newsDao, staffDao);
                } else if (ClientStatusConst.BE_RUN_OFF == clientVO.getYyRst()) {
                    //进店未定,发送消息给客服
                    GoEasyUtil.pushComeNotSuccess(info.getCompanyId(), info.getAppointorId(), info, newsDao, staffDao);
                }
            }
        } else if ("130019".equals(jsInfo.getString("code"))) {
            //重复客资，给邀约推送消息
            ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(jsInfo.getString("data"),
                    DBSplitUtil.getInfoTabName(staffPO.getCompanyId()),
                    DBSplitUtil.getDetailTabName(staffPO.getCompanyId()));
            if (info == null || NumUtil.isNull(info.getAppointorId())) {
                throw new RException("存在重复客资");
            }
            GoEasyUtil.pushRepeatClient(staffPO.getCompanyId(), info.getAppointorId(), info, staffPO.getNickName(), newsDao, staffDao);
            GoEasyUtil.pushRepeatClient(staffPO.getCompanyId(), info.getCollectorId(), info, staffPO.getNickName(), newsDao, staffDao);
            throw new RException("存在重复客资");
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
        if (NumUtil.isValid(clientVO.getShopId())) {
            // 获取拍摄地名
            ShopVO shopVO = shopDao.getShowShopById(staffPO.getCompanyId(), clientVO.getShopId());
            if (shopVO == null) {
                throw new RException(ExceptionEnum.SHOP_NOT_FOUND);
            }
            reqContent.put("shopid", clientVO.getShopId());
            reqContent.put("shopname", shopVO.getShopName());
        }
        if (NumUtil.isValid(clientVO.getReceptorId())) {
            StaffPO receptor = staffDao.getById(clientVO.getReceptorId());
            if (receptor == null) {
                throw new RException(ExceptionEnum.STAFF_IS_NOT_EXIST);
            }
            reqContent.put("receptorid", clientVO.getReceptorId());
            reqContent.put("receptorname", receptor.getNickName());
        }
        if (NumUtil.isValid(clientVO.getCollectorId())) {
            StaffPO collector = staffDao.getById(clientVO.getCollectorId());
            if (collector == null) {
                throw new RException(ExceptionEnum.STAFF_IS_NOT_EXIST);
            }
            reqContent.put("collectorid", clientVO.getCollectorId());
            reqContent.put("collectorname", collector.getNickName());
        }

        // 纠错信息
        // 获取来源名
        SourcePO sourcePO = sourceDao.getByIdAndCid(clientVO.getSourceId(), staffPO.getCompanyId());
        if (sourcePO == null) {
            throw new RException(ExceptionEnum.SOURCE_NOT_FOUND);
        }
        reqContent.put("sourceid", clientVO.getSourceId());
        reqContent.put("sourcename", sourcePO.getSrcName());
        reqContent.put("srctype", sourcePO.getTypeId());

        // 获取渠道名
        ChannelPO channelPO = channelDao.getShowChannelById(staffPO.getCompanyId(), clientVO.getChannelId());
        if (channelPO == null) {
            throw new RException(ExceptionEnum.CHANNEL_NOT_FOUND);
        }
        reqContent.put("channelid", clientVO.getChannelId());
        reqContent.put("channelname", channelPO.getChannelName());
        reqContent.put("createtime", clientVO.getCreateTime());
        reqContent.put("statusid", clientVO.getStatusId());
        reqContent.put("appointtime", clientVO.getAppointTime());
        reqContent.put("comeshoptime", clientVO.getComeShopTime());

        if (NumUtil.isValid(clientVO.getFilmingCode())) {
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
        String addRstStr = crmBaseApi.doService(reqContent, "clientEditCwzxHs");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("130019".equals(jsInfo.getString("code"))) {
            //重复客资，给邀约推送消息
            ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(jsInfo.getString("data"),
                    DBSplitUtil.getInfoTabName(staffPO.getCompanyId()),
                    DBSplitUtil.getDetailTabName(staffPO.getCompanyId()));
            if (info == null || NumUtil.isNull(info.getAppointorId())) {
                throw new RException("存在重复客资");
            }
            GoEasyUtil.pushRepeatClient(staffPO.getCompanyId(), info.getAppointorId(), info, staffPO.getNickName(), newsDao, staffDao);
            GoEasyUtil.pushRepeatClient(staffPO.getCompanyId(), info.getCollectorId(), info, staffPO.getNickName(), newsDao, staffDao);
            throw new RException("存在重复客资");
        } else if (!"100000".equals(jsInfo.getString("code"))) {
            throw new RException(jsInfo.getString("msg"));
        }
    }

    /**
     * 修改客资详情
     *
     * @param clientVO
     * @param staffPO
     */
    public void editClientDetail(ClientVO clientVO, StaffPO staffPO) {
        Map<String, Object> reqContent = new HashMap<>();
        reqContent.put("companyid", staffPO.getCompanyId());
        reqContent.put("operaid", staffPO.getId());
        reqContent.put("operaname", staffPO.getNickName());
        reqContent.put("kzid", clientVO.getKzId());
        // 客资基础信息
        reqContent.put("sex", clientVO.getSex());
        reqContent.put("kzname", clientVO.getKzName());
        reqContent.put("kzphone", clientVO.getKzPhone());
        reqContent.put("kzwechat", clientVO.getKzWechat());
        reqContent.put("kzqq", clientVO.getKzQq());
        reqContent.put("kzww", clientVO.getKzWw());
        reqContent.put("matename", clientVO.getMateName());
        reqContent.put("matephone", clientVO.getMatePhone());
        reqContent.put("matewechat", clientVO.getMateWeChat());
        reqContent.put("mateqq", clientVO.getMateQq());
        reqContent.put("oldkzname", clientVO.getOldKzName());
        reqContent.put("oldkzphone", clientVO.getOldKzPhone());
        String addRstStr = crmBaseApi.doService(reqContent, "clientEditDetailHs");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        if ("130019".equals(jsInfo.getString("code"))) {
            //重复客资，给邀约推送消息
            ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(jsInfo.getString("data"),
                    DBSplitUtil.getInfoTabName(staffPO.getCompanyId()),
                    DBSplitUtil.getDetailTabName(staffPO.getCompanyId()));
            if (info == null || NumUtil.isNull(info.getAppointorId())) {
                throw new RException("存在重复客资");
            }
            GoEasyUtil.pushRepeatClient(staffPO.getCompanyId(), info.getAppointorId(), info, staffPO.getNickName(), newsDao, staffDao);
            GoEasyUtil.pushRepeatClient(staffPO.getCompanyId(), info.getCollectorId(), info, staffPO.getNickName(), newsDao, staffDao);
            throw new RException("存在重复客资");
        } else if (!"100000".equals(jsInfo.getString("code"))) {
            throw new RException(jsInfo.getString("msg"));
        }
    }

    /**
     * 快捷备注
     *
     * @param kzId
     * @param memo
     */
    @Transactional(rollbackFor = Exception.class)
    public void editFastMemo(String kzId, String memo, StaffPO staffPO) {
        //修改备注
        clientDao.editClientMemoLabel(DBSplitUtil.getDetailTabName(staffPO.getCompanyId()), staffPO.getCompanyId(), kzId, memo);
        memo = "快捷备注：" + memo;
        //添加客资操作日志
        clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(staffPO.getCompanyId()), new ClientLogPO(kzId,
                staffPO.getId(), staffPO.getNickName(), memo, ClientLogConst.INFO_LOGTYPE_EDIT, staffPO.getCompanyId()));
        //添加邀约记录
        clientLogDao.addInvitationLog(DBSplitUtil.getInvitaLogTabName(staffPO.getCompanyId()), kzId, memo, staffPO.getId(), staffPO.getCompanyId());
    }
    /**
     * 渠道转移客资
     * @param clientSourceDTO
     */
    public void changeClientSource(ClientSourceDTO clientSourceDTO){
        clientInfoDao.changeClientSource(clientSourceDTO);
    }

}
