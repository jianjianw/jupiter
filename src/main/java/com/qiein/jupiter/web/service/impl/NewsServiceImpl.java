package com.qiein.jupiter.web.service.impl;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.NewsDao;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.NewsPO;
import com.qiein.jupiter.web.entity.vo.NewsTotalAmountAndFlag;
import com.qiein.jupiter.web.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao newsDao;

    /**
     * 设置单条消息的已读状态
     *
     * @param id  消息Id
     * @param cid 公司id
     * @return
     */
    public int updateNewsReadFlag(int id, int cid) {
        //todo
        return 0;
    }

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
        NewsPO newsPO = new NewsPO();
        newsPO.setStaffId(uid);
        newsPO.setCompanyId(cid);
        newsPO.setType(type);
        newsPO.setTableName(tableName);
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        List<NewsPO> allByStaffIdAndCid = newsDao.getAllByStaffIdAndCid(newsPO);
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
        //获取类型
        String type = queryMapDTO.getCondition() == null ? "" : (String) queryMapDTO.getCondition().get("type");
        NewsPO newsPO = new NewsPO();
        newsPO.setStaffId(uid);
        newsPO.setCompanyId(cid);
        newsPO.setType(type);
        newsPO.setTableName(tableName);
        PageHelper.startPage(queryMapDTO.getPageNum(), queryMapDTO.getPageSize());
        List<NewsPO> allByStaffIdAndCid = newsDao.getNotReadByStaffIdAndCid(newsPO);
        return new PageInfo<>(allByStaffIdAndCid);
    }

    /**
     * 批量设置已读状态
     *
     * @param msgIds
     * @param cid
     * @return
     */
    @Override
    public int batchUpdateNewsReadFlag(String msgIds, int cid) {
        //根据公司获取表名
        String tableName = DBSplitUtil.getTableName(DBSplitUtil.NEWS_, cid);
        if (StringUtil. isEmpty(msgIds)) {
            //todo
            return 0;
        }
        return newsDao.batchUpdateNewsReadFlag(tableName, msgIds.split(CommonConstant.STR_SEPARATOR), cid);
    }

    /**
     * 首页获取总消息数量和各个消息是否存在未读
     *
     * @param uid
     * @param cid
     * @return
     */
    @Override
    public NewsTotalAmountAndFlag getNewsTotalAmountAndFlag(final int uid, final int cid) {
        //根据公司获取表名
        final String tableName = DBSplitUtil.getTableName(DBSplitUtil.NEWS_, cid);
        NewsPO newsPO = new NewsPO();
        newsPO.setTableName(tableName);
        newsPO.setStaffId(uid);
        newsPO.setCompanyId(cid);
        //获取所有的消息的数量
        long allAmount = PageHelper.count(new ISelect() {
            @Override
            public void doSelect() {
                NewsPO newsPO = new NewsPO();
                newsPO.setTableName(tableName);
                newsPO.setStaffId(uid);
                newsPO.setCompanyId(cid);
                newsDao.getAllByStaffIdAndCid(newsPO);
            }
        });
        //获取其他三种未读消息的数量
        newsPO.setReadFlag(false);
        newsPO.setType("1");
        int type1Amount = newsDao.getDiffTypeMsgAmount(newsPO);
        newsPO.setType("2");
        int type2Amount = newsDao.getDiffTypeMsgAmount(newsPO);
        newsPO.setType("3");
        int type3Amount = newsDao.getDiffTypeMsgAmount(newsPO);
        //生成消息数量实体
        NewsTotalAmountAndFlag newsTotalAmountAndFlag = new NewsTotalAmountAndFlag();
        //放入数量及是否有各种类型的未读消息
        newsTotalAmountAndFlag.setAllAmount(allAmount);
        newsTotalAmountAndFlag.setKzType(type1Amount > 0);
        newsTotalAmountAndFlag.setNoticeType(type2Amount > 0);
        newsTotalAmountAndFlag.setSystemType(type3Amount > 0);
        return newsTotalAmountAndFlag;
    }


}
