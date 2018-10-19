package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ChannelConstant;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.RegexUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.NewsDao;
import com.qiein.jupiter.web.dao.StaffDao;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.dto.RepeatDTO;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 客资查重
 *
 * @Author: shiTao
 */
@Repository
public class CheckClientRepeatDao {
    @Autowired
    private NamedParameterJdbcOperations namedJdbc;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private ClientInfoDao clientInfoDao;
    @Autowired
    private NewsDao newsDao;


    public void check(ClientVO clientVO) {
        int companyId = clientVO.getCompanyId();
        if (CommonConstant.WECHAT_QRCODE.equals(clientVO.getKzWechat()) ||
                CommonConstant.WECHAT_QRCODE.equals(clientVO.getKzPhone()) ||
                CommonConstant.WECHAT_QRCODE.equals(clientVO.getKzQq()) ||
                CommonConstant.WECHAT_QRCODE.equals(clientVO.getKzWw()) ||
                CommonConstant.WECHAT_QRCODE.equals(clientVO.getMatePhone()) ||
                CommonConstant.WECHAT_QRCODE.equals(clientVO.getMateQq()) ||
                CommonConstant.WECHAT_QRCODE.equals(clientVO.getMateWeChat())) {
            return;
        }
        Set<String> infoSet = getLinkHashSet(clientVO);
        //客资没有任何联系方式
        if (infoSet.isEmpty()) {
            return;
        }
        //获取公司的重复设置
        RepeatDTO companyRepeatSet = getCompanyRepeatSet(companyId);
        //info表查重
        checkClientInfo(clientVO, companyRepeatSet);
        //detail查重
        checkClientDetail(clientVO, companyRepeatSet);
    }


    /**
     * 检查Info表
     */
    private void checkClientInfo(ClientVO clientVO, RepeatDTO repeatDTO) {
        String kzPhone = clientVO.getKzPhone();
        String kzWeChat = clientVO.getKzWechat();
        String kzQQ = clientVO.getKzQq();
        String kzWW = clientVO.getKzWw();
        String matePhone = clientVO.getMatePhone();
        String mateQQ = clientVO.getMateQq();
        String mateWeChat = clientVO.getMateWeChat();

        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", clientVO.getCompanyId());
        keyMap.put("kzPhone", kzPhone);
        keyMap.put("kzWeChat", kzWeChat);
        keyMap.put("kzQQ", kzQQ);
        keyMap.put("kzWW", kzWW);
        keyMap.put("matePhone", matePhone);
        keyMap.put("mateWeChat", mateQQ);
        keyMap.put("mateQQ", mateWeChat);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(1) NUM, info.KZID FROM hm_crm_client_info info" +
                " WHERE info.ISDEL =0 AND  info.COMPANYID = :companyId ");
        sql.append(" AND ( ");
        Set<String> infoSet = getLinkHashSet(clientVO);

        StringBuilder whereSql = new StringBuilder();
        int index = 0;
        for (String linkStr : infoSet) {
            if (StringUtil.isNotEmpty(whereSql.toString())) {
                whereSql.append(" OR ");
            }
            index++;
            String key = "keyWord" + index;
            keyMap.put(key, linkStr);
            whereSql.append(" info.KZPHONE = :").append(key)
                    .append(" OR info.KZWECHAT = :").append(key)
                    .append(" OR info.KZQQ = :").append(key)
                    .append(" OR info.KZWW = :").append(key);
        }
        sql.append(whereSql);
        sql.append(" ) ");

        //追加重复配置
        appendRepeatConfig(clientVO, repeatDTO, keyMap, sql);
        //查询
        Map<String, Object> resultMap = namedJdbc.queryForMap(sql.toString(), keyMap);
        //校验结果
        checkResult(clientVO, resultMap);
    }

    /**
     * 检查detail表
     */
    private void checkClientDetail(ClientVO clientInfoVO, RepeatDTO repeatDTO) {
        String kzPhone = clientInfoVO.getKzPhone();
        String kzWeChat = clientInfoVO.getKzWechat();
        String kzQQ = clientInfoVO.getKzQq();
        String kzWW = clientInfoVO.getKzWw();
        String matePhone = clientInfoVO.getMatePhone();
        String mateQQ = clientInfoVO.getMateQq();
        String mateWeChat = clientInfoVO.getMateWeChat();

        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", clientInfoVO.getCompanyId());
        keyMap.put("kzPhone", kzPhone);
        keyMap.put("kzWeChat", kzWeChat);
        keyMap.put("kzQQ", kzQQ);
        keyMap.put("kzWW", kzWW);
        keyMap.put("matePhone", matePhone);
        keyMap.put("mateWeChat", mateQQ);
        keyMap.put("mateQQ", mateWeChat);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(1) NUM, info.KZID FROM hm_crm_client_info info" +
                " WHERE info.ISDEL =0 AND  info.COMPANYID = :companyId ");
        sql.append(" AND ( ");
        Set<String> infoSet = getLinkHashSet(clientInfoVO);
        StringBuilder whereSql = new StringBuilder();
        int index = 0;
        for (String linkStr : infoSet) {
            if (StringUtil.isNotEmpty(whereSql.toString())) {
                whereSql.append(" OR ");
            }
            index++;
            String key = "keyWord" + index;
            keyMap.put(key, linkStr);
            whereSql.append(" det.MATEPHONE = :").append(key)
                    .append(" OR det.MATEWECHAT = :").append(key)
                    .append(" OR det.MATEQQ = :").append(key);
        }
        sql.append(whereSql);
        sql.append(" ) ");

        //追加重复配置
        appendRepeatConfig(clientInfoVO, repeatDTO, keyMap, sql);
        //查询
        Map<String, Object> resultMap = namedJdbc.queryForMap(sql.toString(), keyMap);
        //校验结果
        checkResult(clientInfoVO, resultMap);
    }

    /**
     * 获取联系方式的HASHSET
     *
     * @return
     */
    private Set<String> getLinkHashSet(ClientVO clientVO) {
        String kzPhone = clientVO.getKzPhone();
        String kzWeChat = clientVO.getKzWechat();
        String kzQQ = clientVO.getKzQq();
        String kzWW = clientVO.getKzWw();
        String matePhone = clientVO.getMatePhone();
        String mateQQ = clientVO.getMateQq();
        String mateWeChat = clientVO.getMateWeChat();

        Set<String> infoSet = new HashSet<>();
        if (StringUtil.isNotEmpty(kzPhone)) {
            infoSet.add(kzPhone);
        }
        if (StringUtil.isNotEmpty(kzWeChat)) {
            infoSet.add(kzWeChat);
        }
        if (StringUtil.isNotEmpty(kzQQ)) {
            infoSet.add(kzQQ);
        }
        if (StringUtil.isNotEmpty(kzWW)) {
            infoSet.add(kzWW);
        }
        if (StringUtil.isNotEmpty(matePhone)) {
            infoSet.add(matePhone);
        }
        if (StringUtil.isNotEmpty(mateQQ)) {
            infoSet.add(mateQQ);
        }
        if (StringUtil.isNotEmpty(mateWeChat)) {
            infoSet.add(mateWeChat);
        }
        return infoSet;
    }


    /**
     * 检查重复设置追加字段
     */
    private void appendRepeatConfig(ClientVO clientInfoVO, RepeatDTO repeatDTO, Map<String, Object> keyMap, StringBuilder sql) {
        //校验重复时间
        if (StringUtil.isNotEmpty(repeatDTO.getTimeTypeIgnore()) && NumUtil.isValid(repeatDTO.getDayIgnore())) {
            keyMap.put("dayIgnore", repeatDTO.getDayIgnore() * 24 * 60 * 60);
            sql.append(" AND info.").append(repeatDTO.getTimeTypeIgnore()).append(" >= ( UNIX_TIMESTAMP(NOW()) - :dayIgnore )");
        }
        //校验重复时是否区分拍摄类型
        if (repeatDTO.getTypeRepeat()) {
            keyMap.put("typeId", clientInfoVO.getTypeId());
            sql.append(" AND info.TYPEID = :typeId ");
        }
        if (repeatDTO.getSrcRepeat()) {
            if (ChannelConstant.DS_TYPE_LIST.contains(clientInfoVO.getSrcType())) {
                sql.append(" AND info.SRCTYPE IN (1,2) ");
            } else if (ChannelConstant.ZJS_TYPE_LIST.contains(clientInfoVO.getSrcType())) {
                sql.append(" AND info.SRCTYPE IN ( 3,4,5 ) ");
            }
        }
        //
        if (StringUtil.isNotEmpty(repeatDTO.getStatusIgnore())) {
            String[] stsArr = repeatDTO.getStatusIgnore().split(CommonConstant.STR_SEPARATOR);
            for (String statusId : stsArr) {
                if (!RegexUtil.isNumeric(statusId)) {
                    continue;
                }
                sql.append(" AND info.STATUSID != ");
                sql.append(statusId);
            }
        }
        if (StringUtil.isNotEmpty(clientInfoVO.getKzId())) {
            keyMap.put("kzId", clientInfoVO.getKzId());
            sql.append(" AND info.KZID != :kzId ");
        }
    }


    /**
     * 校验检查结果
     */
    private void checkResult(ClientVO clientInfoVO, Map<String, Object> resultMap) {
        int companyId = clientInfoVO.getCompanyId();
        if ((long) resultMap.get("NUM") != 0) {
            String kzId = (String) resultMap.get("KZID");
            //添加日志
            addRepeatLog(kzId, clientInfoVO);
            //发送消息
            //重复客资，给邀约推送消息
            ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(kzId,
                    DBSplitUtil.getInfoTabName(companyId),
                    DBSplitUtil.getDetailTabName(companyId));
            if (info == null) {
                throw new RException("存在重复客资");
            }
            GoEasyUtil.pushRepeatClient(companyId, info.getAppointorId(), info, clientInfoVO.getOperaName(), newsDao, staffDao);
            GoEasyUtil.pushRepeatClient(companyId, info.getCollectorId(), info, clientInfoVO.getOperaName(), newsDao, staffDao);
            GoEasyUtil.pushRepeatClient(companyId, info.getPromotorId(), info, clientInfoVO.getOperaName(), newsDao, staffDao);
            throw new RException("存在重复客资");
        }
    }


    /**
     * 新增重复日志
     */
    private void addRepeatLog(String kzId, ClientVO clientInfoVO) {
        String operatorName = clientInfoVO.getOperaName();
        StringBuilder desc = new StringBuilder();
        desc.append("尝试重复录入被拦截：重复信息 -  ");
        if (StringUtil.isNotEmpty(clientInfoVO.getKzName())) {
            desc.append("客资姓名：").append(clientInfoVO.getKzName()).append("；");
        }
        if (StringUtil.isNotEmpty(clientInfoVO.getSourceName())) {
            desc.append("渠道：").append(clientInfoVO.getSourceName()).append("；");
        }
        desc.append("重复客资的联系方式：");
        if (StringUtil.isNotEmpty(clientInfoVO.getKzWechat())) {
            desc.append("微信：").append(clientInfoVO.getKzWechat()).append("；");
        }
        if (StringUtil.isNotEmpty(clientInfoVO.getKzPhone())) {
            desc.append("手机：").append(clientInfoVO.getKzPhone()).append("；");
        }
        if (StringUtil.isNotEmpty(clientInfoVO.getKzQq())) {
            desc.append("QQ：").append(clientInfoVO.getKzQq()).append("；");
        }
        if (StringUtil.isNotEmpty(clientInfoVO.getKzWw())) {
            desc.append("旺旺：").append(clientInfoVO.getKzWw()).append("；");
        }
        if (StringUtil.isNotEmpty(clientInfoVO.getMateWeChat())) {
            desc.append("配偶微信：").append(clientInfoVO.getMateWeChat()).append("；");
        }
        if (StringUtil.isNotEmpty(clientInfoVO.getMatePhone())) {
            desc.append("配偶手机：").append(clientInfoVO.getMatePhone()).append("；");
        }
        if (StringUtil.isNotEmpty(clientInfoVO.getMateQq())) {
            desc.append("配偶QQ：").append(clientInfoVO.getMateQq()).append("；");
        }
        desc.append("提报人：").append(operatorName).append("；");

        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", clientInfoVO.getCompanyId());
        keyMap.put("kzId", kzId);
        keyMap.put("operatorId", clientInfoVO.getOperaId());
        keyMap.put("operatorName", operatorName);
        keyMap.put("memo", desc.toString());
        keyMap.put("type", ClientLogConst.INFO_LOGTYPE_REPEAT);

        String sql = "INSERT INTO hm_crm_client_log ( KZID, OPERAID, OPERANAME, MEMO, OPERATIME, LOGTYPE, COMPANYID ) " +
                " VALUES (:kzId, :operatorId, :operatorName,:memo,UNIX_TIMESTAMP(NOW()),:type,:companyId) ";
        namedJdbc.update(sql, keyMap);

    }

    /**
     * 获取公司重复配置
     *
     * @param companyId
     */
    private RepeatDTO getCompanyRepeatSet(int companyId) {

        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);

        final RepeatDTO repeatVO = new RepeatDTO();
        String sql = "SELECT comp.TYPEREPEAT, comp.SRCREPEAT, comp.STATUSIGNORE, comp.TIMETYPEIGNORE, comp.DAYIGNORE FROM hm_pub_company comp WHERE comp.ID = :companyId ";

        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                repeatVO.setTypeRepeat(resultSet.getBoolean("TYPEREPEAT"));
                repeatVO.setSrcRepeat(resultSet.getBoolean("SRCREPEAT"));
                repeatVO.setStatusIgnore(resultSet.getString("STATUSIGNORE"));
                repeatVO.setTimeTypeIgnore(resultSet.getString("TIMETYPEIGNORE"));
                repeatVO.setDayIgnore(resultSet.getInt("DAYIGNORE"));
            }

        });

        return repeatVO;
    }
}
