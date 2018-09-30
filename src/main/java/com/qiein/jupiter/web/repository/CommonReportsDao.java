package com.qiein.jupiter.web.repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ReportsConfigConst;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.vo.DsInvalidVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: yyx
 * @Date: 2018-8-10
 */
@Repository
public class CommonReportsDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 获取无效等指标定义
     */
    public DsInvalidVO getInvalidConfig(int companyId) {
        StringBuilder sb = new StringBuilder();

        if (NumUtil.isInValid(companyId)) {
            return null;
        }
        sb.append(" SELECT comp.REPORTSCONFIG  FROM hm_pub_company comp WHERE comp.ID = ? AND comp.ISDEL = 0 ");
//        sb.append(" SELECT comp.DSINVALIDSTATUS, comp.DSINVALIDLEVEL, comp.DDISVALID, comp.DSDDSTATUS ,comp.ZJSVALIDSTATUS FROM hm_pub_company comp WHERE comp.ID = ? AND comp.ISDEL = 0 ");
        DsInvalidVO dsInvalidVO = jdbcTemplate.queryForObject(sb.toString(),
                new Object[]{companyId},
                new RowMapper<DsInvalidVO>() {
                    @Override
                    public DsInvalidVO mapRow(ResultSet rs, int i) throws SQLException {
                        DsInvalidVO dsInvalidVO = new DsInvalidVO();
                        JSONObject configObj = JSONObject.parseObject(rs.getString("REPORTSCONFIG"));
                        //筛选无效的意向等级
                        JSONArray yxLevelArr = configObj.getJSONObject(ReportsConfigConst.WX_SET).getJSONArray(ReportsConfigConst.DZZ_YXDJ);
                        //筛选无效状态
                        JSONArray statusArr = configObj.getJSONObject(ReportsConfigConst.WX_SET).getJSONArray(ReportsConfigConst.WX_STATUS);
                        //待定状态
                        JSONArray xkzArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.XKZ_CLASS);
                        JSONArray yyyArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.YYY_CLASS);
                        JSONArray dzzArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.DZZ_CLASS);
                        JSONArray yjdArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.YJD_CLASS);
                        JSONArray yddArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.YDD_CLASS);
                        JSONArray wxArr = configObj.getJSONObject(ReportsConfigConst.DS_DD_SET).getJSONArray(ReportsConfigConst.WX_CLASS);
                        JSONArray dsDdStatusArr = new JSONArray();
                        dsDdStatusArr.addAll(xkzArr);
                        dsDdStatusArr.addAll(yyyArr);
                        dsDdStatusArr.addAll(dzzArr);
                        dsDdStatusArr.addAll(yjdArr);
                        dsDdStatusArr.addAll(yddArr);
                        dsDdStatusArr.addAll(wxArr);
                        //转介绍状态
                        JSONArray zjsXkzArr = configObj.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.XKZ_CLASS);
                        JSONArray zjsYyyArr = configObj.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.YYY_CLASS);
                        JSONArray zjsDzzArr = configObj.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.DZZ_CLASS);
                        JSONArray zjsYjdArr = configObj.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.YJD_CLASS);
                        JSONArray zjsYddArr = configObj.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.YDD_CLASS);
                        JSONArray zjsWxArr = configObj.getJSONObject(ReportsConfigConst.ZJS_VALID_SET).getJSONArray(ReportsConfigConst.WX_CLASS);
                        JSONArray zjsDdStatusArr = new JSONArray();
                        zjsDdStatusArr.addAll(zjsXkzArr);
                        zjsDdStatusArr.addAll(zjsYyyArr);
                        zjsDdStatusArr.addAll(zjsDzzArr);
                        zjsDdStatusArr.addAll(zjsYjdArr);
                        zjsDdStatusArr.addAll(zjsYddArr);
                        zjsDdStatusArr.addAll(zjsWxArr);

                        dsInvalidVO.setDsInvalidStatus(arrToStr(statusArr));
                        dsInvalidVO.setDsInvalidLevel(arrToStr(yxLevelArr));
                        dsInvalidVO.setDdIsValid(configObj.getJSONObject(ReportsConfigConst.YX_SET).getBoolean(ReportsConfigConst.DD_IS_YX));
                        dsInvalidVO.setDsDdStatus(arrToStr(dsDdStatusArr));
                        dsInvalidVO.setZjsValidStatus(arrToStr(zjsDdStatusArr));

                        if(null == configObj.getJSONObject(ReportsConfigConst.SHOW_SET)){
                            dsInvalidVO.setSourceShowStatus(false);
                        }
                        dsInvalidVO.setSourceShowStatus(configObj.getJSONObject(ReportsConfigConst.SHOW_SET).getBoolean(ReportsConfigConst.SOURCE_SHOW));

                        return dsInvalidVO;
                    }
                });
        return dsInvalidVO;
    }

    /**
     * 数组转字符串
     * */
    private String arrToStr(JSONArray jsonArray){
        String str = "";
        if(!jsonArray.isEmpty()){
            str = jsonArray.toString().substring(1, jsonArray.toString().length() - 1);
        }
        return str;
    }


}
