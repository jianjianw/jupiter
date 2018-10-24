package com.qiein.jupiter.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.constant.ClientConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.constant.GoldDataConst;
import com.qiein.jupiter.enums.GoldDataStatusEnum;
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
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.entity.vo.GoldCustomerShowVO;
import com.qiein.jupiter.web.entity.vo.GoldCustomerVO;
import com.qiein.jupiter.web.repository.CheckClientRepeatDao;
import com.qiein.jupiter.web.repository.ClientAddDao;
import com.qiein.jupiter.web.service.ApolloService;
import com.qiein.jupiter.web.service.GoldDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private DictionaryDao dictionaryDao;
    @Autowired
    private ClientLogDao clientLogDao;
    @Autowired
    private ClientBlackListDao clientBlackListDao;
    @Autowired
    private ApolloService apolloService;
    @Autowired
    private ClientAddDao clientAddDao;
    @Autowired
    private CheckClientRepeatDao checkClientRepeatDao;

    /**
     * 添加表单
     *
     * @param goldFingerPO
     */
    public void insert(GoldFingerPO goldFingerPO) {
        //从阿波罗 获取地址
        String crmUrlByCidFromApollo = apolloService.getCrmUrlByCidFromApollo(goldFingerPO.getCompanyId());
        String requestURI = crmUrlByCidFromApollo + "gold_data/receive_gold_data_form";
        goldFingerPO.setPostURL(requestURI);
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
        //从阿波罗 获取地址
        String crmUrlByCidFromApollo = apolloService.getCrmUrlByCidFromApollo(goldFingerPO.getCompanyId());
        String requestURI = crmUrlByCidFromApollo + "gold_data/receive_gold_data_form";
        goldFingerPO.setPostURL(requestURI);
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
    public PageInfo<GoldFingerPO> select(int companyId, int pageNum, int pageSize, String formId, String staffIds, String srcIds) {
        PageHelper.startPage(pageNum, pageSize);
        List<Integer> srcList = new ArrayList<>();
        List<Integer> staffList = new ArrayList<>();
        if (!StringUtil.isEmpty(staffIds)) {
            for (String staff : staffIds.split(CommonConstant.STR_SEPARATOR)) {
                staffList.add(Integer.parseInt(staff));
            }
        }
        if (!StringUtil.isEmpty(srcIds)) {
            for (String src : srcIds.split(CommonConstant.STR_SEPARATOR)) {
                srcList.add(Integer.parseInt(src));
            }
        }
        List<GoldFingerPO> select = goldDataDao.select(companyId, formId, srcList, staffList);
        return new PageInfo<>(select);

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
        for (GoldCustomerVO goldCustomerVO : list) {
            goldCustomerVO.setStatus(GoldDataStatusEnum.getGoldStatusDesc(goldCustomerVO.getStatusId()));
        }
        GoldCustomerShowVO showVO = new GoldCustomerShowVO();
        showVO.setPageInfo(new PageInfo<>(list));
        GoldFingerPO goldFingerPO = goldDataDao.findForm(goldCustomerDTO.getFormId(), goldCustomerDTO.getCompanyId());
        showVO.setGoldFingerPO(goldFingerPO);
        return showVO;
    }


    /**
     * 金数据接受数据
     */
    @Override
//    @Transactional(rollbackFor = Exception.class)
    public void receiveGoldDataForm(JSONObject jsonObject) {
//        Map<String, Object> reqContent = new HashMap<>();
        ClientVO clientVO = new ClientVO();
        //表单数据
        JSONObject entry = jsonObject.getJSONObject("entry");
        String formId = jsonObject.getString("form");
        String formName = jsonObject.getString("formName");
        GoldFingerPO goldFingerPO = goldDataDao.getGoldFingerByFormId(formId);
        String ip = StringUtil.nullToStrTrim(entry.getString("info_remote_ip"));
        //获取金数据表单模板数据
//        if (null == goldFingerPO) {
//            throw new RException(ExceptionEnum.FORM_NOT_EXISTS);
//        }
        String kzPhone = StringUtil.nullToStrTrim(entry.getString(goldFingerPO.getKzPhoneField()));
//        if (!RegexUtil.checkMobile(kzPhone)) {
//            throw new RException(ExceptionEnum.PHONE_ERROR);
//        }
        String kzName = StringUtil.nullToStrTrim(entry.getString(goldFingerPO.getKzNameField()));
        String weChat = StringUtil.nullToStrTrim(entry.getString(goldFingerPO.getKzWechatField()));
        List<BlackListPO> list = clientBlackListDao.checkBlackList(goldFingerPO.getCompanyId(), kzPhone, null, null, weChat);
        if (!list.isEmpty()) {
            String ids = CommonConstant.NULL_STR;
            for (BlackListPO blackListPO : list) {
                ids += blackListPO.getId() + CommonConstant.STR_SEPARATOR;
            }
            ids = ids.substring(0, ids.lastIndexOf(CommonConstant.STR_SEPARATOR));
            clientBlackListDao.addCount(ids);
            throw new RException(ExceptionEnum.KZ_IN_BLACK_LIST);

        }
        String address = MobileLocationUtil.getPhoneLocation(kzPhone);
        String oldKzName = StringUtil.nullToStrTrim(entry.getString(goldFingerPO.getOldKzName()));
        String oldKzPhone = StringUtil.nullToStrTrim(entry.getString(goldFingerPO.getOldKzPhone()));
        //获取字段值
        String[] fieldKeys = StringUtil.isNotEmpty(goldFingerPO.getFieldKey()) ? goldFingerPO.getFieldKey().split(CommonConstant.STR_SEPARATOR) : new String[]{};
        String[] fieldValues = StringUtil.isNotEmpty(goldFingerPO.getFieldValue()) ? goldFingerPO.getFieldValue().split(CommonConstant.STR_SEPARATOR) : new String[]{};
//        if (fieldKeys.length != fieldValues.length) {
//            throw new RException(ExceptionEnum.GOLD_DATA_ARR_LENGTH_ERROR);
//        }

        //备注放入其他信息
        String remark = "<span style=\"color:#FF8533;\">【金数据】</span>";
        StringBuilder sb = new StringBuilder(remark);
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
        if (fieldKeys.length != 0) {
            for (int i = 0; i < fieldValues.length; i++) {
                if ("kzqq".equalsIgnoreCase(fieldValues[i])) {
//                    reqContent.put("kzqq", entry.getString(fieldKeys[i]));
                    clientVO.setKzQq(entry.getString(fieldKeys[i]));
                    continue;
                }
                String value = entry.getString(fieldValues[i]);
                if (entry.get(fieldValues[i]) != null && !"".equals(value)) {
                    if (StringUtil.isNotEmpty(value)) {
                        sb.append(fieldKeys[i]).append("：")
                                .append(StringUtil.nullToStrTrim(value))
                                .append("<br/>");
                    }
                }
            }
        }


        //获取当前来源
        SourcePO sourcePO = sourceDao.getByIdAndCid(goldFingerPO.getSrcId(), goldFingerPO.getCompanyId());
//        if (null == sourcePO) {
//            throw new RException(ExceptionEnum.SOURCE_NOT_FOUND);
//        }
//        reqContent.put("companyid", goldFingerPO.getCompanyId());
        clientVO.setCompanyId(goldFingerPO.getCompanyId());
//        reqContent.put("kzname", kzName);
        clientVO.setKzName(kzName);
//        reqContent.put("kzphone", kzPhone);
        clientVO.setKzPhone(kzPhone);
//        reqContent.put("kzwechat", weChat);
        clientVO.setKzWechat(weChat);
//        reqContent.put("channelid", sourcePO.getChannelId());
        clientVO.setChannelId(sourcePO.getChannelId());
//        reqContent.put("channelname", sourcePO.getChannelName());
        clientVO.setChannelName(sourcePO.getChannelName());
//        reqContent.put("sourceid", goldFingerPO.getSrcId());
        clientVO.setSourceId(goldFingerPO.getSrcId());
//        reqContent.put("srctype", sourcePO.getTypeId());
        clientVO.setSrcType(sourcePO.getTypeId());
//        reqContent.put("sourcename", goldFingerPO.getSrcName());
        clientVO.setSourceName(goldFingerPO.getSrcName());
        //更新状态
//        reqContent.put("isfilter", goldFingerPO.getIsFilter());
        clientVO.setFilterFlag(goldFingerPO.getIsFilter());
//        reqContent.put("adid", goldFingerPO.getAdId());
        clientVO.setAdId(goldFingerPO.getAdId());
//        reqContent.put("adaddress", goldFingerPO.getAdAddress());
        clientVO.setAddress(goldFingerPO.getAdAddress());
//        reqContent.put("typeid", goldFingerPO.getTypeId());
        clientVO.setTypeId(goldFingerPO.getTypeId());
        DictionaryPO dictionaryPO = dictionaryDao.getDicByTypeAndName(goldFingerPO.getCompanyId(), DictionaryConstant.ZX_STYLE, goldFingerPO.getZxStyle());
//        reqContent.put("zxstyle", dictionaryPO.getDicCode());
        clientVO.setZxStyle(dictionaryPO.getDicCode());
//        reqContent.put("collectorid", goldFingerPO.getCreateorId());
        clientVO.setCollectorId(goldFingerPO.getCreateorId());
//        reqContent.put("collectorname", goldFingerPO.getCreateorName());
        clientVO.setCollectorName(goldFingerPO.getCreateorName());
//        reqContent.put("address", address);
        clientVO.setAddress(address);
//        reqContent.put("remark", sb.toString());
        clientVO.setRemark(sb.toString());
//        reqContent.put("oldkzname", oldKzName);
        clientVO.setOldKzName(oldKzName);
//        reqContent.put("oldkzphone", oldKzPhone);
        clientVO.setOldKzPhone(oldKzPhone);


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
        goldTempPO.setIp(StringUtil.isEmpty(ip) ? "" : ip);
        goldTempPO.setIpAddress(StringUtil.isEmpty(ip) ? "" : HttpUtil.getIpLocation(ip));
        goldTempDao.insert(goldTempPO);


        //重复拦截
//        GoldTempPO goldTemp = goldTempDao.getByKzNameOrKzPhoneOrKzWechat(formId, kzPhone);
//        if (null != goldTemp) {
//            goldTempPO.setStatusId(GoldDataConst.REPEATED_SCREEN);
//            goldTempDao.update(goldTempPO);
//            return;
//        }

//        String addRstStr = crmBaseApi.doService(reqContent, "clientAddGoldPlug");
//        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);

        //查重
        try {
            checkClientRepeatDao.check(clientVO);
        } catch (RException e) {
            e.printStackTrace();
            goldTempPO.setStatusId(GoldDataConst.REPEATED_SCREEN);
            goldTempDao.update(goldTempPO);
        }
        //新增客资
        try {
            ClientVO clientVO1 = clientAddDao.addClientInfo(clientVO);
            //更新状态(录入成功)
            goldTempPO.setStatusId(GoldDataConst.IN_SUCCESS);
            goldTempDao.update(goldTempPO);
            //发送消息
            if (goldFingerPO.getPushNews()) {
                ClientDTO info = new ClientDTO();
                info.setKzId(clientVO1.getKzId());
                info.setKzName(goldTempPO.getKzName());
                info.setKzPhone(goldTempPO.getKzPhone());
                info.setKzWeChat(weChat);
                info.setSrcName(goldFingerPO.getSrcName());
                info.setChannelName(sourcePO.getChannelName());
                GoEasyUtil.pushGoldDataKz(goldFingerPO.getCompanyId(), goldFingerPO.getCreateorId(), info, newsDao, staffDao);
            }
            //客资日志
            ClientLogPO clientLogPO = new ClientLogPO();
            clientLogPO.setCompanyId(goldFingerPO.getCompanyId());
            clientLogPO.setLogType(ClientConst.ALLOT_LOG_STATUS_YES);
            clientLogPO.setMemo("金数据录入");
            clientLogPO.setKzId(clientVO1.getKzId());
            clientLogPO.setOperaId(goldFingerPO.getCreateorId());
            StaffPO staffPO = staffDao.getByIdAndCid(goldFingerPO.getCreateorId(), goldFingerPO.getCompanyId());
            clientLogPO.setOperaName(staffPO.getNickName());
            clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(goldFingerPO.getCompanyId()), clientLogPO);
        } catch (Exception e) {
            e.printStackTrace();
            goldTempPO.setStatusId(GoldDataConst.IN_FAIL);
            goldTempDao.update(goldTempPO);
        }
//        String kzId = JsonFmtUtil.strContentToJsonObj(addRstStr).getString("kzid");


//
//        if ("100000".equals(jsInfo.getString("code"))) {
//            String kzId = JsonFmtUtil.strContentToJsonObj(addRstStr).getString("kzid");
//            //更新状态(录入成功)
//            goldTempPO.setStatusId(GoldDataConst.IN_SUCCESS);
//            goldTempDao.update(goldTempPO);
//            //发送消息
//            if (goldFingerPO.getPushNews()) {
//                ClientDTO info = new ClientDTO();
//                info.setKzId(kzId);
//                info.setKzName(goldTempPO.getKzName());
//                info.setKzPhone(goldTempPO.getKzPhone());
//                info.setKzWeChat(weChat);
//                info.setSrcName(goldFingerPO.getSrcName());
//                info.setChannelName(sourcePO.getChannelName());
//                GoEasyUtil.pushGoldDataKz(goldFingerPO.getCompanyId(), goldFingerPO.getCreateorId(), info, newsDao, staffDao);
//            }
//            //客资日志
//            ClientLogPO clientLogPO = new ClientLogPO();
//            clientLogPO.setCompanyId(goldFingerPO.getCompanyId());
//            clientLogPO.setLogType(ClientConst.ALLOT_LOG_STATUS_YES);
//            clientLogPO.setMemo("金数据录入");
//            clientLogPO.setKzId(kzId);
//            clientLogPO.setOperaId(goldFingerPO.getCreateorId());
//            StaffPO staffPO = staffDao.getByIdAndCid(goldFingerPO.getCreateorId(), goldFingerPO.getCompanyId());
//            clientLogPO.setOperaName(staffPO.getNickName());
//            clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(goldFingerPO.getCompanyId()), clientLogPO);
//        } else if ("130019".equals(jsInfo.getString("code"))) {
//            goldTempPO.setStatusId(GoldDataConst.REPEATED_SCREEN);
//            goldTempDao.update(goldTempPO);
//        } else {
//            goldTempPO.setStatusId(GoldDataConst.IN_FAIL);
//            goldTempDao.update(goldTempPO);
//        }
    }


    /**
     * 筛选
     *
     * @param goldTempPO
     */
    public void addkzByGoldTemp(GoldTempPO goldTempPO) {
//        Map<String, Object> reqContent = new HashMap<String, Object>();
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
        ClientVO clientVO = new ClientVO();
        clientVO.setCompanyId(goldFingerPO.getCompanyId());
//        reqContent.put("companyid", goldFingerPO.getCompanyId());
//        reqContent.put("kzname", goldTempPO.getKzName());
        clientVO.setKzName(goldTempPO.getKzName());
//        reqContent.put("kzphone", goldTempPO.getKzPhone());
        clientVO.setKzPhone(goldTempPO.getKzPhone());
//        reqContent.put("channelid", sourcePO.getChannelId());
        clientVO.setChannelId(sourcePO.getChannelId());
//        reqContent.put("channelname", sourcePO.getChannelName());
        clientVO.setKzPhone(sourcePO.getChannelName());
//        reqContent.put("sourceid", goldFingerPO.getSrcId());
        clientVO.setSourceId(goldFingerPO.getSrcId());
//        reqContent.put("srctype", sourcePO.getTypeId());
        clientVO.setSrcType(sourcePO.getTypeId());
//        reqContent.put("sourcename", goldFingerPO.getSrcName());
        clientVO.setSourceName(goldFingerPO.getSrcName());
//        reqContent.put("adid", goldFingerPO.getAdId());
        clientVO.setAdId(goldFingerPO.getAdId());
//        reqContent.put("adaddress", goldFingerPO.getAdAddress());
        clientVO.setAddress(goldFingerPO.getAdAddress());
//        reqContent.put("typeid", goldFingerPO.getTypeId());
        clientVO.setTypeId(goldFingerPO.getTypeId());
        DictionaryPO dictionaryPO = dictionaryDao.getDicByTypeAndName(goldFingerPO.getCompanyId(), DictionaryConstant.ZX_STYLE, goldFingerPO.getZxStyle());
//        reqContent.put("zxstyle", dictionaryPO.getDicType());
        clientVO.setZxStyle(dictionaryPO.getDicCode());
//        reqContent.put("remark", goldFingerPO.getMemo());
        clientVO.setRemark(goldFingerPO.getMemo());
//        reqContent.put("collectorid", goldFingerPO.getCreateorId());
        clientVO.setCollectorId(goldFingerPO.getCreateorId());
//        reqContent.put("collectorname", goldFingerPO.getCreateorName());
        clientVO.setCollectorName(goldFingerPO.getCreateorName());
//        String addRstStr = crmBaseApi.doService(reqContent, "clientAddGoldPlug");
//        JSONObject jsInfo = JsonFmtUtil.strInfoToJsonObj(addRstStr);
        //查重
        try {
            checkClientRepeatDao.check(clientVO);
        } catch (RException e) {
            e.printStackTrace();
            goldTempPO.setStatusId(GoldDataConst.REPEATED_SCREEN);
            goldTempDao.update(goldTempPO);
            //重复录入时返回
            return;
        }
        //新增客资
        try {
            clientAddDao.addClientInfo(clientVO);
        } catch (Exception e) {
            e.printStackTrace();
            goldTempPO.setStatusId(GoldDataConst.IN_FAIL);
            goldTempDao.update(goldTempPO);
        }
        goldTempPO.setStatusId(GoldDataConst.IN_SUCCESS);
        goldTempDao.update(goldTempPO);

//        if ("100000".equals(jsInfo.getString("code"))) {
//            goldTempPO.setStatusId(GoldDataConst.IN_SUCCESS);
//            goldTempDao.update(goldTempPO);
//        } else if ("130019".equals(jsInfo.getString("code"))) {
//            goldTempPO.setStatusId(GoldDataConst.REPEATED_SCREEN);
//            goldTempDao.update(goldTempPO);
//        } else {
//            goldTempPO.setStatusId(GoldDataConst.IN_FAIL);
//            goldTempDao.update(goldTempPO);
//        }
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
