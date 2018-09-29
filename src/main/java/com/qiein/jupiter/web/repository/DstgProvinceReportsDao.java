package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.constant.ChinaTerritoryConst;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.dto.ProvinceAnalysisParamDTO;
import com.qiein.jupiter.web.entity.dto.ProvinceReportsVO3;
import com.qiein.jupiter.web.entity.dto.SourceClientDataDTO;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.ProvinceReportsVO;
import com.qiein.jupiter.web.entity.vo.ProvinceReportsVO2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 电商推广 省域数据分析报表
 *
 * @Auther: Tt(yehuawei)
 * @Date: 2018/9/26 18:58
 */
@Repository
public class DstgProvinceReportsDao {
    /**
     * dys占位符
     */
    private static final String PLACEHOLDER = "$Dy$UpUp$";

    @Autowired
    private NamedParameterJdbcOperations namedJdbc;

    public Map<String,Object> getDstgProvinceReports(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        return getDstgProvinceReports(new ArrayList<SourceClientDataDTO>(), provinceAnalysisParamDTO, invalidConfig);
    }

    /**
     * 获取电商推广省域报表分析
     *
     * @param listData
     * @param provinceAnalysisParamDTO
     * @param invalidConfig
     * @return
     */
    public Map<String,Object> getDstgProvinceReports(final List<SourceClientDataDTO> listData, ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        //返回结果对象
        List<ProvinceReportsVO> resultContent = new ArrayList<>();
        //获取待查询的省份列表
        List<String> list = new ArrayList<>();
        list.addAll(ChinaTerritoryConst.TERRITORY_MAP.keySet());
//        list.add("total");

        //获取sql
        String sql = getSQL(provinceAnalysisParamDTO, invalidConfig);
        System.out.println("本次输出sql: " + sql);
        //循环执行所有省份的指定参数
        for (final String provinceName : list) {
//            //计算合计
//            if (provinceName.equals("total")) {
//                //获取合计渠道参数
//                total(listData, provinceAnalysisParamDTO, invalidConfig, sql.replace("AND INSTR(detail.ADDRESS, :address )> 0", " "));
//            }

            Map<String, Object> paramMap = getParamMap(provinceAnalysisParamDTO, invalidConfig, provinceName);

            List<ProvinceReportsVO3> queryData = namedJdbc.query(sql, paramMap, new RowMapper<ProvinceReportsVO3>() {
                @Override
                public ProvinceReportsVO3 mapRow(ResultSet rs, int rowNum) throws SQLException {
                    do {
                        SourceClientDataDTO scd = new SourceClientDataDTO();
                        scd.setProvinceName(provinceName);
                        scd.setSrcId(rs.getInt("srcId"));
                        scd.setDataNum(rs.getString("dataNum"));
                        scd.setSrcName(rs.getString("srcName"));
                        scd.setSrcImg(rs.getString("srcImg"));

                        listData.add(scd);

                    } while (rs.next());
                    return null;
                }
            });

        }

//        //获取合计渠道参数
//        total(listData, provinceAnalysisParamDTO, invalidConfig, sql.replace("AND INSTR(detail.ADDRESS, :address )> 0", " "));

        //获取合计渠道参数
        htotal(listData, provinceAnalysisParamDTO, invalidConfig, sql.replace("AND INSTR(detail.ADDRESS, :address )> 0", " "));
        //TODO 进行 转换 其它（合计-所有渠道） 排序
        if (provinceAnalysisParamDTO.getSearchType().contains("量") || provinceAnalysisParamDTO.getSearchType().equals("总客资")) {
            return calculateOtherDate(listData);
        } else {
            //TODO 计算其它
            otherR(listData, provinceAnalysisParamDTO, invalidConfig, sql.replace("AND INSTR(detail.ADDRESS, :address )> 0", "AND detail.ADDRESS NOT IN (:addressList)"),list);
            return calculateOtherRate(listData);
        }
    }

    /**
     * 各个渠道的合计合计
     *
     * @param listData
     * @param provinceAnalysisParamDTO
     * @param invalidConfig
     * @param sql
     */
    private void htotal(final List<SourceClientDataDTO> listData, ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig, String sql) {
        System.out.println("合计sql: " + sql);
        Map<String, Object> paramMap = getParamMap(provinceAnalysisParamDTO, invalidConfig, "");
        List<ProvinceReportsVO3> queryData = namedJdbc.query(sql, paramMap, new RowMapper<ProvinceReportsVO3>() {
            @Override
            public ProvinceReportsVO3 mapRow(ResultSet rs, int rowNum) throws SQLException {
                do {
                    SourceClientDataDTO scd = new SourceClientDataDTO();
                    scd.setProvinceName("合计");
                    scd.setSrcId(rs.getInt("srcId"));
                    scd.setDataNum(rs.getString("dataNum"));
                    scd.setSrcName(rs.getString("srcName"));
                    scd.setSrcImg(rs.getString("srcImg"));

                    listData.add(scd);

                } while (rs.next());
                return null;
            }
        });
    }

    /**
     * 各个省域的数据统计
     *
     * @param listData
     * @return
     */
    private List<String> ztotal(List<ProvinceReportsVO2> listData) {
        ProvinceReportsVO2 hj = new ProvinceReportsVO2();
        listData.add(0, hj);
        hj.setSrcName("合计");
        Map<String, String> map = hj.getProvinceDataMap();
        for (ProvinceReportsVO2 prv : listData) {  //遍历所有渠道
            Map<String, String> proMap = prv.getProvinceDataMap();
            //遍历所有key，如果合计中包涵这个key就累加 如果合计中不包含 就直接put
            for (String provinceName : proMap.keySet()) {
                if (map.containsKey(provinceName)) {
                    map.put(provinceName, String.valueOf(Integer.valueOf(map.get(provinceName)) + Integer.valueOf(proMap.get(provinceName))));
                } else {
                    map.put(provinceName, proMap.get(provinceName));
                }
            }
        }
        List<String> head = new ArrayList<>();
        //遍历所有key
        for (String key : map.keySet()) {
            //获取当前key对应的value
            Integer now = Integer.valueOf(map.get(key));
            boolean flag =false;
            //遍历所有排序字符串列表
            for (int i = 0, len = head.size(); i < len && !head.isEmpty(); i++) {
                flag=false;
                //如果比当前数要大，就占用当前的index
                if ((!head.isEmpty())&&Integer.compare((int)now,(int)Integer.valueOf(map.get(head.get(i))))>0) {
                    head.add(i,key);
                    flag=true;
                    break;
                    //如果不如当前大就到下一个
                }
            }
            if (!flag){
                head.add(key);
            }
        }
        return head;
    }


    /**
     * 横纵转换
     *
     * @param listData
     * @return
     */
    private List<ProvinceReportsVO2> changeVO(List<SourceClientDataDTO> listData) {
        List<ProvinceReportsVO2> resultList = new LinkedList<>();
        ProvinceReportsVO2 prv = null;
        boolean flag;
        for (SourceClientDataDTO scd : listData) { //遍历所有省份渠道信息
            flag = false;
            for (ProvinceReportsVO2 prv2 : resultList) { //遍历返回结果列表
                if (prv2.getSrcId().equals(scd.getSrcId())) {//比较渠道 如果是 则插入其map中 ,如果不存在，生成一个对象
                    flag = true;
                    prv2.getProvinceDataMap().put(scd.getProvinceName(), scd.getDataNum());
                }
                if (flag) {
                    break;
                }
            }
            if (!flag) {
                prv = new ProvinceReportsVO2();
                prv.setSrcId(scd.getSrcId());
                prv.setSrcName(scd.getSrcName());
                prv.setSrcImg(scd.getSrcImg());
                Map<String, String> map = prv.getProvinceDataMap();
                map.put(scd.getProvinceName(), scd.getDataNum());
                resultList.add(prv);
            }
        }
        return resultList;
    }


    /**
     * 对int值的指标进行计算其它地域指标和排序
     *
     * @param listData
     * @return
     */
    private Map<String,Object> calculateOtherDate(List<SourceClientDataDTO> listData) {
        List<ProvinceReportsVO2> list = changeVO(listData);
        for (ProvinceReportsVO2 prv2 : list) {
            Map<String, String> map = prv2.getProvinceDataMap();
            String other = map.get("合计");
            for (String key : map.keySet()) {
                if (key.equals("合计"))
                    continue;
                other = String.valueOf(Integer.valueOf(other) - Integer.valueOf(map.get(key)));
            }
            map.put("其它", other);
        }
        List<String> head = ztotal(list);
        Collections.sort(list);
        Map<String,Object> map = new HashMap<>();
        map.put("head",head);
        map.put("list",list);
        return map;
    }

    private Map<String,Object> otherR(final List<SourceClientDataDTO> listData, ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig, String sql,List<String> list){
        System.out.println("其它sql: " + sql);
        Map<String, Object> paramMap = getParamMap(provinceAnalysisParamDTO, invalidConfig, "其它");
        paramMap.put("addressList",list);
        System.out.println(paramMap);
        List<ProvinceReportsVO3> queryData = namedJdbc.query(sql, paramMap, new RowMapper<ProvinceReportsVO3>() {
            @Override
            public ProvinceReportsVO3 mapRow(ResultSet rs, int rowNum) throws SQLException {
                do {
                    SourceClientDataDTO scd = new SourceClientDataDTO();
                    scd.setProvinceName("其它");
                    scd.setSrcId(rs.getInt("srcId"));
                    scd.setDataNum(rs.getString("dataNum"));
                    scd.setSrcName(rs.getString("srcName"));
                    scd.setSrcImg(rs.getString("srcImg"));

                    listData.add(scd);

                } while (rs.next());
                return null;
            }
        });
        return null;
    }

    /**
     * 对double率的指标进行计算其它地域和排序
     *
     * @param listData
     * @return
     */
    private Map<String,Object> calculateOtherRate(List<SourceClientDataDTO> listData) {
        List<ProvinceReportsVO2> list = changeVO(listData);
        List<String> head = new ArrayList<>();
        for (ProvinceReportsVO2 prv:list){
            for (String address:prv.getProvinceDataMap().keySet()){
                if(!head.contains(address)&&!address.equals("合计"))
                    head.add(address);
            }
        }
        head.add(0,"合计");

        Map<String,Object> map = new HashMap<>();
        map.put("head",head);
        map.put("list",list);
        return map;
    }

    /**
     * 获取参数Map
     *
     * @return
     */
    private Map<String, Object> getParamMap(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig, String provinceName) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("address", provinceName);
        paramMap.put("start", provinceAnalysisParamDTO.getStart());
        paramMap.put("end", provinceAnalysisParamDTO.getEnd());
        paramMap.put("dsDdStatus", invalidConfig.getDsDdStatus());
        paramMap.put("companyId", provinceAnalysisParamDTO.getCompanyId());
        return paramMap;
    }

    /**
     * 根据请求的类型获取相对的sql
     *
     * @param provinceAnalysisParamDTO
     * @param invalidConfig
     * @return
     */
    private String getSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        switch (provinceAnalysisParamDTO.getSearchType()) {
            case "总客资":
                return getAllClientSQL(provinceAnalysisParamDTO).toString();
            case "客资量":
                return getClientSQL(provinceAnalysisParamDTO).toString();
            case "有效量":
                return getValidClientSQL(provinceAnalysisParamDTO, invalidConfig).toString();
            case "入店量":
                return getComeShopClientSQL(provinceAnalysisParamDTO).toString();
            case "成交量":
                return getSuccessClientSQL(provinceAnalysisParamDTO).toString();
            case "无效量":
                return getInValidClientCountSQL(provinceAnalysisParamDTO, invalidConfig).toString();
            case "待定量":
                return getPendingClientSQL(provinceAnalysisParamDTO, invalidConfig).toString();
            case "有效率":
                return getValidRateSQL(provinceAnalysisParamDTO, invalidConfig).toString();
            case "无效率":
                return getInValidRateSQL(provinceAnalysisParamDTO, invalidConfig).toString();
            case "待定率":
                return getWaitRateSQL(provinceAnalysisParamDTO, invalidConfig).toString();
            case "毛客资入店率":
                return getClientComeShopRateSQL(provinceAnalysisParamDTO).toString();
            case "有效客资入店率":
                return getValidClientComeShopRate(provinceAnalysisParamDTO, invalidConfig).toString();
            case "入店成交率":
                return getComeShopSuccessRate(provinceAnalysisParamDTO).toString();
            case "毛客资成交率":
                return getClientSuccessRate(provinceAnalysisParamDTO).toString();
            case "有效客资成交率":
                return getValidClientSuccessRate(provinceAnalysisParamDTO, invalidConfig).toString();
//            case "营业额":
//                return getAmountSQL(provinceAnalysisParamDTO).toString();
//            case "成交均价":
//                return getAvgAmountSQL(provinceAnalysisParamDTO).toString();
            default:
                throw new RException(ExceptionEnum.SEARCH_TYPE_IS_UNKNOW);
        }
    }

    /**
     * 获取通用sql头部
     *
     * @param sb
     * @param provinceAnalysisParamDTO
     */
    private StringBuilder getBaseSQL(StringBuilder sb, ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        sb.append(" SELECT info.SOURCEID srcId,src.SRCNAME ,src.SRCIMG , COUNT(1) dataNum ")
                .append(" FROM  " + DBSplitUtil.getInfoTabName(provinceAnalysisParamDTO.getCompanyId()) + " info")
                .append(" INNER JOIN  " + DBSplitUtil.getDetailTabName(provinceAnalysisParamDTO.getCompanyId()) + " detail ON info.KZID = detail.KZID AND info.COMPANYID = detail.COMPANYID ")
                .append(" INNER JOIN hm_crm_source src ON src.ID = info.SOURCEID AND src.COMPANYID = info.COMPANYID")
                .append(" WHERE info.ISDEL = 0 AND info.COMPANYID = :companyId")
                .append(" AND info.SRCTYPE IN (1,2) ")
                .append(" AND INSTR(detail.ADDRESS, :address )> 0 ");
        return sb;
    }

    /**
     * 将sql中的占位符替换为对应的条件子句
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
     * 获取总客资量 allClientCount sql
     *
     * @param provinceAnalysisParamDTO
     */
    private StringBuilder getAllClientSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder sb = new StringBuilder();

        getBaseSQL(sb, provinceAnalysisParamDTO);

        sb.append(" AND info.CREATETIME BETWEEN :start AND :end ")
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
                .append(" LEFT JOIN (" + getFilterInValidClientCount(provinceAnalysisParamDTO) + ") sxwxl ON zkz.srcId = sxwxl.srcId ");  //筛选无效量

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
        filterPendingClientSQL.append(" AND info.STATUSID = 98 ")
                .append(" AND (info.CREATETIME BETWEEN :start AND :end) ")
                .append(PLACEHOLDER);
        return filterPendingClientSQL;
    }

    /**
     * 筛选中量
     */
    private StringBuilder getFilterInClientCount(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder filterInClienSQL = new StringBuilder();
        getBaseSQL(filterInClienSQL, provinceAnalysisParamDTO);
        filterInClienSQL.append(" and info.CREATETIME BETWEEN :start AND :end ")
                .append(" and info.STATUSID = 0 ")
                .append(PLACEHOLDER);
        return filterInClienSQL;
    }

    /**
     * 筛选无效量
     *
     * @return
     */
    private StringBuilder getFilterInValidClientCount(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder filterInValidClientSQL = new StringBuilder();
        getBaseSQL(filterInValidClientSQL, provinceAnalysisParamDTO);
        filterInValidClientSQL.append(" and info.STATUSID = 99 ")
                .append(" AND (info.CREATETIME BETWEEN :start AND :end) ")
                .append(PLACEHOLDER);
        return filterInValidClientSQL;
    }

    /**
     * 获取有效量 validClientCount sql 有效量 =  总客资-无效-筛选待定-筛选中-筛选无效-待定量
     *
     * @param provinceAnalysisParamDTO
     */
    private StringBuilder getValidClientSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder sb = new StringBuilder();
        if (dsInvalidVO.getDdIsValid()) { //有效量(总客资-无效量-筛选中-筛选无效-筛选待定)
            sb.append("SELECT zkz.srcId,zkz.SRCIMG,zkz.SRCNAME,IFNULL(zkz.dataNum,0) - IFNULL(sxdd.dataNum,0) - IFNULL(sxz.dataNum,0) - IFNULL(sxwxl.dataNum,0) dataNum ")
                    .append(" FROM ")
                    .append("(" + getAllClientSQL(provinceAnalysisParamDTO) + ") zkz  ")     //总客资
                    .append(" LEFT JOIN (" + getInValidClient(provinceAnalysisParamDTO, dsInvalidVO) + ") wxl ON zkz.srcId = wxl.srcId ") //无效量
                    .append(" LEFT JOIN (" + getFilterPendingClientCount(provinceAnalysisParamDTO) + ") sxdd ON zkz.srcId = sxdd.srcId ")     //筛选待定
                    .append(" LEFT JOIN (" + getFilterInClientCount(provinceAnalysisParamDTO) + ") sxz N zkz.srcId = sxz.srcId ")   //筛选中
                    .append(" LEFT JOIN (" + getFilterInValidClientCount(provinceAnalysisParamDTO) + ") sxwxl N zkz.srcId = sxwxl.srcId ");   //筛选无效量
        } else { // 有效量(总客资-无效量-筛选中-筛选无效-筛选待定-待定量)
            sb.append("SELECT zkz.srcId,zkz.SRCIMG,zkz.SRCNAME,IFNULL(zkz.dataNum,0) - IFNULL(sxdd.dataNum,0) - IFNULL(sxz.dataNum,0) - IFNULL(sxwxl.dataNum,0) - IFNULL(ddl.dataNum,0) dataNum ")
                    .append(" FROM ")
                    .append("(" + getAllClientSQL(provinceAnalysisParamDTO) + ") zkz  ")     //总客资
                    .append(" LEFT JOIN (" + getInValidClient(provinceAnalysisParamDTO, dsInvalidVO) + ") wxl ON zkz.srcId = wxl.srcId ") //无效量
                    .append(" LEFT JOIN (" + getFilterPendingClientCount(provinceAnalysisParamDTO) + ") sxdd ON zkz.srcId = sxdd.srcId ")     //筛选待定
                    .append(" LEFT JOIN (" + getFilterInClientCount(provinceAnalysisParamDTO) + ") sxz ON zkz.srcId = sxz.srcId ")   //筛选中
                    .append(" LEFT JOIN (" + getFilterInValidClientCount(provinceAnalysisParamDTO) + ") sxwxl ON zkz.srcId = sxwxl.srcId ")   //筛选无效量
                    .append(" LEFT JOIN (" + getPendingClientCount(provinceAnalysisParamDTO, dsInvalidVO) + ") ddl ON zkz.srcId = ddl.srcId");    //待定量
        }


        setConditionSQL(sb, provinceAnalysisParamDTO);
        return sb;
    }

    /**
     * 无效量
     *
     * @return
     */
    private StringBuilder getInValidClient(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder inValidClientSQL = new StringBuilder();
        getBaseSQL(inValidClientSQL, provinceAnalysisParamDTO);
        inValidClientSQL.append(" AND (info.CREATETIME BETWEEN :start AND :end) ");
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
     * 待定量
     *
     * @return
     */
    private StringBuilder getPendingClientCount(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder pendingClientSQL = new StringBuilder();
        getBaseSQL(pendingClientSQL, provinceAnalysisParamDTO);
        pendingClientSQL.append(" AND (info.CREATETIME BETWEEN :start AND :end) ")
                .append(" AND INSTR( :dsDdStatus , CONCAT('\"',info.STATUSID ,'\"')) > 0")
                .append(PLACEHOLDER);
        return pendingClientSQL;
    }

    /**
     * 获取入店量 comeShopClientCount sql
     *
     * @param provinceAnalysisParamDTO
     */
    private StringBuilder getComeShopClientSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder sb = new StringBuilder();
        getBaseSQL(sb, provinceAnalysisParamDTO);
        sb.append(" AND (info.COMESHOPTIME BETWEEN :start AND :end) ")
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
        sb.append(" AND (info.SUCCESSTIME BETWEEN :start AND :end) ")
                .append(PLACEHOLDER);

        setConditionSQL(sb, provinceAnalysisParamDTO);
        return sb;
    }

    /**
     * 获取无效量 inValidClientCount sql
     *
     * @param provinceAnalysisParamDTO
     * @param dsInvalidVO
     * @return
     */
    private StringBuilder getInValidClientCountSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO dsInvalidVO) {
        StringBuilder sb = getInValidClient(provinceAnalysisParamDTO, dsInvalidVO);
        setConditionSQL(sb, provinceAnalysisParamDTO);
        return sb;
    }

    /**
     * 获取待定量 pendingClientCount sql
     *
     * @param provinceAnalysisParamDTO
     * @param invalidConfig
     * @return
     */
    private StringBuilder getPendingClientSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        StringBuilder sb = getPendingClientCount(provinceAnalysisParamDTO, invalidConfig);
        setConditionSQL(sb, provinceAnalysisParamDTO);
        return sb;
    }

    /**
     * 有效率
     *
     * @param provinceAnalysisParamDTO
     * @param invalidConfig
     * @return
     */
    private StringBuilder getValidRateSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT yxl.srcId,yxl.SRCIMG,yxl.SRCNAME,IFNULL(FORMAT(yxl.dataNum*100/kzl.dataNum,2),0)  dataNum ")
                .append("FROM ( " + getValidClientSQL(provinceAnalysisParamDTO, invalidConfig).toString() + " ) yxl ")
                .append("INNER JOIN (" + getClientSQL(provinceAnalysisParamDTO).toString() + " ) kzl ON yxl.srcId = kzl.srcId");
        return sb;
    }

    /**
     * 无效率 inValidRate SQL (无效量 / 客资量)
     *
     * @return
     */
    private StringBuilder getInValidRateSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT wxl.srcId,wxl.SRCIMG,wxl.SRCNAME,IFNULL(FORMAT(wxl.dataNum*100/kzl.dataNum,2),0)  dataNum ")
                .append("FROM ( " + getInValidClientCountSQL(provinceAnalysisParamDTO, invalidConfig).toString() + " ) wxl ")
                .append("INNER JOIN (" + getClientSQL(provinceAnalysisParamDTO).toString() + ")kzl ON wxl.srcId = kzl.srcId");
        return sb;
    }

    /**
     * 待定率  waitRate  SQL  (待定量 / 客资量)
     *
     * @param provinceAnalysisParamDTO
     * @param invalidConfig
     * @return
     */
    private StringBuilder getWaitRateSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ddl.srcId,ddl.SRCIMG,ddl.SRCNAME,IFNULL(FORMAT(ddl.dataNum*100/kzl.dataNum,2),0) dataNum  ")
                .append("FROM ( " + getPendingClientSQL(provinceAnalysisParamDTO, invalidConfig) + " ) ddl ")
                .append("INNER JOIN (" + getClientSQL(provinceAnalysisParamDTO).toString() + ") kzl ON ddl.srcId = kzl.srcId");
        return sb;
    }

    /**
     * 毛客资入店率 clientComeShopRate SQL (入店量 / 总客资)
     *
     * @param provinceAnalysisParamDTO
     * @return
     */
    private StringBuilder getClientComeShopRateSQL(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT rdl.srcId,rdl.SRCIMG,rdl.SRCNAME,IFNULL(FORMAT(rdl.dataNum*100/kzl.dataNum,2),0) dataNum  ")
                .append("FROM ( " + getComeShopClientSQL(provinceAnalysisParamDTO).toString() + " ) rdl ")
                .append("INNER JOIN (" + getClientSQL(provinceAnalysisParamDTO).toString() + " ) kzl ON rdl.srcId = kzl.srcId");
        return sb;
    }

    /**
     * 有效客资入店率  validClientComeShopRate 有效客资入店率 (入店量 / 有效量 )
     *
     * @param provinceAnalysisParamDTO
     * @param invalidConfig
     * @return
     */
    private StringBuilder getValidClientComeShopRate(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT rdl.srcId,rdl.SRCIMG,rdl.SRCNAME,IFNULL(FORMAT(rdl.dataNum*100/yxl.dataNum,2),0) dataNum  ")
                .append("FROM ( " + getComeShopClientSQL(provinceAnalysisParamDTO).toString() + " ) rdl ")
                .append("INNER JOIN (" + getValidClientSQL(provinceAnalysisParamDTO, invalidConfig).toString() + " ) yxl ON rdl.srcId = yxl.srcId");
        return sb;
    }

    /**
     * 入店成交率      comeShopSuccessRate 入店成交率（成交量/入店量）
     *
     * @param provinceAnalysisParamDTO
     * @return
     */
    private StringBuilder getComeShopSuccessRate(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cjl.srcId,cjl.SRCIMG,cjl.SRCNAME,IFNULL(FORMAT(cjl.dataNum*100/rdl.dataNum,2),0) dataNum  ")
                .append("FROM ( " + getSuccessClientSQL(provinceAnalysisParamDTO).toString() + " ) cjl ")
                .append("INNER JOIN (" + getComeShopClientSQL(provinceAnalysisParamDTO).toString() + " ) rdl ON cjl.srcId = rdl.srcId");
        return sb;
    }

    /**
     * 毛客资成交率 clientSuccessRate   毛客资成交率（成交量/总客资）
     *
     * @param provinceAnalysisParamDTO
     * @return
     */
    private StringBuilder getClientSuccessRate(ProvinceAnalysisParamDTO provinceAnalysisParamDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cjl.srcId,cjl.SRCIMG,cjl.SRCNAME,IFNULL(FORMAT(cjl.dataNum*100/zkz.dataNum,2),0) dataNum  ")
                .append("FROM ( " + getSuccessClientSQL(provinceAnalysisParamDTO).toString() + " ) cjl ")
                .append("INNER JOIN (" + getAllClientSQL(provinceAnalysisParamDTO).toString() + " ) zkz ON cjl.srcId = zkz.srcId");
        return sb;
    }

    /**
     * 有效客资成交率  validClientSuccessRate  有效客资成交率（成交量/有效量）
     *
     * @param provinceAnalysisParamDTO
     * @return
     */
    private StringBuilder getValidClientSuccessRate(ProvinceAnalysisParamDTO provinceAnalysisParamDTO, DsInvalidVO invalidConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cjl.srcId,cjl.SRCIMG,cjl.SRCNAME,IFNULL(FORMAT(cjl.dataNum*100/yxl.dataNum,2),0) dataNum  ")
                .append("FROM ( " + getSuccessClientSQL(provinceAnalysisParamDTO).toString() + " ) cjl ")
                .append("INNER JOIN (" + getValidClientSQL(provinceAnalysisParamDTO, invalidConfig).toString() + " ) yxl ON cjl.srcId = yxl.srcId");
        return sb;
    }
}
