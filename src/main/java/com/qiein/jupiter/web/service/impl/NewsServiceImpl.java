package com.qiein.jupiter.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.NewsDao;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.NewsPO;
import com.qiein.jupiter.web.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao newsDao;

    /**
     * 获取所有消息，分页
     *
     * @param queryMapDTO
     * @param uid
     * @param cid
     * @return
     */
    @Override
    public PageInfo getAllList(QueryMapDTO queryMapDTO, int uid, int cid) {
        //根据公司获取表名
        String tableName = DBSplitUtil.getTableName(DBSplitUtil.NEWS_, cid);
        //获取类型
        String type = queryMapDTO.getCondition() == null ? "" : (String) queryMapDTO.getCondition().get("type");
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        List<NewsPO> allByStaffIdAndCid = newsDao.getAllByStaffIdAndCid(
                tableName, type, uid, cid);
        return new PageInfo<>(allByStaffIdAndCid);

    }

    /**
     * 获取所有未读消息，分页
     *
     * @param queryMapDTO
     * @param uid
     * @param cid
     * @return
     */
    @Override
    public PageInfo getNotReadList(QueryMapDTO queryMapDTO, int uid, int cid) {
        //根据公司获取表名
        String tableName = DBSplitUtil.getTableName(DBSplitUtil.NEWS_, cid);
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        //获取类型
        String type = queryMapDTO.getCondition() == null ? "" : (String) queryMapDTO.getCondition().get("type");
        List<NewsPO> allByStaffIdAndCid = newsDao.getNotReadByStaffIdAndCid(
                tableName, type, uid, cid);
        return new PageInfo<>(allByStaffIdAndCid);
    }

    @Override
    public int batchUpdateNewsReadFlag(String msgIds, int cid) {
        //根据公司获取表名
        String tableName = DBSplitUtil.getTableName(DBSplitUtil.NEWS_, cid);
        if (StringUtil.isNullStr(msgIds)) {
            //todo
            return 0;
        }
        return newsDao.batchUpdateNewsReadFlag(tableName, msgIds.split(CommonConstant.STR_SEPARATOR));
    }
}
