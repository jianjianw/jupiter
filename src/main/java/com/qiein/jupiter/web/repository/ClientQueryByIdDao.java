package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.RegexUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.QueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询客资详情
 *
 * @Author: shiTao
 */
@Repository
public class ClientQueryByIdDao {

    @Autowired
    private NamedParameterJdbcOperations namedJdbc;


    /**
     * 根据KZID 获取客资信息
     */
    public JSONObject getClientByKzid(QueryVO vo) {


        JSONObject info = new JSONObject();

        JSONObject clientDetailInfo = getClientDetailInfo(vo);
        // 获取录入备注
        clientDetailInfo.put("remark", getInfoRemark(vo));
        // 获取客资信息
        info.put("info", clientDetailInfo);
        // 操作日志
        info.put("kzlog", getClientLog(vo));
        // 邀约日志
        info.put("yylog", getAppointLog(vo));
        // 分配日志
        info.put("fplog", getAllotLog(vo));
        //收款日志
        info.put("sklog", getCashLog(vo));

        // 获取权限集
        info.put("pms", getPmsMap(vo.getUid(), vo.getCompanyId(),
                clientDetailInfo.getIntValue("collectorid"),
                clientDetailInfo.getIntValue("appointorid"),
                clientDetailInfo.getIntValue("receptorid")));

        return info;
    }


    /**
     * 查询客资详情
     */
    private JSONObject getClientDetailInfo(QueryVO vo) {
        int companyId = vo.getCompanyId();
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("kzid", vo.getKzId());
        //select
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT info.ID, info.KZID, info.TYPEID, info.CLASSID, info.STATUSID,info.LETTERID,info.KZPHONE_FLAG, ").
                append("info.KZNAME, info.KZPHONE, info.KZWECHAT, info.WEFLAG,info.SRCTYPE, ")
                .append(" info.KZQQ, info.KZWW, info.SEX, info.CHANNELID, info.SOURCEID, info.COLLECTORID, det.COLLECTORNAME,")
                .append(" info.PROMOTORID, det.PROMOTERNAME, info.APPOINTORID, ")
                .append("  det.APPOINTNAME, info.RECEPTORID, det.RECEPTORNAME, info.SHOPID, det.SHOPNAME, info.ALLOTTYPE,")
                .append(" info.CREATETIME, info.RECEIVETIME, info.TRACETIME, info.APPOINTTIME, det.PACKAGECODE, ")
                .append(" info.COMESHOPTIME, info.SUCCESSTIME, info.UPDATETIME, det.MEMO,det.FILMINGCODE,det.FILMINGAREA, ")
                .append(" det.OLDKZNAME, det.OLDKZPHONE, det.AMOUNT, det.STAYAMOUNT, det.TALKIMG, det.ORDERIMG, ")
                .append(" det.ZXSTYLE, det.YXLEVEL, det.YSRANGE, det.ADADDRESS, det.ADID, det.MARRYTIME,")
                .append(" det.YPTIME, det.MATENAME, det.MATEPHONE, det.ADDRESS, det.MATEWECHAT,det.MATEQQ, ")
                .append(" info.GROUPID, det.GROUPNAME, det.PAYSTYLE, det.HTNUM, det.INVALIDLABEL, det.KEYWORD ");
        //from
        sql.append(" FROM ").append(DBSplitUtil.getInfoTabName(companyId));
        sql.append(" info LEFT JOIN ");
        sql.append(DBSplitUtil.getDetailTabName(companyId));
        sql.append(" det ON info.KZID = det.KZID AND det.COMPANYID = info.COMPANYID ");
        //where
        sql.append(" WHERE info.COMPANYID = :companyId ");
        if (RegexUtil.isNumeric(vo.getKzId())) {
            sql.append(" AND info.ID = :kzid");
        } else {
            sql.append(" AND info.KZID = :kzid");
        }

        //执行查询
        final List<JSONObject> result = new ArrayList<>();
        namedJdbc.query(sql.toString(), keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                result.add(ClientQueryDao.resultToClientInfo(resultSet));
            }
        });
        return result.get(0);
    }


    /**
     * 客资备注
     *
     * @param vo
     * @return
     */
    private String getInfoRemark(QueryVO vo) {
        int companyId = vo.getCompanyId();
        String sql = " SELECT remark.CONTENT FROM " +
                DBSplitUtil.getRemarkTabName(companyId) +
                "remark WHERE remark.COMPANYID = :companyId AND remark.KZID = :kzid ";
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("kzid", vo.getKzId());

        final StringBuilder content = new StringBuilder();
        //执行查询
        final List<JSONObject> result = new ArrayList<>();
        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                content.append(resultSet.getString("CONTENT"));
            }
        });
        return content.toString();
    }

    /**
     * 获取客资日志
     */
    public List<JSONObject> getClientLog(QueryVO queryVO) {
        int companyId = queryVO.getCompanyId();
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("kzid", queryVO.getKzId());
        String sql = "  SELECT log.OPERAID, log.OPERANAME, sf.HEADIMG, log.MEMO, log.OPERATIME, log.LOGTYPE FROM "
                + DBSplitUtil.getInfoLogTabName(companyId)
                + " log LEFT JOIN hm_pub_staff sf ON log.OPERAID = sf.ID AND sf.COMPANYID = :companyId" +
                " WHERE log.COMPANYID = :companyId AND log.KZID = :kzid ORDER BY log.OPERATIME DESC, log.ID DESC ";

        //执行查询
        final List<JSONObject> logs = new ArrayList<>();
        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                JSONObject log = new JSONObject();
                log.put("operaid", resultSet.getInt("OPERAID"));
                log.put("operaname", resultSet.getString("OPERANAME"));
                if (0 == log.getInteger("operaid")) {
                    log.put("headimg", CommonConstant.CMJ_LOGO_IMG48);
                } else {
                    log.put("headimg", resultSet.getString("HEADIMG"));
                }
                log.put("memo", resultSet.getString("MEMO"));
                log.put("operatime", resultSet.getInt("OPERATIME"));
                log.put("logtype", resultSet.getInt("LOGTYPE"));
                logs.add(log);
            }
        });
        return logs;

    }


    /**
     * 获取分配日志
     */
    public List<JSONObject> getAllotLog(QueryVO queryVO) {
        int companyId = queryVO.getCompanyId();
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("kzid", queryVO.getKzId());
        String sql = "  SELECT log.ALLOTTIME, log.STAFFID, log.STAFFNAME, " +
                "sf.HEADIMG, log.GROUPID, log.GROUPNAME, log.STATUSID, log.RECEIVETIME, log.ALLOTTYPE FROM  "
                + DBSplitUtil.getAllotLogTabName(companyId) +
                " log LEFT JOIN hm_pub_staff sf ON log.STAFFID = sf.ID AND sf.COMPANYID = :companyId" +
                " WHERE log.COMPANYID = :companyId AND log.KZID = :kzid ORDER BY log.ID DESC ";
        //执行查询
        final List<JSONObject> logs = new ArrayList<>();
        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                JSONObject log = new JSONObject();
                log.put("allottime", resultSet.getInt("ALLOTTIME"));
                log.put("staffid", resultSet.getInt("STAFFID"));
                log.put("staffname", resultSet.getString("STAFFNAME"));
                log.put("headimg", resultSet.getString("HEADIMG"));
                log.put("groupid", resultSet.getString("GROUPID"));
                log.put("groupname", resultSet.getString("GROUPNAME"));
                log.put("statusid", resultSet.getInt("STATUSID"));
                log.put("receivetime", resultSet.getInt("RECEIVETIME"));
                log.put("allottype", resultSet.getInt("ALLOTTYPE"));
                logs.add(log);
            }
        });
        return logs;
    }

    /**
     * 获取收款日志
     */
    public List<JSONObject> getCashLog(QueryVO queryVO) {
        int companyId = queryVO.getCompanyId();
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("kzid", queryVO.getKzId());
        String sql = " SELECT log.ID, log.PAYSTYLE, log.AMOUNT, log.STAFFID, log.STAFFNAME," +
                " FROM_UNIXTIME(log.PAYMENTTIME, '%Y/%m/%d %H:%i') AS PAYMENTTIME, log.OPERAID, " +
                "log.OPERANAME, FROM_UNIXTIME(log.CREATETIME, '%Y/%m/%d %H:%i') AS CREATETIME, log.COMPANYID, log.STATUS FROM " +
                DBSplitUtil.getCashTabName(companyId) +
                " log WHERE log.COMPANYID = :companyId AND log.KZID = :kzid AND log.STATUS = 1 ORDER BY log.ID DESC";
        //执行查询
        final List<JSONObject> logs = new ArrayList<>();
        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                JSONObject log = new JSONObject();
                log.put("id", resultSet.getInt("ID"));
                log.put("paystyle", resultSet.getInt("PAYSTYLE"));
                log.put("amount", resultSet.getInt("AMOUNT"));
                log.put("staffid", resultSet.getInt("STAFFID"));
                log.put("staffname", resultSet.getString("STAFFNAME"));
                log.put("paymenttime", resultSet.getString("PAYMENTTIME"));
                log.put("operaid", resultSet.getInt("OPERAID"));
                log.put("operaname", resultSet.getString("OPERANAME"));
                log.put("createtime", resultSet.getString("CREATETIME"));
                log.put("companyid", resultSet.getInt("COMPANYID"));
                log.put("status", resultSet.getInt("STATUS"));
                logs.add(log);
            }
        });
        return logs;
    }


    /**
     * 获取邀约日志
     */
    public List<JSONObject> getAppointLog(QueryVO queryVO) {
        int companyId = queryVO.getCompanyId();
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("kzid", queryVO.getKzId());
        String sql = " SELECT log.YYMEMO, log.IMGLIST, log.STAFFID, sf.NICKNAME, sf.HEADIMG, log.CREATETIME FROM "
                + DBSplitUtil.getInvitaLogTabName(companyId) +
                " log LEFT JOIN hm_pub_staff sf ON log.STAFFID = sf.ID AND sf.COMPANYID = :companyId " +
                " WHERE log.COMPANYID = :companyId AND log.KZID = :kzid ORDER BY log.CREATETIME DESC, log.ID DESC ";
        //执行查询
        final List<JSONObject> logs = new ArrayList<>();
        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                JSONObject log = new JSONObject();
                JSONArray list = new JSONArray();
                if (StringUtil.isNotEmpty(resultSet.getString("IMGLIST"))) {
                    list = JSONArray.parseArray(resultSet.getString("IMGLIST"));
                }
                log.put("yymemo", resultSet.getString("YYMEMO"));
                log.put("imglist", list);
                log.put("staffid", resultSet.getInt("STAFFID"));
                log.put("nickname", resultSet.getString("NICKNAME"));
                log.put("headimg", resultSet.getString("HEADIMG"));
                log.put("createtime", resultSet.getInt("CREATETIME"));
                logs.add(log);
            }
        });
        return logs;
    }


    /**
     * 获取权限
     */
    public HashMap<String, Object> getPmsMap(int staffId, int companyId, int collecterId,
                                             int appointId, int recepterId) {
        HashMap<String, Object> pmsMap = new HashMap<String, Object>();
        // 获取角色集
        String roleStr = getRoleStr(staffId, companyId);
        // 获取权限集
        String pmsStr = getPmsStr(staffId, companyId);
        //客服是否可以编辑接待结果
        boolean kfEditFlag = getKfEditJdRstFlag(companyId);
        // 主管纠错
        if (pmsStr.contains(",122,")) {
            pmsMap.put("editkz", true);
        } else {
            pmsMap.put("editkz", false);
        }

        // 推广渠道
        if (roleStr.contains("dscj") || roleStr.contains("glzx")) {
            if (pmsStr.contains(",97,")) {
                // 编辑个人
                if (staffId == collecterId) {
                    pmsMap.put("dscj", true);
                } else {
                    pmsMap.put("dscj", false);
                }
            } else if (pmsStr.contains(",98,")) {
                // 编辑本组
                if (staffId == collecterId) {
                    pmsMap.put("dscj", true);
                } else {
                    pmsMap.put("dscj", checkGroup(staffId, collecterId, "dscj", companyId));
                }
            } else if (pmsStr.contains(",125,")) {
                // 编辑部门
                if (staffId == collecterId) {
                    pmsMap.put("dscj", true);
                } else {
                    pmsMap.put("dscj", checkDepart(staffId, collecterId, "dscj", companyId));
                }
            } else {
                // 编辑所有
                pmsMap.put("dscj", true);
            }
        } else {
            pmsMap.put("dscj", false);
        }
        //电商邀约客服
        if (roleStr.contains("dsyy") || roleStr.contains("glzx")) {
            if (pmsStr.contains(",97,")) {
                // 编辑个人
                if (staffId == appointId) {
                    pmsMap.put("dsyy", true);
                } else {
                    pmsMap.put("dsyy", false);
                    pmsMap.put("dsyyreason", "该客资不是您的客资");
                }
            } else if (pmsStr.contains(",98,")) {
                // 编辑本组
                if (staffId == appointId) {
                    pmsMap.put("dsyy", true);
                } else {
                    boolean flag = checkGroup(staffId, appointId, "dsyy", companyId);
                    pmsMap.put("dsyy", flag);
                    if (!flag) {
                        pmsMap.put("dsyyreason", "该客资不是您所在小组的客资");
                    }
                }
            } else if (pmsStr.contains(",125,")) {
                // 编辑部门
                if (staffId == appointId) {
                    pmsMap.put("dsyy", true);
                } else {
                    boolean flag = checkDepart(staffId, appointId, "dsyy", companyId);
                    pmsMap.put("dsyy", flag);
                    if (!flag) {
                        pmsMap.put("dsyyreason", "该客资不是您所在部门的客资");
                    }
                }
            } else {
                // 编辑所有
                pmsMap.put("dsyy", true);
            }
        } else {
            pmsMap.put("dsyy", false);
            pmsMap.put("dsyyreason", "您不是电商客服，不能编辑客资状态");
        }
        //转介绍客服
        if (roleStr.contains("zjsyy") || roleStr.contains("glzx")) {
            if (pmsStr.contains(",97,")) {
                // 编辑个人
                if (staffId == appointId) {
                    pmsMap.put("zjsyy", true);
                } else {
                    pmsMap.put("zjsyy", false);
                    pmsMap.put("zjsyyreason", "该客资不是您的客资");
                }
            } else if (pmsStr.contains(",98,")) {
                // 编辑本组
                if (staffId == appointId) {
                    pmsMap.put("zjsyy", true);
                } else {
                    boolean flag = checkGroup(staffId, appointId, "zjsyy", companyId);
                    pmsMap.put("zjsyy", flag);
                    if (!flag) {
                        pmsMap.put("zjsyyreason", "该客资不是您所在小组的客资");
                    }
                }
            } else if (pmsStr.contains(",125,")) {
                // 编辑部门
                if (staffId == appointId) {
                    pmsMap.put("zjsyy", true);
                } else {
                    boolean flag = checkDepart(staffId, appointId, "zjsyy", companyId);
                    pmsMap.put("zjsyy", flag);
                    if (!flag) {
                        pmsMap.put("zjsyyreason", "该客资不是您所在部门的客资");
                    }
                }
            } else {
                // 编辑所有
                pmsMap.put("zjsyy", true);
            }
        } else {
            pmsMap.put("zjsyy", false);
            pmsMap.put("zjsyyreason", "您不是转介绍客服，不能编辑客资状态");
        }
        // 门市接待
        if (roleStr.contains("msjd") || roleStr.contains("glzx")) {
            if (pmsStr.contains(",97,")) {
                // 编辑个人
                if (staffId == recepterId || staffId == appointId) {
                    pmsMap.put("msjd", true);
                } else {
                    pmsMap.put("msjd", false);
                    pmsMap.put("msjdreason", "该客资不是您的客资");
                }
            } else if (pmsStr.contains(",98,")) {
                // 编辑本组
                if (staffId == recepterId || staffId == appointId) {
                    pmsMap.put("msjd", true);
                } else {
                    boolean flag = checkGroup(staffId, recepterId, "msjd", companyId);
                    pmsMap.put("msjd", flag);
                    if (!flag) {
                        pmsMap.put("msjdreason", "该客资不是您所在小组的客资");
                    }
                }
            } else if (pmsStr.contains(",125,")) {
                // 编辑部门
                if (staffId == recepterId || staffId == appointId) {
                    pmsMap.put("msjd", true);
                } else {
                    boolean flag = checkDepart(staffId, recepterId, "msjd", companyId);
                    pmsMap.put("msjd", flag);
                    if (!flag) {
                        pmsMap.put("msjdreason", "该客资不是您所在部门的客资");
                    }
                }
            } else {
                // 编辑所有
                pmsMap.put("msjd", true);
            }
        } else {
            pmsMap.put("msjd", false);
            pmsMap.put("msjdreason", "您不是门市接待，不能编辑客资到店情况");
        }

        // 客服是否可以修改接待结果
        if (kfEditFlag && roleStr.contains("yy") && !roleStr.contains("glzx")) {
            if (pmsMap.containsKey("dsyy") && (Boolean) pmsMap.get("dsyy")) {
                pmsMap.remove("msjd");
                pmsMap.put("msjd", true);
            } else if (pmsMap.containsKey("zjsyy") && (Boolean) pmsMap.get("zjsyy")) {
                pmsMap.remove("msjd");
                pmsMap.put("msjd", true);
            }
        }
        return pmsMap;
    }

    /**
     * 获取角色
     *
     * @param staffId
     * @param companyId
     * @return
     */
    private String getRoleStr(int staffId, int companyId) {
        String sql = "SELECT DISTINCT grp.GROUPTYPE FROM hm_pub_group grp"
                + " LEFT JOIN hm_pub_group_staff rela ON grp.GROUPID = rela.GROUPID AND rela.COMPANYID = :companyId "
                + " WHERE grp.COMPANYID = :companyId AND ( rela.STAFFID = :staffId " +
                "OR INSTR( CONCAT(',', grp.CHIEFIDS, ','), CONCAT(',', :staffId, ',') ) != 0 ) GROUP BY grp.GROUPTYPE";
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("staffId", staffId);

        final StringBuilder result = new StringBuilder();
        result.append(CommonConstant.STR_SEPARATOR);
        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                result.append(resultSet.getString("GROUPTYPE"));
                result.append(CommonConstant.STR_SEPARATOR);
            }
        });

        return result.toString().toLowerCase();
    }

    /**
     * 权限字符串
     *
     * @param staffId
     * @param companyId
     * @return
     */
    private String getPmsStr(int staffId, int companyId) {
        String sql = "SELECT DISTINCT pms.ID FROM hm_pub_staff_role rela "
                + " LEFT JOIN hm_pub_role role ON rela.ROLEID = role.ID AND role.COMPANYID = :companyId  "
                + " LEFT JOIN hm_pub_role_permission rp ON role.ID = rp.ROLEID AND rp.COMPANYID = :companyId  "
                + " LEFT JOIN hm_pub_permission pms ON rp.PERMISSIONID = pms.ID  "
                + " WHERE rela.COMPANYID = :companyId  AND rela.STAFFID = :staffId ";
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("staffId", staffId);

        final StringBuilder result = new StringBuilder();
        result.append(CommonConstant.STR_SEPARATOR);
        namedJdbc.query(sql, keyMap, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                result.append(resultSet.getString("ID"));
                result.append(CommonConstant.STR_SEPARATOR);
            }
        });

        return result.toString().toLowerCase();
    }

    /**
     * 获取客服是否可以编辑接待结果
     *
     * @param companyId
     * @return
     */
    private boolean getKfEditJdRstFlag(int companyId) {
        String sql = "SELECT KFEDITJDRST FROM hm_pub_company  WHERE ID = :companyId ";
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);

        return namedJdbc.queryForObject(sql, keyMap, boolean.class);

    }

    /**
     * 判断两个人是不是同一个小组
     *
     * @return
     */
    private boolean checkGroup(int staffId, int collecterId, String type, int companyId) {
        if (NumUtil.haveInvalid(staffId, collecterId, companyId) || StringUtil.isEmpty(type)) {
            return false;
        }
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("collecterId", collecterId);
        keyMap.put("type", type);
        keyMap.put("staffId", staffId);

        String sql = "SELECT COUNT(1) FROM hm_pub_group_staff rela LEFT JOIN " +
                " hm_pub_group grp ON rela.GROUPID = grp.GROUPID AND grp.COMPANYID = :companyId " +
                " WHERE ( rela.STAFFID = :staffId OR rela.STAFFID = :collecterId ) AND rela.COMPANYID = :companyId " +
                " AND grp.GROUPTYPE = :type GROUP BY grp.GROUPID HAVING COUNT(1) > 1 ";
        SqlRowSet sqlRowSet = namedJdbc.queryForRowSet(sql, keyMap);
        return sqlRowSet.next();
    }

    /**
     * 判断两个人是不是同一个部门
     *
     * @return
     */
    private boolean checkDepart(int staffId, int collecterId, String type, int companyId) {
        if (NumUtil.haveInvalid(staffId, collecterId, companyId) || StringUtil.isEmpty(type)) {
            return false;
        }
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("collecterId", collecterId);
        keyMap.put("type", type);
        keyMap.put("staffId", staffId);

        String sql = "SELECT COUNT(1) FROM hm_pub_group_staff rela" +
                "LEFT JOIN hm_pub_group grp ON rela.GROUPID = grp.GROUPID AND grp.COMPANYID = :companyId  " +
                "WHERE ( rela.STAFFID = :staffId  OR rela.STAFFID = :collecterId ) AND rela.COMPANYID = :companyId  " +
                "AND grp.GROUPTYPE = ? GROUP BY grp.PARENTID HAVING COUNT(1) > 1 ";

        SqlRowSet sqlRowSet = namedJdbc.queryForRowSet(sql, keyMap);
        return sqlRowSet.next();
    }

}
