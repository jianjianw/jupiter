package com.qiein.jupiter.web.repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import com.qiein.jupiter.web.entity.vo.DstgReportsSrcMonthVO;
import com.qiein.jupiter.web.entity.vo.ReportsParamSrcMonthVO;

/**
 * @author: Hjf
 * @Date: 2018-8-10
 */
@Repository
public class DstgReportsSrcMonthDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	/**
     * 获取总客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsSum(List<Map<String, Object>> dayList , String month,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO dsInvalidVO) {
		StringBuilder sql = new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		
        sql.append("SELECT src.SRCNAME srcName,src.ID srcId,");
        if(StringUtil.isEmpty(reportsParamSrcMonthVO.getTypeId()) ){
        	sql.append("(select COUNT(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0  AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
        	for (Map<String, Object> day : dayList) {
                sql.append("(select count(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0  AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
            }
        }else{
        	sql.append("(select COUNT(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 "+" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")  " +" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
        	for (Map<String, Object> day : dayList) {
                sql.append("(select count(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 "+" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")  " +" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
            }
        }
        sql.append(" src.SRCIMG srcImg ");
        sql.append(" FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (1,2) ");
        
        if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
            sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
        }
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listSum = jdbcTemplate.queryForList(sqlString, new Object[]{reportsParamSrcMonthVO.getCompanyId()});
		
        //横的一列合计
        /*StringBuilder hjsql = new StringBuilder();
        hjsql.append("SELECT '合计'  srcName, ''  srcIamge,");
        hjsql.append("(SELECT COUNT(1)");
        hjsql.append(" FROM ");
        hjsql.append(" "+infoTabName+" info");
        hjsql.append(" WHERE ");
        hjsql.append(" FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d') = '"+month+"'");
        hjsql.append(" AND info.ISDEL=0 ");
        if(StringUtil.isEmpty(reportsParamSrcMonthVO.getTypeId()) ){
        	hjsql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")  ");
        }
        hjsql.append(" AND info.COMPANYID="+reportsParamSrcMonthVO.getCompanyId() );
        hjsql.append(" AND info.SRCTYPE IN(1,2) ) hj,");
        //
        for (Map<String, Object> day : dayList) {
        	hjsql.append("(SELECT COUNT(1)");
        	hjsql.append(" FROM ");
        	hjsql.append(" "+infoTabName+" info");
        	hjsql.append(" WHERE ");
        	hjsql.append(" FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d') = '"+day.get("day")+"' ");
        	hjsql.append(" AND info.ISDEL=0 ");
            if(StringUtil.isEmpty(reportsParamSrcMonthVO.getTypeId()) ){
            	hjsql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")  ");
            }
            hjsql.append(" AND info.COMPANYID="+reportsParamSrcMonthVO.getCompanyId() );
            hjsql.append(" AND info.SRCTYPE IN(1,2) ) "+day.get("day")+" , ");
        }
        String hjsqlString = hjsql.toString();
        List<Map<String, Object>> hjlistSum = jdbcTemplate.queryForList(hjsqlString, new Object[]{});
        hjlistSum.addAll(listSum);*/
		return listSum;
	}
	
	/**
     * 获取客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsAll(List<Map<String, Object>> dayList , String month,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		StringBuilder sql = new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		
        sql.append("SELECT src.SRCNAME srcName,src.ID srcId,");
        if(StringUtil.isEmpty(reportsParamSrcMonthVO.getTypeId()) ){
        	sql.append("(select COUNT(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 AND info.STATUSID NOT IN(0,98,99) AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
        	for (Map<String, Object> day : dayList) {
                sql.append("(select count(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 AND info.STATUSID NOT IN(0,98,99)  AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
            }
        	
        }else{
        	sql.append("(select COUNT(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 AND info.STATUSID NOT IN(0,98,99) "+" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")  " +" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
        	for (Map<String, Object> day : dayList) {
                sql.append("(select count(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 AND info.STATUSID NOT IN(0,98,99) "+" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")  " +" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
            }
        }
        sql.append(" src.SRCIMG srcImg ");
        sql.append(" FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (1,2) ");
        
        if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
            sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
        }
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listAll = jdbcTemplate.queryForList(sqlString, new Object[]{reportsParamSrcMonthVO.getCompanyId()});
		return listAll;
	}
	
	/**
     * 获取待定客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsDdNum(List<Map<String, Object>> dayList , String month,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		StringBuilder sql = new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		
        sql.append("SELECT src.SRCNAME srcName,src.ID srcId,");
        sql.append("(select COUNT(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0  ");
        	
        if(StringUtil.isNotEmpty(invalidConfig.getDsDdStatus())){
    		sql.append(" AND INSTR('"+invalidConfig.getDsDdStatus()+"', info.STATUSID ) != 0");
    	}
    	if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
    		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
    	}
    	sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
    	//计算每天客资数
    	for (Map<String, Object> day : dayList) {
            sql.append("(select count(info.ID) from " + infoTabName + " info where info.SOURCEID=src.ID AND info.ISDEL=0 ");
            if(StringUtil.isNotEmpty(invalidConfig.getDsDdStatus())){
        		sql.append(" AND INSTR('"+invalidConfig.getDsDdStatus()+"' , info.STATUSID ) != 0");
        	}
        	if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
        		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
        	}
            sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
        }	
        	
        sql.append(" src.SRCIMG srcImg ");
        sql.append(" FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (1,2) ");
        
        if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
            sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
        }
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listdd = jdbcTemplate.queryForList(sqlString, new Object[]{
        		reportsParamSrcMonthVO.getCompanyId(),});
		return listdd;
	}
	
	/**
     * 获取无效客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsInvalid(List<Map<String, Object>> dayList , String month,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		StringBuilder sql = new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		
        sql.append("SELECT src.SRCNAME srcName,src.ID srcId,");
        sql.append("(select COUNT(info.ID) from " + infoTabName + " info, hm_crm_client_detail deta where info.SOURCEID=src.ID AND deta.KZID=info.KZID AND info.ISDEL=0  ");
        //无效指标&&意向等级
        if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
    		sql.append(" AND ( info.STATUSID  IN("+invalidConfig.getDsInvalidStatus()+")"+ "OR deta.YXLEVEL IN("+invalidConfig.getDsInvalidLevel()+"))");
    	}
        //只有无效指标
        if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isEmpty(invalidConfig.getDsInvalidLevel())){
    		sql.append(" AND info.STATUSID  IN("+invalidConfig.getDsInvalidStatus()+")");
    	}
        //只有意向指标
        if(StringUtil.isEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
    		sql.append(" AND deta.YXLEVEL IN("+invalidConfig.getDsInvalidLevel()+")");
    	}
        //拍摄类型
    	if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
    		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
    	}
    	sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
    	//计算每天客资数
    	for (Map<String, Object> day : dayList) {
            sql.append("(select COUNT(info.ID) from " + infoTabName + " info, hm_crm_client_detail deta where info.SOURCEID=src.ID AND deta.KZID=info.KZID AND info.ISDEL=0  ");
          //无效指标&&意向等级
            if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
        		sql.append(" AND ( info.STATUSID  IN("+invalidConfig.getDsInvalidStatus()+")"+ "OR deta.YXLEVEL IN("+invalidConfig.getDsInvalidLevel()+"))");
        	}
            //只有无效指标
            if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isEmpty(invalidConfig.getDsInvalidLevel())){
        		sql.append(" AND info.STATUSID  IN("+invalidConfig.getDsInvalidStatus()+")");
        	}
            //只有意向指标
            if(StringUtil.isEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
        		sql.append(" AND deta.YXLEVEL IN("+invalidConfig.getDsInvalidLevel()+")");
        	}
        	if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
        		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
        	}
            sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
        }	
        	
        sql.append(" src.SRCIMG srcImg ");
        sql.append(" FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (1,2) ");
        
        if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
            sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
        }
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listdd = jdbcTemplate.queryForList(sqlString, new Object[]{
        		reportsParamSrcMonthVO.getCompanyId(),});
		return listdd;
	}
	
	/**
     * 获取有效客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<Map<String, Object>> getDSTGSrcMonthReportsvalid(List<Map<String, Object>> dayList , String month,ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		//构造集合对象，把指标放入
		StringBuilder sql = new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		
        sql.append("SELECT src.SRCNAME srcName,src.ID srcId,");
        sql.append("(select COUNT(info.ID) from " + infoTabName + " info, hm_crm_client_detail deta where info.SOURCEID=src.ID AND info.STATUSID NOT IN(0,98,99) AND deta.KZID=info.KZID AND info.ISDEL=0  ");
        
        //减去无效客资状态指标
        if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) ){
    		sql.append("  AND info.STATUSID NOT IN("+invalidConfig.getDsInvalidStatus()+")");
    	}
        //减去无效意向指标
        if(StringUtil.isEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
    		sql.append(" AND deta.YXLEVEL NOT IN("+invalidConfig.getDsInvalidLevel()+")");
    	}
        //待定不计算为有效时
        if(!(invalidConfig.getDdIsValid())){
        	sql.append(" AND INSTR('"+invalidConfig.getDsDdStatus()+"', CONCAT(',',info.STATUSID,',')) = 0");
        }
        //拍摄类型
    	if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
    		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
    	}
    	sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m')='" + month + "') hj,  ");
    	//计算每天客资数
    	for (Map<String, Object> day : dayList) {
            sql.append("(select COUNT(info.ID) from " + infoTabName + " info, hm_crm_client_detail deta where info.SOURCEID=src.ID AND info.STATUSID NOT IN(0,98,99) AND deta.KZID=info.KZID AND info.ISDEL=0  ");
            //减去无效客资状态指标
            if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) ){
        		sql.append(" AND info.STATUSID NOT IN("+invalidConfig.getDsInvalidStatus()+")");
        	}
            //减去无效意向指标
            if(StringUtil.isEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
        		sql.append(" AND deta.YXLEVEL NOT IN("+invalidConfig.getDsInvalidLevel()+")");
        	}
            //待定不计算为有效时
            if(!(invalidConfig.getDdIsValid())){
            	sql.append(" AND INSTR('"+invalidConfig.getDsDdStatus()+"', CONCAT(',',info.STATUSID,',')) = 0");
            }
        	if(StringUtil.isNotEmpty(reportsParamSrcMonthVO.getTypeId())){
        		sql.append(" AND info.TYPEID IN("+reportsParamSrcMonthVO.getTypeId()+")");
        	}
            sql.append(" AND FROM_UNIXTIME(info.CREATETIME, '%Y/%m/%d')='" + day.get("day") + "') " + day.get("dayKey") + ",");
        }	
        	
        sql.append(" src.SRCIMG srcImg ");
        sql.append(" FROM hm_crm_source src WHERE src.COMPANYID = ? AND TYPEID IN (1,2) ");
        
        if (StringUtil.isNotEmpty(reportsParamSrcMonthVO.getSourceId())) {
            sql.append(" AND src.ID IN (" + reportsParamSrcMonthVO.getSourceId() + ")");
        }
        String sqlString = sql.toString(); 
        List<Map<String, Object>> listdd = jdbcTemplate.queryForList(sqlString, new Object[]{
        		reportsParamSrcMonthVO.getCompanyId(),});
		return listdd;
	}
	
	
	
}
