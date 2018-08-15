package com.qiein.jupiter.web.repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
	public List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsSum(ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO dsInvalidVO) {
		List<DstgReportsSrcMonthVO> dstgReportsSrcMonthVOS = new ArrayList<>();
		
		StringBuilder sb=new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		sb.append("SELECT sr.ID as srcId,sr.SRCNAME as srcName ,COUNT(1) as AllClientCount,FROM_UNIXTIME(infos.CREATETIME, '%Y-%m-%d') as day ");
		sb.append(" FROM ");
		sb.append(infoTabName+" infos ," + " hm_crm_source sr ");
		sb.append(" WHERE");
		sb.append(" infos.SOURCEID=sr.ID");
		sb.append(" AND	infos.COMPANYID=?");
		sb.append(" AND infos.ISDEL=0");
		sb.append(" AND infos.TYPEID IN(?)");
		sb.append(" AND infos.CREATETIME >=?");
		sb.append(" AND infos.CREATETIME <=?");
		sb.append(" AND sr.TYPEID=1");
		sb.append(" AND sr.ID IN(?)");
		sb.append(" GROUP BY sr.SRCNAME , day");
		sb.append(" ORDER BY infos.CREATETIME");
		List<Map<String, Object>> lists = jdbcTemplate.queryForList(sb.toString(), new Object[]{
			reportsParamSrcMonthVO.getCompanyId(),
			reportsParamSrcMonthVO.getTypeId(),
			reportsParamSrcMonthVO.getStart(),
			reportsParamSrcMonthVO.getEnd(),
			reportsParamSrcMonthVO.getSourceId()
		});
		for (Map<String, Object> list : lists) {
			DstgReportsSrcMonthVO dstgReportsSrcMonthVO=new DstgReportsSrcMonthVO();
			dstgReportsSrcMonthVO.setSourceId(Long.toString((Long)list.get("srcId")));
			dstgReportsSrcMonthVO.setSourceName((String) list.get("srcName"));
			dstgReportsSrcMonthVO.setAllClientCount( Integer.parseInt(Long.toString((Long)list.get("AllClientCount"))));
			dstgReportsSrcMonthVO.setDay((String) list.get("day"));
			dstgReportsSrcMonthVOS.add(dstgReportsSrcMonthVO);
		}
		
		return dstgReportsSrcMonthVOS;
	}
	
	/**
     * 获取客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsAll(ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		List<DstgReportsSrcMonthVO> dstgReportsSrcMonthVOS = new ArrayList<>();
		
		StringBuilder sb=new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		sb.append("SELECT sr.ID as srcId,sr.SRCNAME as srcName ,COUNT(1) as ClientCount,FROM_UNIXTIME(infos.CREATETIME, '%Y-%m-%d') as day ");
		sb.append(" FROM ");
		sb.append(infoTabName+" infos ," + " hm_crm_source sr ");
		sb.append(" WHERE");
		sb.append(" infos.SOURCEID=sr.ID");
		sb.append(" AND	infos.COMPANYID=?");
		sb.append(" AND infos.ISDEL=0");
		sb.append(" AND infos.TYPEID IN(?)");
		sb.append(" AND infos.CREATETIME >=?");
		sb.append(" AND infos.CREATETIME <=?");
		sb.append(" AND infos.STATUSID !=0");
		sb.append(" AND infos.STATUSID !=98");
		sb.append(" AND infos.STATUSID !=99");
		sb.append(" AND sr.TYPEID=1");
		sb.append(" AND sr.ID IN(?)");
		sb.append(" GROUP BY sr.SRCNAME , day");
		sb.append(" ORDER BY infos.CREATETIME");
		List<Map<String, Object>> lists = jdbcTemplate.queryForList(sb.toString(), new Object[]{
			reportsParamSrcMonthVO.getCompanyId(),
			reportsParamSrcMonthVO.getTypeId(),
			reportsParamSrcMonthVO.getStart(),
			reportsParamSrcMonthVO.getEnd(),
			reportsParamSrcMonthVO.getSourceId()
		});
		for (Map<String, Object> list : lists) {
			DstgReportsSrcMonthVO dstgReportsSrcMonthVO=new DstgReportsSrcMonthVO();
			dstgReportsSrcMonthVO.setSourceId(Long.toString((Long)list.get("srcId")));
			dstgReportsSrcMonthVO.setSourceName((String) list.get("srcName"));
			dstgReportsSrcMonthVO.setClientCount(Integer.parseInt(Long.toString((Long)list.get("ClientCount"))));
			dstgReportsSrcMonthVO.setDay((String) list.get("day"));
			dstgReportsSrcMonthVOS.add(dstgReportsSrcMonthVO);
		}
		return dstgReportsSrcMonthVOS;
	}
	
	/**
     * 获取待定客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsDdNum(ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		List<DstgReportsSrcMonthVO> dstgReportsSrcMonthVOS = new ArrayList<>();
		
		StringBuilder sb=new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		sb.append("SELECT sr.ID as srcId,sr.SRCNAME as srcName ,COUNT(1) as DsDDClientCount,FROM_UNIXTIME(infos.CREATETIME, '%Y-%m-%d') as day ");
		sb.append(" FROM ");
		sb.append(infoTabName+" infos ," + " hm_crm_source sr ");
		sb.append(" WHERE ");
		sb.append(" infos.SOURCEID=sr.ID ");
		sb.append(" AND	infos.COMPANYID=?");
		sb.append(" AND infos.ISDEL=0");
		sb.append(" AND infos.TYPEID IN(?)");
		sb.append(" AND infos.CREATETIME >=?");
		sb.append(" AND infos.CREATETIME <=?");
		sb.append(" AND INSTR( ?, CONCAT(',',infos.STATUSID + '',',')) != 0");
		sb.append(" AND sr.TYPEID=1");
		sb.append(" AND sr.ID IN(?)");
		sb.append(" GROUP BY sr.SRCNAME , day");
		sb.append(" ORDER BY infos.CREATETIME");
		List<Map<String, Object>> lists = jdbcTemplate.queryForList(sb.toString(), new Object[]{
			reportsParamSrcMonthVO.getCompanyId(),
			reportsParamSrcMonthVO.getTypeId(),
			reportsParamSrcMonthVO.getStart(),
			reportsParamSrcMonthVO.getEnd(),
			invalidConfig.getDsDdStatus(),
			reportsParamSrcMonthVO.getSourceId()
		});
		for (Map<String, Object> list : lists) {
			DstgReportsSrcMonthVO dstgReportsSrcMonthVO=new DstgReportsSrcMonthVO();
			dstgReportsSrcMonthVO.setSourceId(Long.toString((Long)list.get("srcId")));
			dstgReportsSrcMonthVO.setSourceName((String) list.get("srcName"));
			dstgReportsSrcMonthVO.setPendingClientCount(Integer.parseInt(Long.toString((Long)list.get("DsDDClientCount"))));
			dstgReportsSrcMonthVO.setDay((String) list.get("day"));
			dstgReportsSrcMonthVOS.add(dstgReportsSrcMonthVO);
		}
		return dstgReportsSrcMonthVOS;
	}
	
	/**
     * 获取无效客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsInvalid(ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		List<DstgReportsSrcMonthVO> dstgReportsSrcMonthVOS = new ArrayList<>();
		
		StringBuilder sb=new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		String detailTabName = DBSplitUtil.getDetailTabName(reportsParamSrcMonthVO.getCompanyId());
		sb.append("SELECT sr.ID as srcId,sr.SRCNAME as srcName ,COUNT(1) as InValidClientCount,FROM_UNIXTIME(infos.CREATETIME, '%Y-%m-%d') as day ");
		sb.append(" FROM ");
		sb.append(infoTabName+" infos LEFT JOIN " + " hm_crm_source sr ON infos.SOURCEID = sr.ID ");
		sb.append(" LEFT JOIN "+ detailTabName +" infodetail " + " ON infos.KZID=infodetail.KZID ");
		sb.append(" WHERE");
		sb.append(" infos.COMPANYID=?");
		sb.append(" AND infos.ISDEL=0");
		sb.append(" AND infos.TYPEID IN(?)");
		sb.append(" AND infos.CREATETIME >=?");
		sb.append(" AND infos.CREATETIME <=?");
		if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
            sb.append(" AND (infos.STATUSID IN("+ invalidConfig.getDsInvalidStatus()+") or");
            sb.append("   infodetail.YXLEVEL IN("+ invalidConfig.getDsInvalidLevel()  +") )");
        }
        if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isEmpty(invalidConfig.getDsInvalidLevel())){
            sb.append(" AND infos.STATUSID IN ("+ invalidConfig.getDsInvalidStatus()+")");
        }
        if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel()) && StringUtil.isEmpty(invalidConfig.getDsInvalidStatus())){
            sb.append(" AND infodetail.YXLEVEL IN("+ invalidConfig.getDsInvalidLevel()  +") ");
        }
		sb.append(" AND sr.TYPEID=1");
		sb.append(" AND sr.ID IN(?)");
		sb.append(" GROUP BY sr.SRCNAME , day");
		sb.append(" ORDER BY infos.CREATETIME");
		List<Map<String, Object>> lists = jdbcTemplate.queryForList(sb.toString(), new Object[]{
			reportsParamSrcMonthVO.getCompanyId(),
			reportsParamSrcMonthVO.getTypeId(),
			reportsParamSrcMonthVO.getStart(),
			reportsParamSrcMonthVO.getEnd(),
			reportsParamSrcMonthVO.getSourceId()
		});
		for (Map<String, Object> list : lists) {
			DstgReportsSrcMonthVO dstgReportsSrcMonthVO=new DstgReportsSrcMonthVO();
			dstgReportsSrcMonthVO.setSourceId(Long.toString((Long)list.get("srcId")));
			dstgReportsSrcMonthVO.setSourceName((String) list.get("srcName"));
			dstgReportsSrcMonthVO.setInValidClientCount(Integer.parseInt(Long.toString((Long)list.get("InValidClientCount"))));
			dstgReportsSrcMonthVO.setDay((String) list.get("day"));
			dstgReportsSrcMonthVOS.add(dstgReportsSrcMonthVO);
		}
		return dstgReportsSrcMonthVOS;
	}
	
	/**
     * 获取有效客资量
     * @param start
     * @param end
     * @param companyId
     * */
	public List<DstgReportsSrcMonthVO> getDSTGSrcMonthReportsvalid(ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			DsInvalidVO invalidConfig) {
		//构造集合对象，把指标放入
		List<DstgReportsSrcMonthVO> dstgReportsSrcMonthVOS = new ArrayList<>();
		//获取客资量
		getClientCount(reportsParamSrcMonthVO, dstgReportsSrcMonthVOS);
		//获取无效量
		getInValidClientCount(reportsParamSrcMonthVO, dstgReportsSrcMonthVOS,invalidConfig);
		//获取待定量
		getDDClientCount(reportsParamSrcMonthVO, dstgReportsSrcMonthVOS,invalidConfig);
		//有效量为客资量-无效量或者客资量-无效量-待定量
		getvalidClientCount(reportsParamSrcMonthVO, dstgReportsSrcMonthVOS,invalidConfig);
		return dstgReportsSrcMonthVOS;
	}
	
	private void getvalidClientCount(ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			List<DstgReportsSrcMonthVO> dstgReportsSrcMonthVOS, DsInvalidVO invalidConfig) {
		
		for (DstgReportsSrcMonthVO dstgReportsSrcMonthVO : dstgReportsSrcMonthVOS) {
			if(invalidConfig.getDdIsValid()){
				dstgReportsSrcMonthVO.setValidClientCount(dstgReportsSrcMonthVO.getClientCount()-dstgReportsSrcMonthVO.getInValidClientCount());
			}else{
				dstgReportsSrcMonthVO.setValidClientCount(dstgReportsSrcMonthVO.getClientCount()-dstgReportsSrcMonthVO.getInValidClientCount()-dstgReportsSrcMonthVO.getPendingClientCount());
			}
		}
		
	}

	//获取待定量
	private void getDDClientCount(ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			List<DstgReportsSrcMonthVO> dstgReportsSrcMonthVOS,DsInvalidVO invalidConfig) {
		StringBuilder sb=new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		sb.append("SELECT sr.ID as srcId,sr.SRCNAME as srcName ,COUNT(1) as DsDDClientCount,FROM_UNIXTIME(infos.CREATETIME, '%Y-%m-%d') as day ");
		sb.append(" FROM ");
		sb.append(infoTabName+" infos ," + " hm_crm_source sr ");
		sb.append(" WHERE");
		sb.append(" infos.SOURCEID=sr.ID");
		sb.append(" AND	infos.COMPANYID=?");
		sb.append(" AND infos.ISDEL=0");
		sb.append(" AND infos.TYPEID IN(?)");
		sb.append(" AND infos.CREATETIME >=?");
		sb.append(" AND infos.CREATETIME <=?");
		sb.append(" AND INSTR( ?, CONCAT(',',infos.STATUSID + '',',')) != 0");
		sb.append(" AND sr.TYPEID=1");
		sb.append(" AND sr.ID IN(?)");
		sb.append(" GROUP BY sr.SRCNAME , day");
		sb.append(" ORDER BY infos.CREATETIME");
		List<Map<String, Object>> lists = jdbcTemplate.queryForList(sb.toString(), new Object[]{
			reportsParamSrcMonthVO.getCompanyId(),
			reportsParamSrcMonthVO.getTypeId(),
			reportsParamSrcMonthVO.getStart(),
			reportsParamSrcMonthVO.getEnd(),
			invalidConfig.getDsDdStatus(),
			reportsParamSrcMonthVO.getSourceId()
		});
		
		List<DstgReportsSrcMonthVO> dstgReportsSrcMonthVOBaks = new LinkedList<>();
		for (Map<String, Object> list : lists) {
			DstgReportsSrcMonthVO dstgReportsSrcMonthVO=new  DstgReportsSrcMonthVO();
			dstgReportsSrcMonthVO.setSourceId(Long.toString((Long)list.get("srcId")));
			dstgReportsSrcMonthVO.setSourceName((String) list.get("srcName"));
			dstgReportsSrcMonthVO.setPendingClientCount(Integer.parseInt(Long.toString((Long)list.get("DsDDClientCount"))));
			dstgReportsSrcMonthVO.setDay((String) list.get("day"));
			dstgReportsSrcMonthVOBaks.add(dstgReportsSrcMonthVO);
		}
		for (DstgReportsSrcMonthVO dstgReportsSrcMonthVO : dstgReportsSrcMonthVOS) {
			for (DstgReportsSrcMonthVO dstgReportsSrcMonthVOBak : dstgReportsSrcMonthVOBaks) {
				if(dstgReportsSrcMonthVO.getSourceId().equalsIgnoreCase(dstgReportsSrcMonthVOBak.getSourceId()) 
						&& dstgReportsSrcMonthVO.getDay().equalsIgnoreCase(dstgReportsSrcMonthVOBak.getDay())){
					dstgReportsSrcMonthVO.setPendingClientCount(dstgReportsSrcMonthVOBak.getPendingClientCount());
					break;
				}
			}
		}
		
	}
	// 获取无效客资量
	private void getInValidClientCount(ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			List<DstgReportsSrcMonthVO> dstgReportsSrcMonthVOS,DsInvalidVO invalidConfig) {
		StringBuilder sb=new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		String detailTabName = DBSplitUtil.getDetailTabName(reportsParamSrcMonthVO.getCompanyId());
		sb.append("SELECT sr.ID as srcId,sr.SRCNAME as srcName ,COUNT(1) as InValidClientCount,FROM_UNIXTIME(infos.CREATETIME, '%Y-%m-%d') as day ");
		sb.append(" FROM ");
		sb.append(infoTabName+" infos LEFT JOIN " + " hm_crm_source sr ON infos.SOURCEID = sr.ID ");
		sb.append("LEFT JOIN"+ detailTabName +" infodetail " + " ON infos.KZID=infodetail.KZID ");
		sb.append(" WHERE");
		sb.append(" infos.COMPANYID=?");
		sb.append(" AND infos.ISDEL=0");
		sb.append(" AND infos.TYPEID IN(?)");
		sb.append(" AND infos.CREATETIME >=?");
		sb.append(" AND infos.CREATETIME <=?");
		if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel())){
            sb.append(" AND (infos.STATUSID IN("+ invalidConfig.getDsInvalidStatus()+") or");
            sb.append("   infodetail.YXLEVEL IN("+ invalidConfig.getDsInvalidLevel()  +") )");
        }
        if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidStatus()) && StringUtil.isEmpty(invalidConfig.getDsInvalidLevel())){
            sb.append(" AND infos.STATUSID IN ("+ invalidConfig.getDsInvalidStatus()+")");
        }
        if(StringUtil.isNotEmpty(invalidConfig.getDsInvalidLevel()) && StringUtil.isEmpty(invalidConfig.getDsInvalidStatus())){
            sb.append(" AND infodetail.YXLEVEL IN("+ invalidConfig.getDsInvalidLevel()  +") ");
        }
		sb.append(" AND sr.TYPEID=1");
		sb.append(" AND sr.ID IN(?)");
		sb.append(" GROUP BY sr.SRCNAME , day");
		sb.append(" ORDER BY infos.CREATETIME");
		List<Map<String, Object>> lists = jdbcTemplate.queryForList(sb.toString(), new Object[]{
			reportsParamSrcMonthVO.getCompanyId(),
			reportsParamSrcMonthVO.getTypeId(),
			reportsParamSrcMonthVO.getStart(),
			reportsParamSrcMonthVO.getEnd(),
			reportsParamSrcMonthVO.getSourceId()
		});
		
		List<DstgReportsSrcMonthVO> dstgReportsSrcMonthVOBaks = new LinkedList<>();
		for (Map<String, Object> list : lists) {
			DstgReportsSrcMonthVO dstgReportsSrcMonthVO=new  DstgReportsSrcMonthVO();
			dstgReportsSrcMonthVO.setSourceId(Long.toString((Long)list.get("srcId")));
			dstgReportsSrcMonthVO.setSourceName((String) list.get("srcName"));
			dstgReportsSrcMonthVO.setInValidClientCount(Integer.parseInt(Long.toString((Long)list.get("InValidClientCount"))));
			dstgReportsSrcMonthVO.setDay((String) list.get("day"));
			dstgReportsSrcMonthVOBaks.add(dstgReportsSrcMonthVO);
		}
		for (DstgReportsSrcMonthVO dstgReportsSrcMonthVO : dstgReportsSrcMonthVOS) {
			for (DstgReportsSrcMonthVO dstgReportsSrcMonthVOBak : dstgReportsSrcMonthVOBaks) {
				if(dstgReportsSrcMonthVO.getSourceId().equalsIgnoreCase(dstgReportsSrcMonthVOBak.getSourceId()) 
						&& dstgReportsSrcMonthVO.getDay().equalsIgnoreCase(dstgReportsSrcMonthVOBak.getDay())){
					dstgReportsSrcMonthVO.setInValidClientCount(dstgReportsSrcMonthVOBak.getInValidClientCount());
					break;
				}
			}
		}
		
	}
	//获取客资量
	private void getClientCount(ReportsParamSrcMonthVO reportsParamSrcMonthVO,
			List<DstgReportsSrcMonthVO> dstgReportsSrcMonthVOS) {
		StringBuilder sb=new StringBuilder();
		String infoTabName = DBSplitUtil.getInfoTabName(reportsParamSrcMonthVO.getCompanyId());
		sb.append("SELECT sr.ID as srcId,sr.SRCNAME as srcName ,COUNT(1) as ClientCount,FROM_UNIXTIME(infos.CREATETIME, '%Y-%m-%d') as day ");
		sb.append(" FROM ");
		sb.append(infoTabName+" infos ," + " hm_crm_source sr ");
		sb.append(" WHERE");
		sb.append(" infos.SOURCEID=sr.ID");
		sb.append(" AND	infos.COMPANYID=?");
		sb.append(" AND infos.ISDEL=0");
		sb.append(" AND infos.TYPEID IN(?)");
		sb.append(" AND infos.CREATETIME >=?");
		sb.append(" AND infos.CREATETIME <=?");
		sb.append(" AND infos.STATUSID !=0");
		sb.append(" AND infos.STATUSID !=98");
		sb.append(" AND infos.STATUSID !=99");
		sb.append(" AND sr.TYPEID=1");
		sb.append(" AND sr.ID IN(?)");
		sb.append(" GROUP BY sr.SRCNAME , day");
		sb.append(" ORDER BY infos.CREATETIME");
		List<Map<String, Object>> lists = jdbcTemplate.queryForList(sb.toString(), new Object[]{
			reportsParamSrcMonthVO.getCompanyId(),
			reportsParamSrcMonthVO.getTypeId(),
			reportsParamSrcMonthVO.getStart(),
			reportsParamSrcMonthVO.getEnd(),
			reportsParamSrcMonthVO.getSourceId()
		});
		
		for (Map<String, Object> list : lists) {
			DstgReportsSrcMonthVO dstgReportsSrcMonthVO=new DstgReportsSrcMonthVO();
			dstgReportsSrcMonthVO.setSourceId(Long.toString((Long)list.get("srcId")));
			dstgReportsSrcMonthVO.setSourceName((String) list.get("srcName"));
			dstgReportsSrcMonthVO.setClientCount(Integer.parseInt(Long.toString((Long)list.get("ClientCount"))));
			dstgReportsSrcMonthVO.setDay((String) list.get("day"));
			dstgReportsSrcMonthVOS.add(dstgReportsSrcMonthVO);
		}
	}

	
}
