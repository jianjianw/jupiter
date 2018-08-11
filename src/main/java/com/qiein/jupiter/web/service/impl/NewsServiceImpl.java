package com.qiein.jupiter.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiein.jupiter.constant.ClientLogConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.dto.ClientGoEasyDTO;
import com.qiein.jupiter.web.entity.dto.QueryMapDTO;
import com.qiein.jupiter.web.entity.po.ClientLogPO;
import com.qiein.jupiter.web.entity.po.NewsPO;
import com.qiein.jupiter.web.entity.po.StaffPO;
import com.qiein.jupiter.web.entity.vo.NewsTotalAmountAndFlag;
import com.qiein.jupiter.web.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息
 *
 * @author shiTao
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao newsDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private ClientLogDao clientLogDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private ClientInfoDao clientInfoDao;

    /**
     * 获取所有消息，分页
     *
     * @param queryMapDTO
     * @param uid
     * @param cid
     * @return
     */
    @Override
    public PageInfo<NewsPO> getAllList(QueryMapDTO queryMapDTO, int uid, int cid) {
        // 获取类型
        Integer type = queryMapDTO.getCondition() == null ? 1 : (Integer) queryMapDTO.getCondition().get("type");
        NewsPO newsPO = new NewsPO();
        newsPO.setStaffId(uid);
        newsPO.setCompanyId(cid);
        newsPO.setType(String.valueOf(type));
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
    public PageInfo<NewsPO> getNotReadList(QueryMapDTO queryMapDTO, int uid, int cid) {
        // 获取类型
        Integer type = queryMapDTO.getCondition() == null ? 1 : (Integer) queryMapDTO.getCondition().get("type");
        NewsPO newsPO = new NewsPO();
        newsPO.setStaffId(uid);
        newsPO.setCompanyId(cid);
        newsPO.setType(String.valueOf(type));
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
    public int batchUpdateNewsReadFlag(String msgIds, int staffId, int cid) {
        if (StringUtil.isEmpty(msgIds)) {
            return 0;
        }
        // ids数组
        String[] msgIdsArr = msgIds.split(CommonConstant.STR_SEPARATOR);
        return newsDao.batchUpdateNewsReadFlag(staffId, msgIdsArr, cid);
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
        NewsPO newsPO = new NewsPO();
        newsPO.setStaffId(uid);
        newsPO.setCompanyId(cid);
        // 获取其他三种未读消息的数量
        newsPO.setReadFlag(false);
        newsPO.setType("1");
        int type1Amount = newsDao.getDiffTypeMsgAmount(newsPO);
        newsPO.setType("2");
        int type2Amount = newsDao.getDiffTypeMsgAmount(newsPO);
        newsPO.setType("3");
        int type3Amount = newsDao.getDiffTypeMsgAmount(newsPO);
        // 生成消息数量实体
        NewsTotalAmountAndFlag newsTotalAmountAndFlag = new NewsTotalAmountAndFlag();
        // 放入数量及是否有各种类型的未读消息
        newsTotalAmountAndFlag.setAllAmount(type1Amount + type2Amount + type3Amount);
        newsTotalAmountAndFlag.setKzType(type1Amount > 0);
        newsTotalAmountAndFlag.setNoticeType(type2Amount > 0);
        newsTotalAmountAndFlag.setSystemType(type3Amount > 0);
        return newsTotalAmountAndFlag;
    }

    /**
     * 更新员工的所有消息为已读
     *
     * @param uid
     * @param cid
     * @return
     */
    @Override
    public int setAllNewIsRead(int uid, int cid) {
        return newsDao.setAllNewIsRead(uid, cid);
    }

    /**
     * 推送闪信信息，，催一次，崔二次
     *
     * @param kzId
     * @param toStaffId
     * @param msg
     * @param opera
     */
    public void pushInfoNewsInApp(String kzId, int toStaffId, String msg, StaffPO opera) {
        ClientGoEasyDTO info = clientInfoDao.getClientGoEasyDTOById(kzId,
                DBSplitUtil.getInfoTabName(opera.getCompanyId()),
                DBSplitUtil.getDetailTabName(opera.getCompanyId()));
        if (info == null) {
            throw new RException(ExceptionEnum.INFO_ERROR);
        }
        StaffPO toStaff = staffDao.getByIdAndCid(toStaffId, opera.getCompanyId());
        if (toStaff == null) {
            throw new RException(ExceptionEnum.STAFF_IS_NOT_EXIST);
        }
        //推送闪信
        GoEasyUtil.pushFlash(opera.getCompanyId(), toStaffId, info, opera.getId(), opera.getNickName(), msg, newsDao);
        //添加备注
        clientDao.editClientMemoLabel(DBSplitUtil.getDetailTabName(opera.getCompanyId()), opera.getCompanyId(), kzId, "【" + msg + "】");
        // 客资日志记录
        clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(opera.getCompanyId()),
                new ClientLogPO(kzId, opera.getId(), opera.getNickName(), ("来自应用内闪信" + " - 发送给->" + toStaff.getNickName()) + msg,
                        ClientLogConst.INFO_LOGTYPE_INVITE, opera.getCompanyId()));
    }

}
