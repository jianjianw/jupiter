package com.qiein.jupiter.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.enums.QueryTimeTypeEnum;
import com.qiein.jupiter.util.NumUtil;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.QueryVO;
import com.qiein.jupiter.web.service.PlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: shiTao
 */
@RestController
@RequestMapping("/plat")
public class PlatController extends BaseController {
    @Autowired
    private PlatService platService;

    /**
     * 页面搜索 根据Key
     *
     * @return
     */
    @GetMapping("/page_search_by_key")
    public ResultInfo pageSearchByKey(String key) {
        if (StringUtil.isEmpty(key)) {
            return ResultInfoUtil.success();
        }
        return ResultInfoUtil.success(platService.pageSearch(getCurrentLoginStaff().getCompanyId(), key));
    }

    /**
     * 根据KZID 查询客资
     */
    @GetMapping("/get_client_by_kzid")
    public ResultInfo getClientByKzi(String kzid) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        QueryVO queryVO = new QueryVO();
        queryVO.setKzId(kzid);
        queryVO.setUid(currentLoginStaff.getId());
        queryVO.setCompanyId(currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(platService.getClientInfoByKzid(queryVO));
    }

    /**
     * 查询删除客资
     */
    @GetMapping("/get_del_client")
    public ResultInfo getDelClientInfo() {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        QueryVO queryVO = new QueryVO();
        queryVO.setUid(currentLoginStaff.getId());
        queryVO.setCompanyId(currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(platService.getDelClient(queryVO));
    }

    /**
     * 查询重复客资
     */
    @GetMapping("/get_repeat_client_by_key")
    public ResultInfo getDelClientInfo(String key) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        QueryVO queryVO = new QueryVO();
        queryVO.setUid(currentLoginStaff.getId());
        queryVO.setCompanyId(currentLoginStaff.getCompanyId());
        queryVO.setSearchKey(key);
        queryVO.setCurrentPage(0);
        queryVO.setPageSize(50);
        return ResultInfoUtil.success(platService.getRepeatClientHsWeb(queryVO));
    }


    /**
     * 页面查询客资
     */
    @PostMapping("/query_page_client_info")
    public ResultInfo queryPageClientInfo(@RequestBody JSONObject content) {
        QueryVO queryVO = initQueryVo(content);
        queryVO.setClassId(ClientStatusConst.getClassByAction(queryVO.getAction()));
        return ResultInfoUtil.success(platService.queryPageClientInfo(queryVO));
    }

    /**
     * 页面统计客资
     */
    @PostMapping("/query_page_client_info_count")
    public ResultInfo queryPageClientInfoCount(@RequestBody JSONObject content) {
        QueryVO queryVO = initQueryVo(content);
        return ResultInfoUtil.success(platService.queryPageClientInfoCount(queryVO));
    }

    /**
     * 初始化参数
     *
     * @param content
     * @return
     */
    private QueryVO initQueryVo(JSONObject content) {
        StaffPO currentLoginStaff = getCurrentLoginStaff();

        QueryVO queryVO = new QueryVO();
        queryVO.setCurrentPage(content.getIntValue("page") - 1);
        queryVO.setPageSize(content.getIntValue("size"));
        queryVO.setTimeType(QueryTimeTypeEnum.getTimeTypeById(NumUtil.valueOf(content.getString("timetype"))));
        queryVO.setStart(content.getIntValue("start"));
        queryVO.setEnd(content.getIntValue("end"));
        queryVO.setUid(currentLoginStaff.getId());
        queryVO.setCompanyId(currentLoginStaff.getCompanyId());

        queryVO.setAction(content.getString("action"));

        //
        queryVO.setRole(content.getString("role"));
        queryVO.setChannelId(content.getString("channelid"));
        queryVO.setSourceId(content.getString("sourceid"));
        queryVO.setShopId(content.getString("shopid"));
        queryVO.setStaffId(content.getString("staffid"));
        queryVO.setTypeId(content.getString("typeid"));
        queryVO.setYxLevel(content.getString("yxlevel"));
        queryVO.setAppointorId(content.getString("appointids"));
        //
        queryVO.setPmsLimit(content.getIntValue("pmslimit"));
        queryVO.setLinkLimit(content.getString("linklimit"));
        queryVO.setSpareSql(content.getString("sparesql"));
        queryVO.setFilterSql(content.getString("filtersql"));
        queryVO.setSuperSql(content.getString("supersql"));
        return queryVO;
    }




}
