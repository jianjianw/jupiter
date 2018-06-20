package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mzlion.core.lang.StringUtils;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.http.CrmBaseApi;
import com.qiein.jupiter.util.JsonFmtUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ChannelDao;
import com.qiein.jupiter.web.dao.GoldDataDao;
import com.qiein.jupiter.web.dao.SourceDao;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.dto.GoldCustomerDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.*;
import com.qiein.jupiter.web.entity.vo.GoldCustomerShowVO;
import com.qiein.jupiter.web.entity.vo.GoldCustomerVO;
import com.qiein.jupiter.web.service.GoldDataService;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.http.protocol.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
    private ChannelDao channelDao;
    @Autowired
    private SourceDao sourceDao;
    @Autowired
    private CrmBaseApi crmBaseApi;

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
    public void receiveGoldDataForm(JSONObject jsonObject, StaffPO staffPO) {
        Map<String, Object> reqContent = new HashMap<String, Object>();
        //获取金数据表单模板数据
        GoldFingerPO goldFingerPO = goldDataDao.getGoldFingerByFormIdAndFormName(jsonObject.getString("form"), jsonObject.getString("form_name"));
        //获取字段值
        String[] fieldKeys = StringUtil.isNotEmpty(goldFingerPO.getFieldKey()) ? goldFingerPO.getFieldKey().split(CommonConstant.STR_SEPARATOR) : new String[]{};
        String[] fieldValues = StringUtil.isNotEmpty(goldFingerPO.getFieldValue()) ? goldFingerPO.getFieldValue().split(CommonConstant.STR_SEPARATOR) : new String[]{};
        if (fieldKeys.length != fieldValues.length) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        //表单数据
        JSONObject entry = jsonObject.getJSONObject("entry");
        if (fieldKeys.length != 0 || fieldValues.length != 0){
            for (int i = 0; i < fieldValues.length; i++) {
                if ("kzqq".equalsIgnoreCase(fieldValues[i])) {
                    reqContent.put("kzqq", entry.getString(fieldKeys[i]));
                    continue;
                }
                if ("address".equalsIgnoreCase(fieldValues[i])) {
                    reqContent.put("address", entry.getJSONObject(fieldValues[i]).getString("province")+entry.getJSONObject(fieldValues[i]).getString("city"));
                }
            }
        }
        //获取当前来源
        SourcePO sourcePO = sourceDao.getByIdAndCid(goldFingerPO.getSrcId(), goldFingerPO.getCompanyId());
        if (null == sourcePO) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        reqContent.put("companyid", goldFingerPO.getCompanyId());
        reqContent.put("kzname", entry.getString(goldFingerPO.getKzNameField()));
        reqContent.put("kzphone", entry.getString(goldFingerPO.getKzPhoneField()));
        reqContent.put("channelid", sourcePO.getChannelId());
        reqContent.put("channelname", sourcePO.getChannelName());
        reqContent.put("srcid", goldFingerPO.getSrcId());
        reqContent.put("srctype", sourcePO.getTypeId());
        reqContent.put("sourcename", goldFingerPO.getSrcName());
        reqContent.put("isfilter", goldFingerPO.getIsFilter());
        reqContent.put("adid", goldFingerPO.getAdId());
        reqContent.put("adaddress", goldFingerPO.getAdAddress());
        reqContent.put("typeid", goldFingerPO.getTypeId());
        reqContent.put("zxstyle", goldFingerPO.getZxStyle());
        reqContent.put("remark", goldFingerPO.getMemo());
        reqContent.put("collectorid", goldFingerPO.getCreateorId());
        reqContent.put("collectorName", goldFingerPO.getCreateorName());



        String addRstStr = crmBaseApi.doService(reqContent, "clientAddGoldPlug");
        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);

        if ("100000".equals(jsInfo.getString("code"))) {
            //TODO 是否需要推送客资
        } else {
            throw new RException(jsInfo.getString("msg"));
        }
    }
}
