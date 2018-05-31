package com.qiein.jupiter.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.po.StatusPO;
import com.qiein.jupiter.web.entity.vo.ChannelDictVO;
import com.qiein.jupiter.web.entity.vo.ClientExportVO;
import com.qiein.jupiter.web.entity.vo.SourceDictVO;
import com.qiein.jupiter.web.service.ChannelService;
import com.qiein.jupiter.web.service.DictionaryService;
import com.qiein.jupiter.web.service.SourceService;
import com.qiein.jupiter.web.service.StatusService;

/**
 * 返回结果解析工具类
 *
 * @author JingChenglong 2016-09-10 16:32
 */
public class JsonFmtUtil {

    static DecimalFormat fmt = new DecimalFormat("0.00");

    /*-- 字符串转JSON --*/
    public static JSONObject strToJsonObj(String str) throws RException {

        JSONObject res = (JSONObject) JSONObject.parse(str);

        if ("100000".equals(res.getJSONObject("response").getJSONObject("info").get("code"))) {
            return res.getJSONObject("response").getJSONObject("content");
        } else {
            throw new RException(res.getJSONObject("response").getJSONObject("info").getString("msg"));
        }
    }

    /*-- 获取返回结果JSON --*/
    public static JSONObject strInfoToJsonObj(String str) throws RException {

        JSONObject res = (JSONObject) JSONObject.parse(str);

        return res.getJSONObject("response").getJSONObject("info");
    }

    /*-- 获取返回内容JSON --*/
    public static JSONObject strContentToJsonObj(String str) throws RException {

        JSONObject res = (JSONObject) JSONObject.parse(str);

        return res.getJSONObject("response").getJSONObject("content");
    }

    /*-- JSONArray转客资导出客资集合 --*/
    public static List<ClientExportVO> jsonArrToClientExportVO(JSONArray jsArr, int companyId, SourceService sourceService,
                                                               StatusService statusService, ChannelService channelService, DictionaryService dictionaryService) {
        Map<String, SourceDictVO> sourceMap = sourceService.getSourcePageMap(companyId);
        Map<String, StatusPO> statusMap = statusService.getStatusDictMap(companyId);
        Map<String, ChannelDictVO> channelMap = channelService.getChannelDict(companyId);
        Map<String, List<DictionaryPO>> dicMap = dictionaryService.getDictMapByCid(companyId);

        List<ClientExportVO> clientList = new ArrayList<ClientExportVO>();
        for (int i = jsArr.size() - 1; i >= 0; i--) {
            JSONObject info = (JSONObject) jsArr.getJSONObject(i).get("info");
            ClientExportVO vo = new ClientExportVO();
            vo.setId(info.getIntValue("id"));
            vo.setGroupName(info.getString("groupname"));
            vo.setCreateTime(TimeUtil.intMillisToTimeStr(info.getIntValue("createtime")));
            vo.setReceiveTime(TimeUtil.intMillisToTimeStr(info.getIntValue("receivetime")));
            vo.setReceivePeriod(getReceivePeriod(info.getIntValue("createtime"), info.getIntValue("receivetime")));//领取周期
            vo.setCollectorName(info.getString("collectorname"));
            vo.setKzPhone(info.getString("kzphone"));
            vo.setKzWechat(info.getString("kzwechat"));
            vo.setKzQq(info.getString("kzqq"));
            vo.setKzWw(info.getString("kzww"));
            vo.setShopName(info.getString("shopname"));
            vo.setAmount(info.getIntValue("amount"));
            vo.setStayAmount(info.getIntValue("stayamount"));
            vo.setPackageName(getPackageName(info.getIntValue("packagecode"), dicMap));
            vo.setSuccessTime(TimeUtil.intMillisToTimeStr(info.getIntValue("successtime")));
            vo.setSuccessPeriod(getSuccessPeriod(info.getIntValue("createtime"), info.getIntValue("successtime")));
            vo.setAppointName(info.getString("appointname"));
            vo.setSourceName(sourceMap.get(info.getString("sourceid")) == null ? "" : sourceMap.get(info.getString("sourceid")).getSrcName());
            vo.setStatusName(statusMap.get(info.getString("statusid")) == null ? "" : statusMap.get(info.getString("statusid")).getStatusName());
            vo.setChannelName(channelMap.get(info.getString("channelid")) == null ? "" : channelMap.get(info.getString("channelid")).getChannelName());
            vo.setRemark(StringUtil.replaceAllHTML(info.getString("content")));
            vo.setAddress(info.getString("address"));
            vo.setKeyWord(info.getString("keyword"));
            vo.setInvalidLabel(info.getString("invalidlabel"));
            vo.setMemo(info.getString("memo"));
            clientList.add(vo);
        }
        return clientList;
    }

    //获取套系名称
    public static String getPackageName(int packageCode, Map<String, List<DictionaryPO>> dicMap) {
        List<DictionaryPO> list = dicMap.get(StringUtil.camelCaseName(DictionaryConstant.TX_NAME));
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictionaryPO dic : list) {
                if (dic.getDicCode() == packageCode) {
                    return dic.getDicName();
                }
            }
        }
        return "";
    }

    //获取订单周期
    public static String getSuccessPeriod(int begin, int end) {
        if (begin > end || begin == 0 || end == 0) {
            return "";
        }
        return ((end - begin) / (3600 * 24) + 1) + "天";
    }

    //获取领取周期
    public static String getReceivePeriod(int begin, int end) {
        if (begin > end || begin == 0 || end == 0) {
            return "";
        }
        if (begin==end){
            return "0";
        }
        int time = end - begin;
        if (time / 60 <= 1) {
            return "1分钟";
        }
        if (time / 3600 < 1) {
            return (time / 60 + 1) + "分钟";
        }
        if (time / (3600 * 24) < 1) {
            return (time / 3600 + 1) + "小时";
        }
        if (time / (3600 * 24) >= 1) {
            return (time / (3600 * 24) + 1) + "天";
        }
        return "";
    }

}