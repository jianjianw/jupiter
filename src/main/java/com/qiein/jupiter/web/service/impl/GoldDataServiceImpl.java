package com.qiein.jupiter.web.service.impl;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mzlion.core.http.IPUtils;
import com.mzlion.core.lang.StringUtils;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.GoldDataConst;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.msg.goeasy.ClientDTO;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.EditCreatorDTO;
import com.qiein.jupiter.web.entity.dto.GoldCustomerDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.entity.vo.GoldCustomerShowVO;
import com.qiein.jupiter.web.entity.vo.GoldCustomerVO;
import com.qiein.jupiter.web.service.GoldDataService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 金手指表单
 * Author xiangliang 2018/6/13
 */
@Service
public class GoldDataServiceImpl implements GoldDataService {

    @Autowired
    private GoldDataDao goldDataDao;
    @Autowired
    private SourceDao sourceDao;
    @Autowired
    private CrmBaseApi crmBaseApi;
    @Autowired
    private GoldTempDao goldTempDao;
    @Autowired
    private NewsDao newsDao;

    /**
     * 添加表单
     *
     * @param goldFingerPO
     */
    public void insert(GoldFingerPO goldFingerPO) {
        List<GoldFingerPO> list = goldDataDao.checkForm(goldFingerPO);
        if (list.size() == 0) {
            goldDataDao.insert(goldFingerPO);
        } else {
            throw new RException(ExceptionEnum.FORM_WAS_IN);
        }
    }

    /**
     * 修改表单
     *
     * @param goldFingerPO
     */
    public void update(GoldFingerPO goldFingerPO) {
        List<GoldFingerPO> list = goldDataDao.checkForm(goldFingerPO);
        if (list.size() == 0) {
            goldDataDao.update(goldFingerPO);
        } else {
            throw new RException(ExceptionEnum.FORM_WAS_IN);
        }
    }

    /**
     * 删除表单
     *
     * @param id
     */
    public void delete(Integer id) {
        goldDataDao.delete(id);
    }

    /**
     * 金数据表单页面显示
     *
     * @param companyId
     * @return
     */
    public List<GoldFingerPO> select(Integer companyId) {
        return goldDataDao.select(companyId);

    }

    /**
     * 管理开关
     *
     * @param goldFingerPO
     */
    public void editOpenOrClose(GoldFingerPO goldFingerPO) {
        goldDataDao.editOpenOrClose(goldFingerPO);
    }

    /**
     * 金数据客资日志
     *
     * @param goldCustomerDTO
     * @return
     */
    public GoldCustomerShowVO goldCustomerSelect(QueryMapDTO queryMapDTO, GoldCustomerDTO goldCustomerDTO) {
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        List<GoldCustomerVO> list = goldDataDao.goldCustomerSelect(goldCustomerDTO);
        GoldCustomerShowVO showVO = new GoldCustomerShowVO();
        showVO.setPageInfo(new PageInfo<>(list));
        GoldFingerPO goldFingerPO = goldDataDao.findForm(goldCustomerDTO.getFormId());
        showVO.setGoldFingerPO(goldFingerPO);
        return showVO;
    }


    /**
     * 金数据接受数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveGoldDataForm(JSONObject jsonObject) {
        Map<String, Object> reqContent = new HashMap<String, Object>();
        //表单数据
        JSONObject entry = jsonObject.getJSONObject("entry");
        String formId = jsonObject.getString("form");
        String formName = jsonObject.getString("formName");
        GoldFingerPO goldFingerPO = goldDataDao.getGoldFingerByFormId(formId);
        String kzPhone = StringUtil.nullToStrTrim(entry.getString(goldFingerPO.getKzPhoneField()));
        if (!RegexUtil.checkMobile(kzPhone)) {
            throw new RException(ExceptionEnum.PHONE_ERROR);
        }
        String kzName = StringUtil.nullToStrTrim(entry.getString(goldFingerPO.getKzNameField()));
        String weChat = StringUtil.nullToStrTrim(entry.getString(goldFingerPO.getKzWechatField()));
        String address = MobileLocationUtil.getPhoneLocation(kzPhone);
        //获取金数据表单模板数据
        if (null == goldFingerPO) {
            throw new RException(ExceptionEnum.FORM_NOT_EXISTS);
        }
        if (NumUtil.isNull(goldFingerPO.getIsShow())) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        if (goldFingerPO.getIsShow().equals(CommonConstant.DEFAULT_ZERO)) {
            return;
        }

        //获取字段值
        String[] fieldKeys = StringUtil.isNotEmpty(goldFingerPO.getFieldKey()) ? goldFingerPO.getFieldKey().split(CommonConstant.STR_SEPARATOR) : new String[]{};
        String[] fieldValues = StringUtil.isNotEmpty(goldFingerPO.getFieldValue()) ? goldFingerPO.getFieldValue().split(CommonConstant.STR_SEPARATOR) : new String[]{};
        if (fieldKeys.length != fieldValues.length) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }

        //备注放入其他信息
        String remark = "<span style=\"color:#FF8533;\">【金数据】</span>";
        StringBuilder sb = new StringBuilder(remark);
        if (fieldKeys.length != 0 || fieldValues.length != 0) {
            for (int i = 0; i < fieldValues.length; i++) {
                if ("kzqq".equalsIgnoreCase(fieldValues[i])) {
                    reqContent.put("kzqq", entry.getString(fieldKeys[i]));
                    continue;
                }
                String value = entry.getString(fieldValues[i]);
                if (entry.get(fieldValues[i]) != null && !"".equals(value)) {
                    if (StringUtil.isNotEmpty(value)) {
                        sb.append(fieldKeys[i] + "：" + StringUtil.nullToStrTrim(value) + "<br/>");
                    }
                }
            }
            if (StringUtil.isNotEmpty(kzName)) {
                sb.append("姓名：").append(kzName).append("<br/>");
            }
            if (StringUtil.isNotEmpty(kzPhone)) {
                sb.append("手机号：").append(kzPhone).append("<br/>");
            }
            if (StringUtil.isNotEmpty(address)) {
                sb.append("归属地：").append(address).append("<br/>");
            }
            if (StringUtil.isNotEmpty(weChat)) {
                sb.append("微信号：").append(weChat).append("<br/>");
            }
            if (StringUtil.isNotEmpty(formId)) {
                sb.append("表单号：").append(formId).append("<br/>");
            }
            if (StringUtil.isNotEmpty(formName)) {
                sb.append("表单名称：").append(formName).append("<br/>");
            }
        }


        //获取当前来源
        SourcePO sourcePO = sourceDao.getByIdAndCid(goldFingerPO.getSrcId(), goldFingerPO.getCompanyId());
        if (null == sourcePO) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        reqContent.put("companyid", goldFingerPO.getCompanyId());
        reqContent.put("kzname", kzName);
        reqContent.put("kzphone", kzPhone);
        reqContent.put("channelid", sourcePO.getChannelId());
        reqContent.put("channelname", sourcePO.getChannelName());
        reqContent.put("sourceid", goldFingerPO.getSrcId());
        reqContent.put("srctype", sourcePO.getTypeId());
        reqContent.put("sourcename", goldFingerPO.getSrcName());
        reqContent.put("isfilter", goldFingerPO.getIsFilter());
        reqContent.put("adid", goldFingerPO.getAdId());
        reqContent.put("adaddress", goldFingerPO.getAdAddress());
        reqContent.put("typeid", goldFingerPO.getTypeId());
        reqContent.put("zxstyle", goldFingerPO.getZxStyle());
        reqContent.put("collectorid", goldFingerPO.getCreateorId());
        reqContent.put("collectorname", goldFingerPO.getCreateorName());
        reqContent.put("address", address);
        reqContent.put("remark", sb.toString());


        //插入记录
        GoldTempPO goldTempPO = new GoldTempPO();
        goldTempPO.setFormId(goldFingerPO.getFormId());
        goldTempPO.setFormName(goldFingerPO.getFormName());
        goldTempPO.setSrcId(goldFingerPO.getSrcId());
        goldTempPO.setSrcName(goldFingerPO.getSrcName());
        goldTempPO.setTypeId(goldFingerPO.getTypeId());
        goldTempPO.setTypeName(goldFingerPO.getTypeName());
        goldTempPO.setCollecterId(goldFingerPO.getCreateorId());
        goldTempPO.setCollecterName(goldFingerPO.getCreateorName());
        goldTempPO.setAdId(goldFingerPO.getAdId());
        goldTempPO.setAdAddress(goldFingerPO.getAdAddress());
        goldTempPO.setKzName(kzName);
        goldTempPO.setKzPhone(kzPhone);
        goldTempPO.setCompanyId(goldFingerPO.getCompanyId());
        goldTempPO.setAddress(address);
        goldTempPO.setWechat(weChat);
        goldTempPO.setRemark(jsonObject.toString());

        goldTempDao.insert(goldTempPO);

        //重复拦截
        GoldTempPO goldTemp = goldTempDao.getByKzNameOrKzPhoneOrKzWechat(formId, kzPhone);
        if (null != goldTemp) {
            goldTempPO.setStatusId(GoldDataConst.REPEATED_SCREEN);
            goldTempDao.update(goldTempPO);
            return;
        }

        String addRstStr = crmBaseApi.doService(reqContent, "clientAddGoldPlug");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);


        if ("100000".equals(jsInfo.getString("code"))) {
            //更新状态
            goldTempPO.setStatusId(GoldDataConst.IN_FILTER);
            goldTempDao.update(goldTempPO);
            //发送消息
            ClientDTO info = new ClientDTO();
            info.setKzName(goldTempPO.getKzName());
            info.setKzPhone(goldTempPO.getKzPhone());
            info.setKzWeChat(weChat);
            info.setSrcName(goldFingerPO.getSrcName());
            info.setChannelName(sourcePO.getChannelName());
            GoEasyUtil.pushGoldDataKz(goldFingerPO.getCompanyId(), goldFingerPO.getCreateorId(), info, newsDao);
        } else if ("130019".equals(jsInfo.getString("code"))) {
            goldTempPO.setStatusId(GoldDataConst.HAVA_ENTERED);
            goldTempDao.update(goldTempPO);
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }


    /**
     * 筛选
     *
     * @param goldTempPO
     */
    public void addkzByGoldTemp(GoldTempPO goldTempPO) {
        Map<String, Object> reqContent = new HashMap<String, Object>();
        //获取金数据表单模板数据
        GoldFingerPO goldFingerPO = goldDataDao.getGoldFingerByFormId(goldTempPO.getFormId());
        //获取字段值
        String[] fieldKeys = StringUtil.isNotEmpty(goldFingerPO.getFieldKey()) ? goldFingerPO.getFieldKey().split(CommonConstant.STR_SEPARATOR) : new String[]{};
        String[] fieldValues = StringUtil.isNotEmpty(goldFingerPO.getFieldValue()) ? goldFingerPO.getFieldValue().split(CommonConstant.STR_SEPARATOR) : new String[]{};
        if (fieldKeys.length != fieldValues.length) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        //表单数据
        /*if (fieldKeys.length != 0 || fieldValues.length != 0){
            for (int i = 0; i < fieldValues.length; i++) {
                if ("kzqq".equalsIgnoreCase(fieldValues[i])) {
                    reqContent.put("kzqq", entry.getString(fieldKeys[i]));
                    continue;
                }
                if ("address".equalsIgnoreCase(fieldValues[i])) {
                    reqContent.put("address", entry.getJSONObject(fieldKeys[i]).getString("province")+entry.getJSONObject(fieldKeys[i]).getString("city"));
                }
            }
        }*/
        //获取当前来源
        SourcePO sourcePO = sourceDao.getByIdAndCid(goldFingerPO.getSrcId(), goldFingerPO.getCompanyId());
        if (null == sourcePO) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        reqContent.put("companyid", goldFingerPO.getCompanyId());
        reqContent.put("kzname", goldTempPO.getKzName());
        reqContent.put("kzphone", goldTempPO.getKzPhone());
        reqContent.put("channelid", sourcePO.getChannelId());
        reqContent.put("channelname", sourcePO.getChannelName());
        reqContent.put("sourceid", goldFingerPO.getSrcId());
        reqContent.put("srctype", sourcePO.getTypeId());
        reqContent.put("sourcename", goldFingerPO.getSrcName());
        reqContent.put("adid", goldFingerPO.getAdId());
        reqContent.put("adaddress", goldFingerPO.getAdAddress());
        reqContent.put("typeid", goldFingerPO.getTypeId());
        reqContent.put("zxstyle", goldFingerPO.getZxStyle());
        reqContent.put("remark", goldFingerPO.getMemo());
        reqContent.put("collectorid", goldFingerPO.getCreateorId());
        reqContent.put("collectorname", goldFingerPO.getCreateorName());
        String addRstStr = crmBaseApi.doService(reqContent, "clientAddGoldPlug");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);

        if ("100000".equals(jsInfo.getString("code"))) {
            goldTempPO.setStatusId(GoldDataConst.IN_FILTER);
            goldTempDao.update(goldTempPO);
        } else if ("130019".equals(jsInfo.getString("code"))) {
            goldTempPO.setStatusId(GoldDataConst.HAVA_ENTERED);
            goldTempDao.update(goldTempPO);
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }

    /**
     * 修改表单创建者
     *
     * @param editCreatorDTO
     */
    public void editFormCreateor(EditCreatorDTO editCreatorDTO) {
        goldDataDao.editFormCreateor(editCreatorDTO);
    }
}
