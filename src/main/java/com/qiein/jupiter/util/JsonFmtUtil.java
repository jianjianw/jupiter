package com.qiein.jupiter.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ClientConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.constant.PmsConstant;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.PermissionDao;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.po.PermissionPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.po.StatusPO;
import com.qiein.jupiter.web.entity.vo.ChannelDictVO;
import com.qiein.jupiter.web.entity.vo.ClientExportVO;
import com.qiein.jupiter.web.entity.vo.CompanyVO;
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
    public static List<ClientExportVO> jsonArrToClientExportVO(JSONArray jsArr, StaffPO staffPO, SourceService sourceService,
                                                               StatusService statusService, ChannelService channelService,
                                                               DictionaryService dictionaryService, CompanyVO companyVO, PermissionDao permissionDao) {
        Map<String, SourceDictVO> sourceMap = sourceService.getSourcePageMap(staffPO.getCompanyId());
        Map<String, StatusPO> statusMap = statusService.getStatusDictMap(staffPO.getCompanyId());
        Map<String, ChannelDictVO> channelMap = channelService.getChannelDict(staffPO.getCompanyId());
        Map<String, List<DictionaryPO>> dicMap = dictionaryService.getDictMapByCid(staffPO.getCompanyId());
        //默认只看脱敏数据
        boolean blind = true;
        //如果没有开启，就看显示全部
        if (companyVO != null && !companyVO.isNotSelfBlind()) {
            blind = false;
        }
        if (blind) {
            // 如果开启，但是又查看全部权限，，查看全部数据
            List<PermissionPO> permissionPOList = permissionDao.getStaffPermission(staffPO.getId(), staffPO.getCompanyId());
            for (PermissionPO pms : permissionPOList) {
                if (pms.getPermissionId() == PmsConstant.SEE_BLIND) {
                    blind = false;
                    break;
                }
            }
        }
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
            //查看全部
            if (!blind || (NumUtil.isNotNull(info.getIntValue("collectorid")) && info.getIntValue("collectorid") == staffPO.getId())
                    || (NumUtil.isNotNull(info.getIntValue("appointorid")) && info.getIntValue("appointorid") == staffPO.getId())
                    || (NumUtil.isNotNull(info.getIntValue("promotorid")) && info.getIntValue("promotorid") == staffPO.getId())
                    || (NumUtil.isNotNull(info.getIntValue("receptorid")) && info.getIntValue("receptorid") == staffPO.getId())) {
                vo.setKzName(info.getString("kzname"));
                vo.setKzPhone(info.getString("kzphone"));
                vo.setKzWechat(info.getString("kzwechat"));
                vo.setKzQq(info.getString("kzqq"));
                vo.setKzWw(info.getString("kzww"));
                vo.setRemark(StringUtil.replaceAllHTML(info.getString("content")));
                vo.setMemo(info.getString("memo"));
            } else {
                //查看脱敏数据
                vo.setKzName(getBlindString(info.getString("kzname")));
                vo.setKzPhone(getBlindString(info.getString("kzphone")));
                vo.setKzWechat(getBlindString(info.getString("kzwechat")));
                vo.setKzQq(getBlindString(info.getString("kzqq")));
                vo.setKzWw(getBlindString(info.getString("kzww")));
                vo.setRemark(getBlindString(StringUtil.replaceAllHTML(info.getString("content"))));
                vo.setMemo(getBlindString(info.getString("memo")));
            }
            vo.setYxLevel(getDicNameByCode(info.getIntValue("yxlevel"), DictionaryConstant.YX_RANK, dicMap));
            vo.setShopName(info.getString("shopname"));
            vo.setAmount(info.getIntValue("amount"));
            vo.setStayAmount(info.getIntValue("stayamount"));
            vo.setPackageName(getDicNameByCode(info.getIntValue("packagecode"), DictionaryConstant.TX_NAME, dicMap));
            vo.setComeShopTime(TimeUtil.intMillisToTimeStr(info.getIntValue("comeshoptime")));
            vo.setSuccessTime(TimeUtil.intMillisToTimeStr(info.getIntValue("successtime")));
            vo.setSuccessPeriod(getSuccessPeriod(info.getIntValue("createtime"), info.getIntValue("successtime")));
            vo.setAppointName(info.getString("appointname"));
            vo.setSourceName(sourceMap.get(info.getString("sourceid")) == null ? "" : sourceMap.get(info.getString("sourceid")).getSrcName());
            vo.setStatusName(statusMap.get(info.getString("statusid")) == null ? "" : statusMap.get(info.getString("statusid")).getStatusName());
            vo.setChannelName(channelMap.get(info.getString("channelid")) == null ? "" : channelMap.get(info.getString("channelid")).getChannelName());
            vo.setProvince(getProvince(info.getString("address")));
            vo.setCity(getCity(info.getString("address")));
            vo.setKeyWord(info.getString("keyword"));
            vo.setInvalidLabel(info.getString("invalidlabel"));
            vo.setAdId(info.getString("adid"));
            vo.setPromoterName(info.getString("promotername"));
            //设置性别
            if(StringUtil.isNotEmpty(info.getString("sex")) && ClientConst.KZ_SEX_UNKNOWN_NUM.equalsIgnoreCase(info.getString("sex"))){
                vo.setKzSex(ClientConst.KZ_SEX_UNKNOWN);
            }else if (StringUtil.isNotEmpty(info.getString("sex")) && ClientConst.KZ_SEX_BODY_NUM.equalsIgnoreCase(info.getString("sex"))){
                vo.setKzSex(ClientConst.KZ_SEX_BODY);
            }else if(StringUtil.isNotEmpty(info.getString("sex")) && ClientConst.KZ_SEX_GIRL_NUM.equalsIgnoreCase(info.getString("sex"))){
                vo.setKzSex(ClientConst.KZ_SEX_GIRL);
            }else{
                vo.setKzSex(ClientConst.KZ_SEX_UNKNOWN);
            }
            clientList.add(vo);
        }
        return clientList;
    }

    //获取脱敏后的数据
    public static String getBlindString(String str) {
        if (StringUtil.isEmpty(str)) {
            return "";
        }
        if (str.length() == 1) {
            return str + "*";
        }
        if (str.length() > 1) {
            return str.substring(0, 1) + "******" + str.substring(str.length() - 1, str.length());
        }
        return "";
    }

    //获取省份
    public static String getProvince(String address) {
        if (StringUtil.isEmpty(address)) {
            return "";
        }
        String[] arr = address.split(CommonConstant.STR_SEPARATOR);
        if (arr.length >= 1) {
            return arr[0];
        }
        return "";
    }

    //获取城市
    public static String getCity(String address) {
        if (StringUtil.isEmpty(address)) {
            return "";
        }
        String[] arr = address.split(CommonConstant.STR_SEPARATOR);
        if (arr.length >= 2) {
            return arr[1];
        }
        return "";
    }

    /**
     * 字典表，根据编码获取中文描述
     *
     * @param code
     * @param type
     * @param dicMap
     * @return
     */
    public static String getDicNameByCode(int code, String type, Map<String, List<DictionaryPO>> dicMap) {
        if (NumUtil.isNull(code) || dicMap.isEmpty()) {
            return "";
        }
        List<DictionaryPO> list = dicMap.get(StringUtil.camelCaseName(type));
        if (CollectionUtils.isNotEmpty(list)) {
            for (DictionaryPO dic : list) {
                if (dic.getDicCode() == code) {
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
        if (begin == end) {
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