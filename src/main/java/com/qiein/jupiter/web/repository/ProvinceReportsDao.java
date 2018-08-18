package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ChinaTerritoryConst;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.ProvinceAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.ProvinceDataDTO;
import com.qiein.jupiter.web.entity.dto.SourceClientDataDTO;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.ProvinceReportsVO;
import com.qiein.jupiter.web.entity.vo.ProvinceReportsVO2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @Auther: Tt(yehuawei)
 * @Date: 2018/8/15 16:07
 */
@Repository
public class ProvinceReportsDao {

    private static final String PLACEHOLDER = "$Dy$UpUp$";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 省域分析报表
     *
     * @param provinceAnalysisParamDTO
     * @param invalidConfig
     * @return
     */
    public List<ProvinceReportsVO2> provinceReport(final ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {

        List<ProvinceReportsVO> resultContent = new ArrayList<>();
        List<String> psn = new ArrayList<>();
        psn.addAll(ChinaTerritoryConst.TERRITORY_MAP.keySet());
        psn.add("total");
        for (String provinceName : psn) {    //遍历所有省
            Object[] objs = getParam(provinceAnalysisParamDTO, invalidConfig, provinceName);
            String sql = getFinalSQL(provinceAnalysisParamDTO, invalidConfig);
            //TODO 添加一个合计 然后查询出所有客资
            if (provinceName.equals("total")) {
                sql = sql.replace("AND INSTR(detail.ADDRESS, ? )> 0", " ");
                objs = getOtherParam(provinceAnalysisParamDTO, invalidConfig);
                System.out.println("合计sql: "+sql);
            }
            List<ProvinceReportsVO> now = jdbcTemplate.query(sql,
                    objs,
                    new RowMapper<ProvinceReportsVO>() {
                        @Override
                        public ProvinceReportsVO mapRow(ResultSet rs, int i) throws SQLException {
                            ProvinceReportsVO provinceReportsVO = new ProvinceReportsVO();
                            List<SourceClientDataDTO> list = new ArrayList<>();
                            do {
                                SourceClientDataDTO scd = new SourceClientDataDTO();
                                scd.setSrcId(rs.getInt("srcId"));
                                scd.setDataNum(rs.getInt("dataNum"));
                                scd.setSrcName(rs.getString("srcName"));
                                scd.setSrcImg(rs.getString("srcImg"));
                                list.add(scd);
                            } while (rs.next());
                            provinceReportsVO.setSourceData(list);
                            return provinceReportsVO;
                        }
                    });
            if (now.isEmpty()) {
                ProvinceReportsVO empty = new ProvinceReportsVO();
                now.add(empty);
            }

            now.get(0).setDataType(provinceAnalysisParamDTO.getSearchType());
            now.get(0).setProvinceName(provinceName);
            resultContent.addAll(now);
        }
        return total(transform(resultContent));
    }

    public static void main(String[] args) {
//        String sql = "SELECT info.SOURCEID srcId,src.SRCNAME ,src.SRCIMG , COUNT(1) dataNum  FROM hm_crm_client_info_2 info INNER JOIN hm_crm_client_detail_2 detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID  INNER JOIN hm_crm_source src ON src.ID = info.SOURCEID AND src.COMPANYID = info.COMPANYID WHERE info.ISDEL = 0 AND info.COMPANYID = 2 AND INSTR(detail.ADDRESS, ? )> 0  AND info.CREATETIME BETWEEN ? AND ?  GROUP BY info.SOURCEID ";
//        System.out.println("index: "+sql.indexOf("AND INSTR(detail.ADDRESS, ? )> 0"));
//        sql.replace("INSTR(detail.ADDRESS, ? )> 0","");
        String sql = "asd,";
        sql.replace(",", "");
        System.out.println(sql);
    }

    /**
     * 获取最终sql字符串
     *
     * @param provinceAnalysisParamDTO
     * @param invalidConfig
     * @return
     */
    private String getFinalSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        //将前端传来的查询数据类型转换成字段
        String finalSQL = setSearchTypeAndSQL(provinceAnalysisParamDTO, invalidConfig);
        System.out.println("输出本次最终sql: " + finalSQL);
        return finalSQL;
    }

    /**
     * 获取通用sql头部
     *
     * @param sb
     * @param provinceAnalysisParamDTO
     */
    private StringBuilder getBaseSQL(StringBuilder sb, ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        sb.append(" SELECT info.SOURCEID srcId,src.SRCNAME ,src.SRCIMG , COUNT(1) dataNum ")
                .append(" FROM hm_crm_client_info_" + provinceAnalysisParamDTO.getCompanyId() + " info")
                .append(" INNER JOIN hm_crm_client_detail_" + provinceAnalysisParamDTO.getCompanyId() + " detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID ")
                .append(" INNER JOIN hm_crm_source src ON src.ID = info.SOURCEID AND src.COMPANYID = info.COMPANYID")
                .append(" WHERE info.ISDEL = 0 AND info.COMPANYID = " + provinceAnalysisParamDTO.getCompanyId())
                .append(" AND INSTR(detail.ADDRESS, ? )> 0 ");
//        if (!provinceAnalysisParamDTO.getSearchType().equals("其他"))
//            sb.append(" AND INSTR(detail.ADDRESS, ? )> 0 ");
        return sb;
    }

    /**
     * 获取总客资量 allClientCount sql
     *
     * @param provinceAnalysisParamDTO
     */
    private StringBuilder getAllClientSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, provinceAnalysisParamDTO);
        sb.append(" AND info.CREATETIME BETWEEN ? AND ? ")
                .append(PLACEHOLDER);

        setConditionSQL(sb, provinceAnalysisParamDTO);
        return sb;
    }

    /**
     * 获取客资量 clientCount sql 客资量(总客资-筛选待定-筛选中-筛选无效)
     *
     * @param provinceAnalysisParamDTO
     */
    private StringBuilder getClientSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT zkz.srcId,zkz.SRCIMG,zkz.SRCNAME,IFNULL(zkz.dataNum,0) - IFNULL(sxdd.dataNum,0) - IFNULL(sxz.dataNum,0) - IFNULL(sxwxl.dataNum,0) dataNum")
                .append(" FROM ")
                .append(" (" + getAllClientSQL(provinceAnalysisParamDTO) + ") zkz ")     //总客资
                .append(" LEFT JOIN (" + getFilterPendingClientCount(provinceAnalysisParamDTO) + ") sxdd ON zkz.srcId = sxdd.srcId ")    //筛选待定
                .append(" LEFT JOIN (" + getFilterInClientCount(provinceAnalysisParamDTO) + ") sxz ON zkz.srcId = sxz.srcId ")          //筛选中
                .append(" LEFT JOIN (" + getFilterInValidClientCount(provinceAnalysisParamDTO) + ") sxwxl ON zkz.srcId = sxwxl.srcId ")  //筛选无效量
        ;

        setConditionSQL(sb, provinceAnalysisParamDTO);
        return sb;
    }

    /**
     * 获取有效量 validClientCount sql 有效量 =  总客资-无效-筛选待定-筛选中-筛选无效-待定量
     *
     * @param provinceAnalysisParamDTO
     */
    private StringBuilder getValidClientSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT zkz.srcId,zkz.SRCIMG,zkz.SRCNAME,IFNULL(zkz.dataNum,0) - IFNULL(sxdd.dataNum,0) - IFNULL(sxz.dataNum,0) - IFNULL(sxwxl.dataNum,0) - IFNULL(ddl.dataNum,0) dataNum ")
                .append(" FROM ")
                .append("(" + getAllClientSQL(provinceAnalysisParamDTO) + ") zkz , ")     //总客资
                .append("(" + getInValidClientSQL(provinceAnalysisParamDTO, dsInvalidVO) + ") wxl , ") //无效量
                .append("(" + getFilterPendingClientCount(provinceAnalysisParamDTO) + ") sxdd , ")     //筛选待定
                .append("(" + getFilterInClientCount(provinceAnalysisParamDTO) + ") sxz , ")   //筛选中
                .append("(" + getFilterInValidClientCount(provinceAnalysisParamDTO) + ") sxwxl , ")   //筛选无效量
                .append("(" + getPendingClientCount(provinceAnalysisParamDTO, dsInvalidVO) + ") ddl ");    //待定量

        setConditionSQL(sb, provinceAnalysisParamDTO);
        return sb;
    }

    /**
     * 获取入店量 comeShopClientCount sql
     *
     * @param provinceAnalysisParamDTO
     */
    private StringBuilder getComeShopClientSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, provinceAnalysisParamDTO);
        sb.append(" AND (info.COMESHOPTIME BETWEEN ? AND ?) ")
                .append(PLACEHOLDER);
        setConditionSQL(sb, provinceAnalysisParamDTO);
        return sb;
    }

    /**
     * 获取成交量 successClientCount sql
     *
     * @param provinceAnalysisParamDTO
     */
    private StringBuilder getSuccessClientSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, provinceAnalysisParamDTO);
        sb.append(" AND (info.SUCCESSTIME BETWEEN ? AND ?) ")
                .append(PLACEHOLDER);

        setConditionSQL(sb, provinceAnalysisParamDTO);
        return sb;
    }

    /**
     * 筛选待定
     *
     * @return
     */
    private StringBuilder getFilterPendingClientCount(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder filterPendingClientSQL = new StringBuilder();
        getBaseSQL(filterPendingClientSQL, provinceAnalysisParamDTO);
        filterPendingClientSQL.append(" AND info.CLASSID = 1 and info.STATUSID = 98 ")
                .append(" AND (info.CREATETIME BETWEEN ? AND ?) ")
                .append(PLACEHOLDER);
        return filterPendingClientSQL;
    }

    /**
     * 无效量
     *
     * @return
     */
    private StringBuilder getInValidClientSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder inValidClientSQL = new StringBuilder();
        getBaseSQL(inValidClientSQL, provinceAnalysisParamDTO);
        inValidClientSQL.append(" AND (info.CREATETIME BETWEEN ? AND ?) ");
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel())) {
            inValidClientSQL.append(" and (info.STATUSID in(" + dsInvalidVO.getDsInvalidStatus() + ") or");
            inValidClientSQL.append("   detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") )");
        }
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidStatus()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidLevel())) {
            inValidClientSQL.append(" and info.STATUSID in (" + dsInvalidVO.getDsInvalidStatus() + ")");
        }
        if (StringUtil.isNotEmpty(dsInvalidVO.getDsInvalidLevel()) && StringUtil.isEmpty(dsInvalidVO.getDsInvalidStatus())) {
            inValidClientSQL.append(" and detail.YXLEVEL IN(" + dsInvalidVO.getDsInvalidLevel() + ") ");
        }
        inValidClientSQL.append(PLACEHOLDER);
        return inValidClientSQL;
    }


    /**
     * 筛选无效量
     *
     * @return
     */
    private StringBuilder getFilterInValidClientCount(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder filterInValidClientSQL = new StringBuilder();
        getBaseSQL(filterInValidClientSQL, provinceAnalysisParamDTO);
        filterInValidClientSQL.append(" and info.CLASSID = 6 and info.STATUSID = 99 ")
                .append(" AND (info.CREATETIME BETWEEN ? AND ?) ")
                .append(PLACEHOLDER);
        return filterInValidClientSQL;
    }

    /**
     * 筛选中量
     */
    private StringBuilder getFilterInClientCount(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder filterInClienSQL = new StringBuilder();
        getBaseSQL(filterInClienSQL, provinceAnalysisParamDTO);
        filterInClienSQL.append(" and info.CREATETIME BETWEEN ? AND ? ")
                .append(" and info.CLASSID = 1 and info.STATUSID = 0 ")
                .append(PLACEHOLDER);
        return filterInClienSQL;
    }

    /**
     * 待定量
     *
     * @return
     */
    private StringBuilder getPendingClientCount(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder pendingClientSQL = new StringBuilder(); //"pendingClientCount"
        getBaseSQL(pendingClientSQL, provinceAnalysisParamDTO);
        pendingClientSQL.append(" AND (info.CREATETIME BETWEEN ? AND ?) ")
                .append(" AND INSTR( ? , CONCAT(',',info.STATUSID + '',',')) != 0")
                .append(PLACEHOLDER);
        return pendingClientSQL;
    }

    /**
     * 将前端传来的查询数据类型转换成字段,然后选择逻辑分支
     *
     * @param provinceAnalysisParamDTO
     */
    private String setSearchTypeAndSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        switch (provinceAnalysisParamDTO.getSearchType()) {
            case "总客资":
//                provinceAnalysisParamDTO.setSearchType("allClientCount");
                return getAllClientSQL(provinceAnalysisParamDTO).toString();
            case "客资量":
//                provinceAnalysisParamDTO.setSearchType("clientCount");
                return getClientSQL(provinceAnalysisParamDTO).toString();
            case "有效量":
//                provinceAnalysisParamDTO.setSearchType("validClientCount");
                return getValidClientSQL(provinceAnalysisParamDTO, invalidConfig).toString();
            case "入店量":
//                provinceAnalysisParamDTO.setSearchType("comeShopClientCount");
                return getComeShopClientSQL(provinceAnalysisParamDTO).toString();
            case "成交量":
//                provinceAnalysisParamDTO.setSearchType("successClientCount");
                return getSuccessClientSQL(provinceAnalysisParamDTO).toString();
            default:
                throw new RException(ExceptionEnum.SEARCH_TYPE_IS_UNKNOW);
        }
    }

    /**
     * 将sql字符串中所有的占位符替换成指定条件
     *
     * @param sb
     * @param searchKey
     * @return
     */
    private StringBuilder setConditionSQL(StringBuilder sb, ProvinceAnalysisParamDTO searchKey) {//查询的客资类型 1所有客资 2 电话客资 3 微信客资 4 qq客资 5 有电话有微信 6 无电话 7.无微信 8 只有电话 9只有微信 10 只有qq
        StringBuilder condition = new StringBuilder();
        String sourceIdSQL = "";
        if (StringUtil.isNotEmpty(searchKey.getSourceIds()))
            sourceIdSQL = " AND info.SOURCEID IN (" + searchKey.getSourceIds() + ") ";
        //客资类型
        condition.append(StringUtil.isNotEmpty(searchKey.getDicCodes()) ? " AND info.TYPEID IN (" + searchKey.getDicCodes() + ") " : "");
        //客资来源
        condition.append(sourceIdSQL);
        switch (searchKey.getSearchClientType()) {
            case 1: //1所有客资
                condition.append(" GROUP BY info.SOURCEID ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 2: //2电话客资
                condition.append(" AND (info.KZPHONE IS NOT NULL AND info.KZPHONE != '') ")
                        .append(" GROUP BY info.SOURCEID ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 3: //3微信客资
                condition.append(" AND (info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '') ")
                        .append(" GROUP BY info.SOURCEID ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 4: //4qq客资
                condition.append(" AND (info.KZQQ IS NOT NULL AND info.KZQQ != '') ")
                        .append(" GROUP BY info.SOURCEID ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 5: //5有电话有微信
                condition.append(" AND (info.KZPHONE IS NOT NULL AND info.KZPHONE != '') AND (info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '') ")
                        .append(" GROUP BY info.SOURCEID ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 6: //无电话
                condition.append(" AND (info.KZPHONE IS NULL OR info.KZPHONE = '') ")
                        .append(" GROUP BY info.SOURCEID ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 7: //7无微信
                condition.append(" AND (info.KZWECHAT IS NULL OR info.KZWECHAT = '') ")
                        .append(" GROUP BY info.SOURCEID ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 8: //8只有电话
                condition.append(" AND (info.KZPHONE IS NOT NULL AND info.KZPHONE != '') AND (info.KZWECHAT IS NULL OR info.KZWECHAT = '') AND (info.KZQQ IS NULL OR info.KZQQ = '')").append(" GROUP BY info.SOURCEID ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 9: //9只有微信
                condition.append(" AND (info.KZWECHAT IS NOT NULL AND info.KZWECHAT != '') AND (info.KZPHONE IS NULL OR info.KZPHONE = '') AND (info.KZQQ IS NULL OR info.KZQQ = '')")
                        .append(" GROUP BY info.SOURCEID ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            case 10://10只有qq
                condition.append(" AND (info.KZQQ IS NOT NULL AND info.KZQQ != '') AND (info.KZPHONE IS NULL OR info.KZPHONE = '') AND (info.KZWECHAT IS NULL OR info.KZWECHAT = '')")
                        .append(" GROUP BY info.SOURCEID ");
                while (sb.indexOf(PLACEHOLDER) >= 0) {
                    sb.replace(sb.indexOf(PLACEHOLDER), sb.indexOf(PLACEHOLDER) + PLACEHOLDER.length(), condition.toString());
                }
                break;
            default:
                break;
        }
        return sb;
    }

    /**
     * 获取参数obj数组
     *
     * @return
     */
    private Object[] getParam(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig, String provinceName) {
        switch (provinceAnalysisParamDTO.getSearchType()) {
            case "总客资":
                return new Object[]{provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd()};
            case "客资量":
                return new Object[]{provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd()};
            case "有效量":
                return new Object[]{provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(), invalidConfig.getDsDdStatus()};
            case "入店量":
                return new Object[]{provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd()};
            case "成交量":
                return new Object[]{provinceName, provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd()};
            default:
                throw new RException(ExceptionEnum.SEARCH_TYPE_IS_UNKNOW);
        }
    }

    private Object[] getOtherParam(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        switch (provinceAnalysisParamDTO.getSearchType()) {
            case "总客资":
                return new Object[]{provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd()};
            case "客资量":
                return new Object[]{provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd()};
            case "有效量":
                return new Object[]{provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(),
                        provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd(), invalidConfig.getDsDdStatus()};
            case "入店量":
                return new Object[]{provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd()};
            case "成交量":
                return new Object[]{provinceAnalysisParamDTO.getStart(), provinceAnalysisParamDTO.getEnd()};
            default:
                throw new RException(ExceptionEnum.SEARCH_TYPE_IS_UNKNOW);
        }
    }

    public static List<ProvinceReportsVO2> transform2(List<ProvinceReportsVO> vo1List) {
        List<ProvinceReportsVO2> vo2List = new ArrayList<>();
        Map<Integer, Integer> exist = new HashMap<>();   //key为srcId value为下标
//        List<Integer> exist = new ArrayList<>();
        int count = 0;
        //根据渠道名字去获取
        for (ProvinceReportsVO pr1 : vo1List) {   //想遍历省的数据
            String prName = pr1.getProvinceName();
            for (SourceClientDataDTO scd : pr1.getSourceData()) {
                if (!exist.containsKey(scd.getSrcId())) {   //如果这个来源不存在，生成一个
                    exist.put(scd.getSrcId(), count++);
//                    exist.add(scd.getSrcId());
                    ProvinceReportsVO2 newOne = new ProvinceReportsVO2();
                    newOne.setDataType(pr1.getDataType());
                    newOne.setSrcId(scd.getSrcId());
                    newOne.setSrcName(scd.getSrcName());
                    newOne.setSrcImg(scd.getSrcImg());
                    newOne.setProvinceDataList(new ArrayList<ProvinceDataDTO>());
                    newOne.getProvinceDataList().add(new ProvinceDataDTO(prName, scd.getDataNum()));
                    vo2List.add(newOne);
                } else { //如果存在，找到再插入
                    ProvinceReportsVO2 now = vo2List.get(exist.get(scd.getSrcId()));
                    now.getProvinceDataList().add(new ProvinceDataDTO(prName, scd.getDataNum()));
                }
            }
        }
        return vo2List;
    }

    public static List<ProvinceReportsVO2> transform(List<ProvinceReportsVO> vo1List) {
        List<ProvinceReportsVO2> vo2List = new ArrayList<>();
        Map<Integer, Integer> exist = new HashMap<>();   //key为srcId value为下标
//        List<Integer> exist = new ArrayList<>();
        int count = 0;
        //根据渠道名字去获取
        for (ProvinceReportsVO pr1 : vo1List) {   //想遍历省的数据
            for (SourceClientDataDTO scd : pr1.getSourceData()) {
                if (!exist.containsKey(scd.getSrcId())) {   //如果这个来源不存在，生成一个
                    exist.put(scd.getSrcId(), count++);
//                    exist.add(scd.getSrcId());
                    ProvinceReportsVO2 newOne = new ProvinceReportsVO2();
                    newOne.setDataType(pr1.getDataType());
                    newOne.setSrcId(scd.getSrcId());
                    newOne.setSrcName(scd.getSrcName());
                    newOne.setSrcImg(scd.getSrcImg());
//                    newOne.setProvinceDataList(new ArrayList<ProvinceDataDTO>());
//                    newOne.getProvinceDataList().add(new ProvinceDataDTO(prName,scd.getDataNum()));
                    newOne.setProvinceDataMap(new HashMap<String, Integer>());
                    newOne.getProvinceDataMap().put(pr1.getProvinceName(), scd.getDataNum());
                    vo2List.add(newOne);
                } else { //如果存在，找到再插入
                    ProvinceReportsVO2 now = vo2List.get(exist.get(scd.getSrcId()));
//                    now.getProvinceDataList().add(new ProvinceDataDTO(prName,scd.getDataNum()));
                    now.getProvinceDataMap().put(pr1.getProvinceName(), scd.getDataNum());
                }
            }
        }
        return vo2List;
    }

    /**
     * 合计 其他
     *
     * @param list
     */
    private List<ProvinceReportsVO2> total(List<ProvinceReportsVO2> list) {

        ProvinceReportsVO2 prv = new ProvinceReportsVO2();
        prv.setSrcName("合计");
        prv.setProvinceDataMap(new HashMap<String, Integer>());

        for (ProvinceReportsVO2 temp:list){
//            prv.setDataType(temp.getDataType());

            Map<String,Integer> map = temp.getProvinceDataMap();
            map.put("其他",map.get("total"));
            for (String key : map.keySet()){
//                if (!prv.getProvinceDataMap().containsKey(key)){
//                    prv.getProvinceDataMap().put(key,map.get(key));
//                }else {
//                    prv.getProvinceDataMap().put(key,prv.getProvinceDataMap().get(key)+map.get(key));
//                }
                if (key.equals("其他")||key.equals("total"))
                    continue;
                map.put("其他",map.get("其他")-map.get(key));
            }

        }

        for (ProvinceReportsVO2 temp:list){
            prv.setDataType(temp.getDataType());

            Map<String,Integer> map = temp.getProvinceDataMap();
            for (String key : map.keySet()){
                if (!prv.getProvinceDataMap().containsKey(key)){
                    prv.getProvinceDataMap().put(key,map.get(key));
                }else {
                    prv.getProvinceDataMap().put(key,prv.getProvinceDataMap().get(key)+map.get(key));
                }
            }

        }

        list.add(0, prv);

        return list;
    }
}
