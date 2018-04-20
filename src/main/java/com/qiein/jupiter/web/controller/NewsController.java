package com.qiein.jupiter.web.controller;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.constant.TipMsgConstant;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户消息API
 *
 * @date 2018-4-20 15:01:06
 */
@RequestMapping("/news")
@RestController
public class NewsController extends BaseController {

    @Autowired
    private NewsService newsService;

    /**
     * 获取所有消息
     *
     * @param queryMapDTO
     * @return
     */
    @GetMapping("/get_all_list")
    public ResultInfo getAllList(QueryMapDTO queryMapDTO) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        PageInfo allList = newsService.getAllList(queryMapDTO, currentLoginStaff.getId(), currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(allList);
    }

    /**
     * 获取未读消息
     *
     * @param queryMapDTO
     * @return
     */
    @GetMapping("/get_not_read_list")
    public ResultInfo getNotReadList(QueryMapDTO queryMapDTO) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        PageInfo allList = newsService.getNotReadList(queryMapDTO, currentLoginStaff.getId(), currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(allList);
    }

    /**
     * 批量更新消息为已读
     *
     * @param ids
     * @return
     */
    @GetMapping("/batch_update_news_read_flag")
    public ResultInfo batchUpdateNewsReadFlag(@RequestParam String ids) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        newsService.batchUpdateNewsReadFlag(ids, currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(TipMsgConstant.UPDATE_SUCCESS);
    }
}
