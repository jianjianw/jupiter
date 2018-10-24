package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.enums.ContactTypeEnum;
import com.qiein.jupiter.enums.SexTypeEnum;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.util.*;
import com.qiein.jupiter.web.dao.DictionaryDao;
import com.qiein.jupiter.web.entity.vo.ClientVO;
import com.qiein.jupiter.web.service.ChannelService;
import com.qiein.jupiter.web.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客资编辑
 *
 * @Author: shiTao
 */
public class ClientEditDao {

    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    @Autowired
    private DictionaryDao dictionaryDao;

    /**
     * 推广客资编辑
     */
    public void editDstgClientInfo() {

    }

    /**
     * 客服客资编辑
     */
    public void editDsyyClientInfo() {
    }

    /**
     * 门市客资编辑
     */
    public void editMsClientInfo() {

    }

    /**
     * 主管纠错
     */
    public void editByAdmin() {

    }

    /**
     * 客资其他信息编辑
     */
    public void editClientOtherInfo() {
    }


    private void getUpdateLog(ClientVO clientVO, Map<String, Object> result) {

        boolean upFlag = false;

        StringBuilder logMemoBf = new StringBuilder();
        logMemoBf.append("修改了");

        int companyId = clientVO.getCompanyId();
        //查询参数
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put("companyId", companyId);
        keyMap.put("kzid", clientVO.getKzId());
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
        if (RegexUtil.isNumeric(clientVO.getKzId())) {
            sql.append(" AND info.ID = :kzid");
        } else {
            sql.append(" AND info.KZID = :kzid");
        }

        SqlRowSet rs = namedJdbc.queryForRowSet(sql.toString(), keyMap);
        rs.first();


        if (!clientVO.getKzName().equals(rs.getString("KZNAME"))) {
            upFlag = true;
            logMemoBf.append(" 客户姓名: " + rs.getString("KZNAME") + "，成为 "
                    + StringUtil.nullToStrTrim(clientVO.getKzName()) + " ；");
        }
        if (clientVO.getSex() != rs.getInt("SEX")) {
            upFlag = true;
            String sex = SexTypeEnum.getNameById(clientVO.getSex());
            String sexOld = SexTypeEnum.getNameById(rs.getInt("SEX"));
            logMemoBf.append(" 性别: " + sexOld + " 成为 " + sex + " ；");

        }
        if (!clientVO.getKzPhone().equals(rs.getString("KZPHONE"))) {
            // 记录修改日志
            addContactEditLog(rs.getInt("ID"), rs.getString("KZID"), clientVO.getCompanyId(),
                    ContactTypeEnum.KZ_PHONE.getTypeId(), rs.getString("KZPHONE"),
                    StringUtil.nullToStrTrim(clientVO.getKzPhone()), clientVO.getOperaId(), clientVO.getOperaName());
            upFlag = true;
            logMemoBf.append(" 手机号码: " + rs.getString("KZPHONE") + "，成为 "
                    + StringUtil.nullToStrTrim(clientVO.getKzPhone()) + " ；");
        }
        if (!clientVO.getKzQq().equals(rs.getString("KZQQ"))) {
            // 记录修改日志
            addContactEditLog(rs.getInt("ID"), rs.getString("KZID"), clientVO.getCompanyId(),
                    ContactTypeEnum.KZ_QQ.getTypeId(), rs.getString("KZQQ"),
                    StringUtil.nullToStrTrim(clientVO.getKzQq()), clientVO.getOperaId(), clientVO.getOperaName());
            upFlag = true;
            logMemoBf.append(" QQ： " + rs.getString("KZQQ") + "，成为 "
                    + StringUtil.nullToStrTrim(clientVO.getKzQq()) + " ；");
        }
        if (!clientVO.getKzWechat().equals(rs.getString("KZWECHAT"))) {
            // 记录修改日志
            addContactEditLog(rs.getInt("ID"), rs.getString("KZID"), clientVO.getCompanyId(),
                    ContactTypeEnum.KZ_WECHAT.getTypeId(), rs.getString("KZWECHAT"),
                    StringUtil.nullToStrTrim(clientVO.getKzWechat()), clientVO.getOperaId(), clientVO.getOperaName());
            upFlag = true;
            logMemoBf.append(" 微信： " + rs.getString("KZWECHAT") + "，成为 "
                    + StringUtil.nullToStrTrim(clientVO.getKzWechat()) + " ；");
        }
        if (!clientVO.getKzWw().equals(rs.getString("KZWW"))) {
            // 记录修改日志
            addContactEditLog(rs.getInt("ID"), rs.getString("KZID"), clientVO.getCompanyId(),
                    ContactTypeEnum.KZ_WW.getTypeId(), rs.getString("KZWW"),
                    StringUtil.nullToStrTrim(clientVO.getKzWw()), clientVO.getOperaId(), clientVO.getOperaName());
            upFlag = true;
            logMemoBf.append(" 旺旺： " + rs.getString("KZWW") + "，成为 "
                    + StringUtil.nullToStrTrim(clientVO.getKzWw()) + " ；");
        }
        if (!clientVO.getAddress().equals(rs.getString("ADDRESS"))) {
            upFlag = true;
            logMemoBf.append(" 地址: " + rs.getString("ADDRESS") + "，成为 "
                    + StringUtil.nullToStrTrim(clientVO.getAddress()) + " ；");
        }
        if (clientVO.getChannelId() != rs.getInt("CHANNELID")) {
            upFlag = true;
            logMemoBf.append(" 渠道: " + getChannelNameFromChannelId(clientVO.getCompanyId(), rs.getInt("CHANNELID"))
                    + "，成为 " + clientVO.getChannelName()
                    + " ；");
        }
        if (clientVO.getSourceId() != rs.getInt("SOURCEID")) {
            upFlag = true;
            logMemoBf.append(" 来源: " + getSrcLabelFromSrcId(clientVO.getCompanyId(), rs.getInt("SOURCEID"))
                    + "，成为 " + clientVO.getSourceName()
                    + " ；");
        }
        if (!clientVO.getAdId().equals(rs.getString("ADID"))) {
            upFlag = true;
            logMemoBf.append(" 广告ID: " + rs.getString("ADID") + "，成为 "
                    + StringUtil.nullToStrTrim(clientVO.getAdId()) + " ；");
        }
        if (!clientVO.getAdAddress().equals(rs.getString("ADADDRESS"))) {
            upFlag = true;
            logMemoBf.append(" 广告着陆页: " + rs.getString("ADADDRESS") + "，成为 "
                    + StringUtil.nullToStrTrim(clientVO.getAdAddress()) + " ；");
        }
        if (rs.getInt("TYPEID") != clientVO.getTypeId()) {
            upFlag = true;
            logMemoBf.append(" 咨询类型: " + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), rs.getInt("TYPEID"), DictionaryConstant.COMMON_TYPE).getDicName()
                    + "，成为 " + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), clientVO.getTypeId(), DictionaryConstant.COMMON_TYPE).getDicName() + " ；");
        }
        if (clientVO.getYxLevel() != rs.getInt("YXLEVEL")) {
            upFlag = true;
            logMemoBf.append(" 意向等级: " + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), rs.getInt("YXLEVEL"), DictionaryConstant.YX_RANK).getDicName() + "，成为 "
                    + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), clientVO.getYxLevel(), DictionaryConstant.YX_RANK).getDicName() + " ；");
        }
        if (clientVO.getYsRange() != rs.getInt("YSRANGE")) {
            upFlag = true;
            logMemoBf.append(" 预算范围: " + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), rs.getInt("YSRANGE"), DictionaryConstant.YS_RANGE).getDicName() + "，成为 "
                    + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), clientVO.getYsRange(), DictionaryConstant.YS_RANGE).getDicName() + " ；");
        }
        if (clientVO.getMarryTime() != rs.getInt("MARRYTIME")) {
            upFlag = true;
            logMemoBf.append(" 婚期时间: "
                    + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), rs.getInt("MARRYTIME"), DictionaryConstant.MARRY_TIME).getDicName()
                    + "，成为 " + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), clientVO.getMarryTime(), DictionaryConstant.MARRY_TIME).getDicName()
                    + " ；");
        }
        if (clientVO.getYpTime() != rs.getInt("YPTIME")) {
            upFlag = true;
            logMemoBf.append(" 预拍时间: "
                    + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), rs.getInt("YPTIME"), DictionaryConstant.YP_TIME).getDicName()
                    + "，成为 " + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), clientVO.getYpTime(), DictionaryConstant.YP_TIME).getDicName()
                    + " ；");
        }
        if (clientVO.getShopId() != rs.getInt("SHOPID")) {
            upFlag = true;
            logMemoBf.append(" 拍摄地: "
                    + StringUtil.nullToStrTrim(getShopNameById(rs.getInt("SHOPID"), clientVO.getCompanyId()))
                    + "，成为 " + StringUtil.nullToStrTrim(getShopNameById(clientVO.getCompanyId(), clientVO.getShopId())) + " ；");
        }
        if (!rs.getString("CONTENT").equals(clientVO.getRemark())) {
            result.put("flag", true);
            String msg = "";
            if ((StringUtil.isEmpty(StringUtil.replaceAllHTML(rs.getString("CONTENT"))) && StringUtil.isEmpty(StringUtil.replaceAllHTML(clientVO.getRemark())))
                    || StringUtil.replaceAllHTML(rs.getString("CONTENT")).equals(StringUtil.replaceAllHTML(clientVO.getRemark()))) {
                msg = clientVO.getOperaName() + "修改了编号为" + rs.getInt("ID") + "的客资推广聊天图片；";
            } else {
                msg = clientVO.getOperaName() + "修改了编号为" + rs.getInt("ID") + "的客资推广备注：" +
                        StringUtil.replaceAllHTML(rs.getString("CONTENT")) + "，成为：" + StringUtil.replaceAllHTML(clientVO.getRemark()) + "；";
            }
            result.put("msg", msg);
            upFlag = true;
            logMemoBf.append(" 推广备注: " + StringUtil.replaceAllHTML(rs.getString("CONTENT"))
                    + (CollectionUtils.isNotEmpty(StringUtil.getImgLists(rs.getString("CONTENT"))) ? StringUtil.getImgStrsWithImg(rs.getString("CONTENT")) : "")
                    + "，成为 "
                    + StringUtil.replaceAllHTML(clientVO.getRemark())
                    + (CollectionUtils.isNotEmpty(StringUtil.getImgLists(clientVO.getRemark())) ? StringUtil.getImgStrsWithImg(clientVO.getRemark()).replaceAll("\\u0024", "，") : "")
                    + " ；");
        }
        if (clientVO.getZxStyle() != rs.getInt("ZXSTYLE")) {
            upFlag = true;
            logMemoBf.append(" 咨询方式: " + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), rs.getInt("ZXSTYLE"), DictionaryConstant.ZX_STYLE).getDicName() + "，成为 "
                    + dictionaryDao.getByCodeAndTypeAndCid(clientVO.getCompanyId(), clientVO.getZxStyle(), DictionaryConstant.ZX_STYLE).getDicName() + " ；");
        }
        if (!rs.getString("KEYWORD").equals(clientVO.getKeyWord())) {
            upFlag = true;
            logMemoBf.append(" 关键词: " + rs.getString("KEYWORD") + "，成为 "
                    + StringUtil.nullToStrTrim(clientVO.getKeyWord()) + " ；");
        }
        if (upFlag) {
            clientVO.setLog(logMemoBf.toString());
        }

    }


    /**
     * 更新客资信息
     *
     * @return
     */
    private void updateClientInfo(ClientVO vo) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE ");
        sb.append(DBSplitUtil.getTable(TableEnum.info, vo.getCompanyId()));
        sb.append(" SET ");


        sb.append(" KZNAME = :kzName, ");
        map.put("kzName", vo.getKzName());
        sb.append(" SEX = :sex, ");
        map.put("sex", vo.getSex());
        sb.append(" KZPHONE = :kzPhone, ");
        map.put("kzPhone", StringUtil.emptyToNull(vo.getKzPhone()));
        sb.append(" KZQQ = :kzQq, ");
        map.put("kzQq", StringUtil.emptyToNull(vo.getKzQq()));
        sb.append(" KZWECHAT = :kzWeChat, ");
        map.put("kzWeChat", StringUtil.emptyToNull(vo.getKzWechat()));
        sb.append(" KZWW = :kzWw, ");
        map.put("kzWw", StringUtil.emptyToNull(vo.getKzWw()));
        sb.append(" SRCTYPE = :srcType, ");
        map.put("srcType", vo.getSrcType());
        sb.append(" TYPEID = :typeId , ");
        map.put("typeId", vo.getTypeId());
        sb.append(" SHOPID = :shopId, ");
        map.put("shopId", vo.getShopId());
        if (NumUtil.isValid(vo.getChannelId())) {
            sb.append(" CHANNELID = :channelId, ");
            map.put("channelId", vo.getChannelId());
        }
        if (NumUtil.isValid(vo.getSourceId())) {
            sb.append(" SOURCEID = :sourceId, ");
            map.put("sourceId", vo.getSourceId());
        }
        sb.append(" UPDATETIME = UNIX_TIMESTAMP(NOW()) ");

        sb.append(" WHERE KZID = :kzId AND COMPANYID = :companyId AND ISDEL = 0 ");
        map.put("kzId", vo.getKzId());
        map.put("companyId", vo.getCompanyId());

        namedJdbc.update(sb.toString(), map);
    }


    /**
     * 更新详情
     */
    private void updateClientDetail(ClientVO vo) {
        Map<String, Object> map = new HashMap<>();
        String remark = vo.getRemark();
        if (vo.getRemarkForm().equals("tg")) {
            vo.setRemark("");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE ");
        sb.append(DBSplitUtil.getTable(TableEnum.detail, vo.getCompanyId()));
        sb.append(" SET ");


        sb.append(" ADDRESS = :address, ");
        map.put("address", vo.getAddress());
        if (StringUtil.isNotEmpty(vo.getShopName())) {
            sb.append(" SHOPNAME = :shopName, ");
            map.put("shopName", vo.getShopName());
        }
        sb.append(" YXLEVEL = :yeLevel, ");
        map.put("yeLevel", vo.getYxLevel());
        sb.append(" YSRANGE = :ysRange, ");
        map.put("ysRange", vo.getYsRange());
        if (NumUtil.isValid(vo.getZxStyle())) {
            sb.append(" ZXSTYLE = :zxStyle, ");
            map.put("zxStyle", vo.getZxStyle());
        }
        sb.append(" ADADDRESS = :adAddress, ");
        map.put("adAddress", vo.getAdAddress());
        sb.append(" ADID = :adId, ");
        map.put("adId", vo.getAdId());
        sb.append(" MARRYTIME = :marryTime, ");
        map.put("marryTime", vo.getMarryTime());
        sb.append(" YPTIME = :ypTime, ");
        map.put("ypTime", vo.getYpTime());
        sb.append(" KEYWORD = :keyWord, ");
        map.put("keyWord", vo.getKeyWord());
        if (StringUtil.isNotEmpty(StringUtil.replaceAllHTML(vo.getRemark()))) {
            sb.append(" MEMO = :memo, ");
            map.put("memo", StringUtil.subStr(StringUtil.replaceAllHTML(vo.getRemark()), 255));
        }
        List<String> imgList = StringUtil.getImgLists(vo.getRemark());
        if (CollectionUtils.isNotEmpty(imgList)) {
            sb.append(" TALKIMG = :talkImg, ");
            map.put("talkImg", imgList.get(0));
        }
        sb.append(" ISDEL = 0  WHERE KZID = :kzId AND COMPANYID = :companyId AND ISDEL = 0 ");
        map.put("kzId", vo.getKzId());
        map.put("companyId", vo.getCompanyId());

        namedJdbc.update(sb.toString(), map);
        vo.setRemark(remark);
    }


    /**
     * 新增操作日志
     */
    private void addUpdateLog() {

    }


    /**
     * 更新推广备注
     *
     * @param vo
     */
    private void editRemarkLog(ClientVO vo) {
        Map<String, Object> map = new HashMap<>();
        map.put("remake", vo.getRemark());
        map.put("companyId", vo.getCompanyId());
        map.put("kzId", vo.getKzId());

        String sql = "UPDATE hm_crm_client_remark  SET CONTENT = :remake  " +
                "WHERE COMPANYID = :companyId AND KZID = :kzId  ";

        namedJdbc.update(sql, map);
    }


    /**
     * 记录联系方式修改日志
     *
     * @param id
     * @param kzId
     * @param companyId
     * @param typeId
     * @param before
     * @param after
     * @param operaId
     */
    private void addContactEditLog(int id, String kzId, int companyId, int typeId, String before, String after,
                                   int operaId, String operaName) {

        if (NumUtil.isInValid(id) || StringUtil.isEmpty(kzId) || NumUtil.isInValid(companyId)
                || NumUtil.isInValid(operaId) || StringUtil.isEmpty(operaName)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        String sql = " INSERT INTO hm_crm_contact_edit_log ( KZNUM, KZID, TYPEID, TYPENAME, BEFOREVALUE," +
                " AFTERVALUE, OPERAID, OPERANAME, OPERATIME, COMPANYID )" +
                " VALUE (:id, :kzId, :typeId, :typeName, :beforeVal, :afterVal, :operaId, :operaName," +
                " UNIX_TIMESTAMP(NOW()), :companyId )  ";
        map.put("id", id);
        map.put("kzId", kzId);
        map.put("typeId", typeId);
        map.put("typeName", ContactTypeEnum.getTypeNameById(typeId));
        map.put("beforeVal", before);
        map.put("afterVal", after);
        map.put("operaId", operaId);
        map.put("operaName", operaName);
        map.put("companyId", companyId);

        namedJdbc.update(sql, map);

    }


    /**
     * 根据ID 获取渠道名称
     *
     * @param companyId
     * @param channelId
     * @return
     */
    private String getChannelNameFromChannelId(int companyId, int channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put("companyId", companyId);
        map.put("id", channelId);
        String sql = "SELECT channel.CHANNELNAME FROM hm_crm_channel channel WHERE channel.ID = :id AND channel.COMPANYID = :companyId";
        String channelName = namedJdbc.queryForObject(sql, map, String.class);
        return channelName;
    }

    /**
     * 根据ID 获取来源
     *
     * @return
     */
    private String getSrcLabelFromSrcId(int companyId, int srcId) {
        Map<String, Object> map = new HashMap<>();
        map.put("companyId", companyId);
        map.put("id", srcId);
        String sql = "SELECT src.SRCNAME FROM hm_crm_source src WHERE src.ID = :id AND src.COMPANYID = :companyId";
        String name = namedJdbc.queryForObject(sql, map, String.class);
        return name;
    }

    /**
     * 根据ID 获取门店名称
     *
     * @return
     */
    private String getShopNameById(int companyId, int shopId) {
        Map<String, Object> map = new HashMap<>();
        map.put("companyId", companyId);
        map.put("id", shopId);
        String sql = "SELECT sp.SHOPNAME FROM hm_pub_shop sp WHERE sp.ID = :id AND sp.COMPANYID = :companyId";
        String name = namedJdbc.queryForObject(sql, map, String.class);
        return name;
    }
}
