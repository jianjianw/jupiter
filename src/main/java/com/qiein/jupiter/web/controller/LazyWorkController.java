package com.qiein.jupiter.web.controller;

import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.LazyWorkVO;
import com.qiein.jupiter.web.service.LazyWorkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Tt on 2018/5/16 0016.
 */
@RestController
@RequestMapping("/lazy_work")
public class LazyWorkController extends BaseController {

    @Resource
    private LazyWorkService lazyWorkService;

    /**
     * 获取个人怠工日志
     *
     * @param staffId
     * @return
     */
    @RequestMapping("/get_person_list")
    public ResultInfo getLazyWorkList(int staffId) {
        List<LazyWorkVO> list = lazyWorkService.getLazyWorkListByStaffId(staffId, getCurrentLoginStaff().getCompanyId());
        return ResultInfoUtil.success(list);
    }

    /**
     * 查询怠工日志
     *
     * @param lazyWorkVO
     * @return
     */
    @GetMapping("/get_list")
    public ResultInfo getLazyWorkList(LazyWorkVO lazyWorkVO) {
        StaffPO staffPO = getCurrentLoginStaff();
        lazyWorkVO.setCompanyId(staffPO.getCompanyId());
        return ResultInfoUtil.success(lazyWorkService.getLazyWorkList(lazyWorkVO));
    }
}
