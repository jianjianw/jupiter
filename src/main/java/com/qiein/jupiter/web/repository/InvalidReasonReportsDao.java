package com.qiein.jupiter.web.repository;

import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.po.SourcePO;
import com.qiein.jupiter.web.entity.vo.InvalidReasonReportsShowVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 无效原因报表
 * author xiangliang
 */
@Repository
public class InvalidReasonReportsDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 报表显示
     * @param list
     * @param tableInfo
     * @param tableDetail
     * @param companyId
     * @param sourceIds
     * @param startTime
     * @param endTime
     * @param typeIds
     * @return
     */
    public List<InvalidReasonReportsShowVO> getInvalidReasonReports(List<SourcePO> sourcePOS, List<DictionaryPO> list, String tableInfo, String tableDetail, Integer companyId, String sourceIds, String startTime, String endTime, String typeIds){
        List<InvalidReasonReportsShowVO> invalidReasonReportsShowVOS=new ArrayList<>();
        SourcePO source=new SourcePO();
        source.setId(0);
        source.setSrcName("合计");
        sourcePOS.add(0,source);
        for(SourcePO sourcePO:sourcePOS){
            InvalidReasonReportsShowVO invalidReasonReportsShowVO=new InvalidReasonReportsShowVO();
            invalidReasonReportsShowVO.setSrcId(sourcePO.getId());
            invalidReasonReportsShowVO.setSrcImg(sourcePO.getSrcImg());
            invalidReasonReportsShowVO.setSrcName(sourcePO.getSrcName());
            Map<String,Integer> map=new HashMap<>();
            for(DictionaryPO dictionaryPO:list){
                map.put(dictionaryPO.getDicType(),0);
            }
            invalidReasonReportsShowVO.setMap(map);
            invalidReasonReportsShowVOS.add(invalidReasonReportsShowVO);
        }
        StringBuilder sql=new StringBuilder();
        sql.append("SELECT src.ID id,");
        sql.append(" COUNT(detail.INVALIDLABEL) count,");
        sql.append(" CONCAT('invalid_reason',detail.INVALIDCODE) statusKey");
        sql.append(" FROM hm_crm_source src");
        sql.append(" LEFT JOIN "+tableInfo+" info ON info.SOURCEID = src.ID");
        sql.append(" LEFT JOIN "+tableDetail+" detail ON detail.KZID = info.KZID");
        sql.append(" WHERE info.CREATETIME BETWEEN ? AND ?");
        sql.append(" AND detail.INVALIDCODE IS NOT NULL");
        sql.append(" and src.companyid=? and src.typeid in (1,2)");
        if(StringUtil.isNotEmpty(typeIds)){
            sql.append(" AND info.TYPEID IN ("+typeIds+")");
        }
        if(StringUtil.isNotEmpty(sourceIds)){
            sql.append(" AND info.SOURCEID IN ("+sourceIds+")");
        }
        sql.append(" GROUP BY detail.INVALIDLABEL,info.SOURCEID ");
        List<Map<String, Object>> invalidalbel = jdbcTemplate.queryForList(sql.toString(), new Object[]{startTime,endTime,companyId});
        for(Map<String,Object> map:invalidalbel){
            for(InvalidReasonReportsShowVO invalidReasonReportsShowVO:invalidReasonReportsShowVOS){
                if(invalidReasonReportsShowVO.getSrcId()==Integer.parseInt(Long.toString((Long) (map.get("id"))))){
                    for(DictionaryPO dictionaryPO:list){
                        if(dictionaryPO.getDicType().equals((String)map.get("statusKey"))){
                            invalidReasonReportsShowVO.getMap().put((String)map.get("statusKey"),Integer.parseInt(Long.toString((Long) (map.get("count")))));
                        }
                    }
                }
            }
        }
        StringBuilder hjsql=new StringBuilder();
        hjsql.append("SELECT COUNT(detail.INVALIDLABEL) count,");
        hjsql.append(" CONCAT('invalid_reason',detail.INVALIDLABEL) statusKey");
        hjsql.append(" FROM "+tableInfo+" info");
        hjsql.append(" LEFT JOIN "+tableDetail+" detail ON detail.KZID = info.KZID");
        hjsql.append(" WHERE info.CREATETIME BETWEEN ? AND ?");
        hjsql.append(" AND detail.INVALIDLABEL IS NOT NULL");
        hjsql.append(" and info.companyid=?");
        if(StringUtil.isNotEmpty(typeIds)){
            hjsql.append(" AND info.TYPEID IN ("+typeIds+")");
        }
        if(StringUtil.isNotEmpty(sourceIds)){
            hjsql.append(" AND info.SOURCEID IN ("+sourceIds+")");
        }
        hjsql.append(" GROUP BY detail.INVALIDLABEL ");
        List<Map<String, Object>> invalidalbelHj = jdbcTemplate.queryForList(hjsql.toString(), new Object[]{startTime,endTime,companyId});
        for(Map<String, Object> map:invalidalbelHj){
            for(DictionaryPO dictionaryPO:list){
                if(dictionaryPO.getDicName().equals((String)map.get("statusKey"))){
                    invalidReasonReportsShowVOS.get(0).getMap().put((String)map.get("statusKey"),Integer.parseInt(Long.toString((Long) (map.get("count")))));
                }
            }
        }
        for(InvalidReasonReportsShowVO invalidReasonReportsShowVO:invalidReasonReportsShowVOS){
            invalidReasonReportsShowVO.getMap().put("hj",0);
            for(String key:invalidReasonReportsShowVO.getMap().keySet()){
                invalidReasonReportsShowVO.getMap().put("hj",invalidReasonReportsShowVO.getMap().get("hj")+invalidReasonReportsShowVO.getMap().get(key));
            }
        }
        return invalidReasonReportsShowVOS;
    }

}
