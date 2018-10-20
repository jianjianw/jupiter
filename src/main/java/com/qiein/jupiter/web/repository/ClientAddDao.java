package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ClientConst;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.enums.AddTypeEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客资新增
 *
 * @Author: shiTao
 */
@Repository
public class ClientAddDao {

    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    /**
     * Pc端录入 电商客资
     *
     * @return
     */
    public String addPcDsClientInfo() {
        return null;
    }

    /**
     * PC端录入转介绍客资
     */
    @Transactional
    public void addPcZjsClientInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("nickName", "小明1");
        paramMap.put("userName", "小明1");
        paramMap.put("phone", 123123123);

        namedJdbc.update("INSERT INTO  test (NICKNAME,PHONE,USERNAME ) VALUES ( :nickName,:phone,:userName)", paramMap);
        addGoldDataClientInfo();

    }

    /**
     * 金数据客资录入
     */
//    @Transactional
    public void addGoldDataClientInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("nickName", "小明2");
        paramMap.put("userName", "小明2");
        paramMap.put("phone", 123123123);

        namedJdbc.update("INSERT INTO  test (NICKNAME,PHONE,USERNAME ) VALUES ( :nickName,:phone,:userName)", paramMap);

        throw new RException();
    }


    /**
     * 录入门市客资
     */
    public void addPcMdClientInfo() {

    }


    /**
     * 钉钉手机录入客资
     */
    @Transactional
    public String addDingClientInfo(ClientVO clientVO) {
        clientVO.setKzId(StringUtil.getRandom());
        //预处理
        initClientInfo(clientVO);
        //新增客资信息
        int id = doAddClintInfo(clientVO);
        //更新letterId
        updateLetterId(id, clientVO.getCompanyId());
        //新增客资详情
        doAddClientDetail(clientVO);
        //新增备注
        doAddRemark(clientVO);
        //新增日志
        doAddClientLog(AddTypeEnum.MOBILE.getTypeId(), clientVO);
        return clientVO.getKzId();
    }


    /**
     * info表新增
     */
    private int doAddClintInfo(ClientVO clientVO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO hm_crm_client_info ");
        sql.append("  ( KZID, TYPEID, CLASSID, STATUSID, KZNAME, KZPHONE, KZWECHAT, KZQQ, KZWW, SEX,  ");
        sql.append(" CHANNELID, SOURCEID, COLLECTORID, APPOINTORID, PROMOTORID, SHOPID, ALLOTTYPE, COMPANYID, SRCTYPE, GROUPID, CREATETIME ");
        sql.append(" ");
        if (ClientStatusConst.BE_HAVE_MAKE_ORDER == clientVO.getStatusId()) {
            sql.append(" , RECEIVETIME ");
        }
        sql.append(" , UPDATETIME ) ");
        sql.append(" VALUES( :kzId, :typeId, :classId, :statusId, :kzName, :kzPhone, :kzWechat, :kzQq, :kzWw ,:sex ,");
        sql.append(" :channelId , :sourceId, :collectorId , :appointId, :promotorId, :shopId, :allotType, :companyId, :srcType, :groupId, UNIX_TIMESTAMP(NOW())");
        if (ClientStatusConst.BE_HAVE_MAKE_ORDER == clientVO.getStatusId()) {
            sql.append(" , UNIX_TIMESTAMP(NOW()) ");
        }
        sql.append(" , UNIX_TIMESTAMP(NOW()) ) ");

        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(clientVO);
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        namedJdbc.update(sql.toString(), sqlParameterSource, generatedKeyHolder);
        return generatedKeyHolder.getKey().intValue();
    }

    /**
     * 新增客资详情
     */
    private void doAddClientDetail(ClientVO clientVO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO hm_crm_client_detail ");
        sql.append(" ( KZID, COLLECTORNAME, PROMOTERNAME, APPOINTNAME, SHOPNAME, MEMO, TALKIMG, ZXSTYLE, ADADDRESS,  ");
        sql.append(" ADID, ADDRESS, GROUPNAME, KEYWORD, COMPANYID, CREATETIME , MATENAME, MATEPHONE, MATEWECHAT, MATEQQ,");
        sql.append(" MARRYTIME, YPTIME, OLDKZNAME, OLDKZPHONE, YSRANGE , YXLEVEL  ) ");
        sql.append(" VALUES ( :kzId, :collectorName, :promoterName, :appointName, :shopName, :memo, :talkImg, :zxStyle, :adAddress,");
        sql.append(" :adId, :address, :groupName, :keyWord, :companyId, UNIX_TIMESTAMP(NOW()), :mateName, :matePhone, :mateWeChat, :mateQq,");
        sql.append(" :marryTime, :ypTime, :oldKzName, :oldKzPhone, :ysRange, yxLevel  ) ");

        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(clientVO);
        namedJdbc.update(sql.toString(), sqlParameterSource, new GeneratedKeyHolder());

    }

    /**
     * 新增备注
     *
     * @param clientVO
     */
    private void doAddRemark(ClientVO clientVO) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("content", StringUtil.nullToStr(clientVO.getRemark()));
        paramMap.put("companyId", clientVO.getCompanyId());
        paramMap.put("kzId", clientVO.getKzId());
        String sql = " INSERT INTO hm_crm_client_remark ( KZID, CONTENT, COMPANYID ) VALUES ( :kzId, :content, :companyId )";
        namedJdbc.update(sql, paramMap);
    }

    /**
     * 新增客资日志
     *
     * @param addType  新增的类型 手机等
     * @param clientVO
     */
    private void doAddClientLog(int addType, ClientVO clientVO) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("companyId", clientVO.getCompanyId());
        paramMap.put("kzId", clientVO.getKzId());
        paramMap.put("operatorId", clientVO.getOperaId());
        paramMap.put("operatorName", clientVO.getOperaName());
        paramMap.put("memo", getInfoAddLog(addType, clientVO));
        paramMap.put("logType", ClientLogConst.INFO_LOGTYPE_ADD);
        String sql = " INSERT INTO hm_crm_client_log  ( KZID, OPERAID, OPERANAME, MEMO, OPERATIME, LOGTYPE, COMPANYID )" +
                " VALUES ( :kzId, :operatorId, :operatorName, :memo, UNIX_TIMESTAMP(NOW()), :logType, :companyId ) ";
        namedJdbc.update(sql, paramMap);

    }

    /**
     * 设置短字母ID
     *
     * @param id
     * @param companyId
     */
    private void updateLetterId(int id, int companyId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("letterId", StringUtil.to26Jinzhi(id));
        paramMap.put("companyId", companyId);
        paramMap.put("id", id);
        String sql = " UPDATE hm_crm_client_info SET LETTERID = :letterId WHERE ID = :id AND COMPANYID =:companyId";
        namedJdbc.update(sql, paramMap);
    }

    /**
     * 获取客资录入时的描述
     */
    private String getInfoAddLog(int method, ClientVO clientVO) {
        StringBuilder log = new StringBuilder();
        log.append("在").append(AddTypeEnum.getTypeNameById(method));
        log.append(" 通过 ");
        log.append(StringUtil.nullToStrTrim(clientVO.getChannelName()));
        log.append(" 渠道，");
        log.append(clientVO.getSourceName());
        log.append(" 来源，录入了客资。");
        if (StringUtil.isNotEmpty(clientVO.getGroupName()) && StringUtil.isNotEmpty(clientVO.getAppointName())) {
            log.append(" => 指定客服：");
            log.append(clientVO.getGroupName());
            log.append(" - ");
            log.append(clientVO.getAppointName());
            log.append("。");
        }
        return log.toString();
    }


    /**
     * 预处理客资信息
     */
    private void initClientInfo(ClientVO clientVO) {
        if (NumUtil.isValid(clientVO.getAppointId()) && StringUtil.isNotEmpty(clientVO.getAppointName())) {
            // 录入时已经指定了邀约客服
            clientVO.setStatusId(ClientStatusConst.BE_HAVE_MAKE_ORDER);
            clientVO.setAllotType(ClientConst.ALLOT_WHILE_COLLECT);
        } else if (StringUtil.isNotEmpty(clientVO.getFilterFlag()) && Boolean.parseBoolean(clientVO.getFilterFlag())) {
            //筛选
            clientVO.setStatusId(ClientStatusConst.BE_WAIT_FILTER);
            clientVO.setAllotType(ClientConst.ALLOT_SYSTEM_AUTO);
        } else {
            //可领取
            clientVO.setStatusId(ClientStatusConst.BE_WAIT_MAKE_ORDER);
            clientVO.setAllotType(ClientConst.ALLOT_SYSTEM_AUTO);
        }
        //设置为新客资
        clientVO.setClassId(ClientStatusConst.KZ_CLASS_NEW);
        //备注
        clientVO.setMemo(StringUtil.subStr(StringUtil.replaceAllHTML(clientVO.getRemark()), 255));
        //获取备注的图片列表
        List<String> imgList = StringUtil.getImgLists(clientVO.getRemark());
        if (CollectionUtils.isNotEmpty(imgList)) {
            clientVO.setTalkImg(imgList.get(0));
        }
    }


}
