package com.qiein.jupiter.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.entity.po.StatusPO;
import com.qiein.jupiter.web.entity.vo.ChannelDictVO;
import com.qiein.jupiter.web.entity.vo.ClientExportVO;
import com.qiein.jupiter.web.entity.vo.SourceDictVO;
import com.qiein.jupiter.web.service.ChannelService;
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
                                                                    StatusService statusService, ChannelService channelService) {
        Map<String, SourceDictVO> sourceMap = sourceService.getSourcePageMap(companyId);
        Map<String, StatusPO> statusMap = statusService.getStatusDictMap(companyId);
        Map<String, ChannelDictVO> channelMap = channelService.getChannelDict(companyId);
        List<ClientExportVO> clientList = new ArrayList<ClientExportVO>();
        for (int i = jsArr.size() - 1; i >= 0; i--) {
            JSONObject info = (JSONObject) jsArr.getJSONObject(i).get("info");
            ClientExportVO vo = new ClientExportVO();
            vo.setId(info.getIntValue("id"));
            vo.setGroupName(info.getString("groupname"));
            vo.setCreateTime(TimeUtil.intMillisToTimeStr(info.getIntValue("createtime")));
            vo.setCollectorName(info.getString("collectorname"));
            vo.setKzPhone(info.getString("kzphone"));
            vo.setKzWechat(info.getString("kzwechat"));
            vo.setKzQq(info.getString("kzqq"));
            vo.setKzWw(info.getString("kzww"));
            vo.setShopName(info.getString("shopname"));
            vo.setAppointName(info.getString("appointname"));
            vo.setSourceName(sourceMap.get(info.getString("sourceid")).getSrcName());
            vo.setStatusName(statusMap.get(info.getString("statusid")).getStatusName());
            vo.setChannelName(channelMap.get(info.getString("channelid")).getChannelName());
            vo.setRemark(info.getString("remark"));
            vo.setAddress(info.getString("adaddress"));
            vo.setKeyWord(info.getString("keyword"));
            vo.setInvalidLabel(info.getString("invalidlabel"));
            vo.setMemo(info.getString("memo"));
            clientList.add(vo);
        }
        return clientList;
    }


}