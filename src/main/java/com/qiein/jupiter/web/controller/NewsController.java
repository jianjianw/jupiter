package com.qiein.jupiter.web.controller;

import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.enums.TigMsgEnum;
import com.qiein.jupiter.util.ResultInfo;
import com.qiein.jupiter.util.ResultInfoUtil;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.NewsTotalAmountAndFlag;
import com.qiein.jupiter.web.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户消息API
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
    @PostMapping("/get_all_list")
    public ResultInfo getAllList(@RequestBody QueryMapDTO queryMapDTO) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        PageInfo allList = newsService.getAllList(
                queryMapDTO, currentLoginStaff.getId(), currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(allList);
    }

    /**
     * 获取未读消息
     *
     * @param queryMapDTO
     * @return
     */
    @PostMapping("/get_not_read_list")
    public ResultInfo getNotReadList(@RequestBody QueryMapDTO queryMapDTO) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        PageInfo allList = newsService.getNotReadList(
                queryMapDTO, currentLoginStaff.getId(), currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(allList);
    }

    /**
     * 批量更新当前用户消息为已读
     *
     * @param ids
     * @return
     */
    @GetMapping("/batch_update_news_read_flag")
    public ResultInfo batchUpdateNewsReadFlag(@RequestParam String ids) {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        newsService.batchUpdateNewsReadFlag(ids, currentLoginStaff.getId(), currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(TigMsgEnum.UPDATE_SUCCESS);
    }

    /**
     * 获取全部类型的消息数量 及是否存在未读
     *
     * @return
     */
    @GetMapping("/get_news_total_amount_and_flag")
    public ResultInfo getNewsTotalAmountAndFlag() {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        NewsTotalAmountAndFlag newsTotalAmountAndFlag = newsService.getNewsTotalAmountAndFlag(currentLoginStaff.getId(),
                currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(newsTotalAmountAndFlag);
    }

    /**
     * 设置当前登录用户的所有消息为已读
     *
     * @return
     */
    @GetMapping("/set_all_news_is_read")
    public ResultInfo setAllNewsIsRead() {
        //获取当前登录用户
        StaffPO currentLoginStaff = getCurrentLoginStaff();
        newsService.setAllNewIsRead(currentLoginStaff.getId(), currentLoginStaff.getCompanyId());
        return ResultInfoUtil.success(TigMsgEnum.OPERATE_SUCCESS);
    }
}
