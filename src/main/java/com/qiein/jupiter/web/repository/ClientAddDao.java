package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ClientConst;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.enums.AddTypeEnum;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.web.dao.ClientStatusDao;
import com.qiein.jupiter.web.dao.DictionaryDao;
import com.qiein.jupiter.web.entity.po.ClientStatusPO;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
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

    @Autowired
    private DictionaryDao dictionaryDao;

    @Autowired
    private ClientStatusDao clientStatusDao;

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
    @Transactional
    public ClientVO addPcMdClientInfo(ClientVO clientVO) {
        clientVO.setKzId(StringUtil.getRandom());
        //预处理
        initClientInfoComeShop(clientVO);
        //新增客资信息
        int id = doAddClintInfo(clientVO);
        //更新letterId
        updateLetterId(id, clientVO.getCompanyId());
        //新增客资详情
        doAddClientDetail(clientVO);
        //新增备注
        doAddRemark(clientVO);
        if (ClientStatusConst.BE_SUCCESS == clientVO.getStatusId() && NumUtil.isValid(clientVO.getPayAmount())) {
            // 添加收款记录
            addCashLog(clientVO);
        }
        //新增日志
        doAddClientLog(clientVO, getInfoAddLog(clientVO));
        doAddClientLog(clientVO,doSavaInviteLog(clientVO));
        return clientVO;
    }


    /**
     * 手机录入客资
     */
    @Transactional
    public ClientVO addClientInfo(ClientVO clientVO) {
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
        doAddClientLog(clientVO, getInfoAddLog(clientVO));
        return clientVO;
    }


    /**
     * info表新增
     */
    private int doAddClintInfo(ClientVO clientVO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO hm_crm_client_info ");
        sql.append("  ( KZID, TYPEID, CLASSID, STATUSID, KZNAME, KZPHONE, KZWECHAT, KZQQ, KZWW, SEX,  ");
        sql.append(" CHANNELID, SOURCEID, COLLECTORID, APPOINTORID, PROMOTORID, RECEPTORID, SHOPID, ALLOTTYPE, " +
                "COMPANYID, SRCTYPE, GROUPID, CREATETIME , COMESHOPTIME, ACTUALTIME ");
        sql.append(" ");
        if (ClientStatusConst.BE_HAVE_MAKE_ORDER == clientVO.getStatusId()) {
            sql.append(" , RECEIVETIME ");
        }
        if (ClientStatusConst.BE_SUCCESS == clientVO.getStatusId()) {
            sql.append(" , SUCCESSTIME ");
        } else if (ClientStatusConst.BE_RUN_OFF == clientVO.getStatusId()) {
            sql.append("  , APPOINTTIME ");
        }
        sql.append(" , UPDATETIME ) ");
        sql.append(" VALUES( :kzId, :typeId, :classId, :statusId, :kzName, :kzPhone, :kzWechat, :kzQq, :kzWw ,:sex ,");
        sql.append(" :channelId , :sourceId, :collectorId , :appointId, :promotorId, :receptorId, :shopId, :allotType," +
                " :companyId, :srcType, :groupId, UNIX_TIMESTAMP(NOW()),:comeShopTime,:comeShopTime");
        if (ClientStatusConst.BE_HAVE_MAKE_ORDER == clientVO.getStatusId()) {
            sql.append(" , UNIX_TIMESTAMP(NOW()) ");
        }
        if (ClientStatusConst.BE_SUCCESS == clientVO.getStatusId()) {
            sql.append(" , :successTime ");
        } else if (ClientStatusConst.BE_RUN_OFF == clientVO.getStatusId()) {
            sql.append(" ,:appointTime  ");
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
        sql.append(" ( KZID, COLLECTORNAME, PROMOTERNAME, APPOINTNAME, SHOPNAME, MEMO, TALKIMG, ORDERIMG, ZXSTYLE, ADADDRESS,  ");
        sql.append(" ADID, ADDRESS, GROUPNAME, KEYWORD, COMPANYID, CREATETIME , MATENAME, MATEPHONE, MATEWECHAT, MATEQQ,");
        sql.append(" MARRYTIME, YPTIME, OLDKZNAME, OLDKZPHONE, YSRANGE , YXLEVEL , AMOUNT, STAYAMOUNT, PAYSTYLE, HTNUM ,INVALIDLABEL ) ");
        sql.append(" VALUES ( :kzId, :collectorName, :promoterName, :appointName, :shopName, :memo, :talkImg,:orderImg, :zxStyle, :adAddress,");
        sql.append(" :adId, :address, :groupName, :keyWord, :companyId, UNIX_TIMESTAMP(NOW()), :mateName, :matePhone, :mateWeChat, :mateQq,");
        sql.append(" :marryTime, :ypTime, :oldKzName, :oldKzPhone, :ysRange, :yxLevel,:amount,:stayAmount,:payStyle,:htNum,:invalidLabel ) ");

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
     */
    private void doAddClientLog(ClientVO clientVO, String memo) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("companyId", clientVO.getCompanyId());
        paramMap.put("kzId", clientVO.getKzId());
        paramMap.put("operatorId", clientVO.getOperaId());
        paramMap.put("operatorName", clientVO.getOperaName());
        paramMap.put("memo", memo);
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
    private String getInfoAddLog(ClientVO clientVO) {
        StringBuilder log = new StringBuilder();
        log.append("在").append(AddTypeEnum.getTypeNameById(clientVO.getAddType() == null ? 1 : clientVO.getAddType()));
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
     * 新增邀约日志
     */
    private String doSavaInviteLog(ClientVO clientVO) {
        String memo = StringUtil.replaceAllHTML(clientVO.getMemo());
        String jdMemo;
        ClientStatusPO clientStatusByStatusId = clientStatusDao.getClientStatusByStatusId(clientVO.getStatusId(), clientVO.getCompanyId());
        //获取字典数据

        String statusName = clientStatusByStatusId.getStatusName();
        if (ClientStatusConst.BE_RUN_OFF == clientVO.getYyRst() || ClientStatusConst.BACK_RUN_OFF == clientVO.getYyRst()) {
            // 流失
            jdMemo = "客资-";
            jdMemo += statusName;
            jdMemo += ",流失原因:";
            jdMemo += clientVO.getInvalidLabel();
            jdMemo += ";接待门店：";
            jdMemo += clientVO.getShopName();
            jdMemo += ";接待门市：";
            jdMemo += clientVO.getReceptorName();
            jdMemo += ";到店时间：";
            jdMemo += TimeUtil.intMillisToTimeStr(clientVO.getComeShopTime());
            if (NumUtil.isValid(clientVO.getAppointTime())) {
                jdMemo += ";下次预约时间：";
                jdMemo += TimeUtil.intMillisToTimeStr(clientVO.getAppointTime());
            }
            if (StringUtil.isNotEmpty(memo)) {
                jdMemo += ";追踪备注:";
                jdMemo += memo;
            }
        } else if (ClientStatusConst.BE_SUCCESS == clientVO.getYyRst()
                || ClientStatusConst.BACK_SHOP_SUCCESS == clientVO.getYyRst()) {
            // 成交
            jdMemo = "客资-";
            jdMemo += statusName;
            jdMemo += ";接待门店：";
            jdMemo += clientVO.getShopName();
            jdMemo += ";接待门市：";
            jdMemo += clientVO.getReceptorName();
            jdMemo += ";订单时间：";
            jdMemo += TimeUtil.intMillisToTimeStr(clientVO.getSuccessTime());
            jdMemo += ";付款方式：";
            jdMemo += dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), clientVO.getPayStyle(), DictionaryConstant.PAY_STYLE).getDicName();
            jdMemo += ";合同编号：";
            jdMemo += clientVO.getHtNum();
            jdMemo += ";套系金额：";
            jdMemo += clientVO.getAmount();
            jdMemo += ";已收金额：";
            jdMemo += clientVO.getPayAmount();
            if (StringUtil.isNotEmpty(memo)) {
                jdMemo += ";追踪备注:";
                jdMemo += memo;
            }
        } else if (ClientStatusConst.BE_SUCCESS_STAY == clientVO.getYyRst()
                || ClientStatusConst.BACK_SUCCESS_SHOP_STAY == clientVO.getYyRst()) {
            // 保留
            jdMemo = "客资-";
            jdMemo += statusName;
            jdMemo += ";接待门店：";
            jdMemo += clientVO.getShopName();
            jdMemo += ";接待门市：";
            jdMemo += clientVO.getReceptorName();
            jdMemo += ";接待时间：";
            jdMemo += TimeUtil.intMillisToTimeStr(clientVO.getComeShopTime());
            jdMemo += ";合同编号：";
            jdMemo += clientVO.getHtNum();
            jdMemo += ";套系金额：";
            jdMemo += clientVO.getAmount();
            jdMemo += ";保留金：";
            jdMemo += clientVO.getPayAmount();
            if (StringUtil.isNotEmpty(memo)) {
                jdMemo += ";追踪备注:";
                jdMemo += memo;
            }
        } else {
            jdMemo = memo;
        }
        if (StringUtil.isEmpty(jdMemo) && StringUtil.isEmpty(StringUtil.getImgStrs(clientVO.getMemo()))) {
            return "";
        }
        return jdMemo;
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("kzId", clientVO.getKzId());
//        paramMap.put("comeShopTime", clientVO.getComeShopTime());
//        paramMap.put("yyRst", clientVO.getYyRst());
//        paramMap.put("jdMemo", jdMemo);
//        paramMap.put("amount", clientVO.getAmount());
//        paramMap.put("receptorId", clientVO.getReceptorId());
//        paramMap.put("operaId", clientVO.getOperaId());
//        paramMap.put("shopId", clientVO.getShopId());
//        paramMap.put("companyId", clientVO.getCompanyId());
//
//        String sql = " INSERT INTO hm_crm_shop_meet_log " +
//                " ( KZID, ARRIVETIME, RSTCODE, QTMEMO, AMOUNT, RECEPTERID, OPERAID, CREATETIME, SHOPID, COMPANYID ) "
//                + " VALUES  ( :kzId, :comeShopTime, :yyRst, :jdMemo,:amount, :receptorId, :operaId,  " +
//                "UNIX_TIMESTAMP(NOW()), :shopId, :companyId) ";
//
//        namedJdbc.update(sql, paramMap);
    }


    /**
     * 预处理客资信息
     */
    private void initClientInfo(ClientVO clientVO) {
        if (NumUtil.isValid(clientVO.getAppointId()) && StringUtil.isNotEmpty(clientVO.getAppointName())) {
            // 录入时已经指定了邀约客服
            clientVO.setStatusId(ClientStatusConst.BE_HAVE_MAKE_ORDER);
            clientVO.setAllotType(ClientConst.ALLOT_WHILE_COLLECT);
        } else if (clientVO.isFilterFlag()) {
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


    /**
     * 进店客资预处理
     */
    private void initClientInfoComeShop(ClientVO clientVO) {
        clientVO.setAllotType(ClientConst.ALLOT_SYSTEM_AUTO);
        /*-- 获取客资分类ID --*/
        ClientStatusPO clientStatusByStatusId = clientStatusDao.getClientStatusByStatusId(clientVO.getStatusId(), clientVO.getCompanyId());
        Integer classId = clientStatusByStatusId.getClassId();
        clientVO.setClassId(classId);
        clientVO.setMemo(StringUtil.subStr(StringUtil.replaceAllHTML(clientVO.getRemark()), 255));
        List<String> imgList = StringUtil.getImgLists(clientVO.getRemark());
        if (CollectionUtils.isNotEmpty(imgList)) {
            clientVO.setOrderImg(imgList.get(0));
        }
        if (ClientStatusConst.BE_SUCCESS == clientVO.getStatusId()) {
            clientVO.setComeShopTime(clientVO.getSuccessTime());
            clientVO.setPayTime(clientVO.getSuccessTime());
        }
    }


    /**
     * 添加收款记录 ,修改已收金额
     */
    private void addCashLog(ClientVO clientVO) {
        String sql = " INSERT INTO hm_crm_cash_log" +
                " ( KZID, PAYSTYLE, AMOUNT, STAFFID, STAFFNAME, PAYMENTTIME, OPERAID, OPERANAME, CREATETIME, COMPANYID, STATUS ) "
                + "VALUES ( :kzId, :payStyle, :payAmount, :receiptId, :receiptName, :payTime, :operaId, :operaName, " +
                "UNIX_TIMESTAMP(NOW()),  :companyId, 1 ) ";
        namedJdbc.update(sql, new BeanPropertySqlParameterSource(clientVO));

        String sql1 = "UPDATE hm_crm_client_detail SET  STAYAMOUNT = ( SELECT IFNULL( SUM( AMOUNT ), 0 ) FROM" +
                " hm_crm_cash_log  WHERE KZID = :kzId AND COMPANYID = :companyId AND STATUS = 1 )" +
                "  WHERE KZID = :kzId AND COMPANYID =:companyId ";
        namedJdbc.update(sql1, new BeanPropertySqlParameterSource(clientVO));

    }


}
