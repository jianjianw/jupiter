package com.qiein.jupiter.web.service.impl;

import ch.qos.logback.classic.db.names.TableName;
import com.qiein.jupiter.constant.*;
import com.qiein.jupiter.enums.TableEnum;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.msg.goeasy.ClientDTO;
import com.qiein.jupiter.msg.goeasy.GoEasyUtil;
import com.qiein.jupiter.util.DBSplitUtil;
import com.qiein.jupiter.util.StringUtil;
import com.qiein.jupiter.web.dao.*;
import com.qiein.jupiter.web.entity.po.ClientLogPO;
import com.qiein.jupiter.web.entity.po.ClientRemarkPO;
import com.qiein.jupiter.web.entity.po.ClientStatusPO;
import com.qiein.jupiter.web.entity.vo.ClientStatusVO;
import com.qiein.jupiter.web.entity.vo.ClientStatusVoteVO;
import com.qiein.jupiter.web.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Tt on 2018/5/15 0015.
 */
@Service
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private ClientLogDao clientLogDao;

    @Autowired
    private ClientRemarkDao clientRemarkDao;

    @Autowired
    private ClientStatusDao clientStatusDao;
    @Autowired
    private NewsDao newsDao;
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private ClientInfoDao clientInfoDao;


    /**
     * 编辑客资性别
     *
     * @param clientStatusVO
     */
    @Override
    @Transactional
    public void editClientSex(ClientStatusVO clientStatusVO) {
        clientDao.editClientBaseInfo(clientStatusVO, DBSplitUtil.getInfoTabName(clientStatusVO.getCompanyId()));

        int addLogNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(clientStatusVO.getCompanyId()),
                new ClientLogPO(clientStatusVO.getKzId(), clientStatusVO.getOperaId(), clientStatusVO.getOperaName(),
                        ClientLogConst.INFO_LOG_EDIT_SEX + (clientStatusVO.getSex() == 1 ? "先生" : "女士"),
                        ClientLogConst.INFO_LOGTYPE_EDIT, clientStatusVO.getCompanyId()));
        if (addLogNum != 1) {
            log.error("插入客资日志失败");
        }
    }

    /**
     * 编辑客资微信标识
     *
     * @param clientStatusVO
     */
    @Override
    @Transactional
    public void editClientWCFlag(ClientStatusVO clientStatusVO) {
        clientDao.editClientBaseInfo(clientStatusVO, DBSplitUtil.getInfoTabName(clientStatusVO.getCompanyId()));

        int addLogNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(clientStatusVO.getCompanyId()),
                new ClientLogPO(clientStatusVO.getKzId(), clientStatusVO.getOperaId(), clientStatusVO.getOperaName(),
                        ClientLogConst.INFO_LOG_EDIT_WCFLAG + (clientStatusVO.getWeFlag() == 1 ? "已添加" : "没加上"),
                        ClientLogConst.INFO_LOGTYPE_EDIT, clientStatusVO.getCompanyId()));
        if (addLogNum != 1) {
            log.error("插入客资日志失败");
        }
    }

    /**
     * 微信二位码扫描记录
     *
     * @param companyId
     * @param kzId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scanWechat(int companyId, String kzId) {
        clientDao.editClientMemoLabel(DBSplitUtil.getDetailTabName(companyId), companyId, kzId, "【微信已扫码】");
        clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(companyId), new ClientLogPO(kzId,
                CommonConstant.DEFAULT_ZERO, null, "通过扫描二维码复制了微信账号", ClientLogConst.INFO_LOGTYPE_SCAN_WECAHT, companyId));
    }

    /**
     * 根据状态筛选客资数量
     *
     * @param companyId
     * @return
     */
    @Override
    public HashMap<String, Integer> getKzNumByStatusId(int companyId) {
        HashMap<String, Integer> result = new HashMap<>();
        result.put("notAllot", clientDao.getKzNumByStatusId(ClientStatusConst.BE_WAIT_MAKE_ORDER, companyId, DBSplitUtil.getInfoTabName(companyId)));
        result.put("beAlloting", clientDao.getKzNumByStatusId(ClientStatusConst.BE_ALLOTING, companyId, DBSplitUtil.getInfoTabName(companyId)));
        return result;
    }


    /**
     * 修改客资状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateKzValidStatus(ClientStatusVoteVO clientStatusVoteVO) {
        if (null == clientStatusVoteVO) {
            throw new RException(ExceptionEnum.UNKNOW_ERROR);
        }
        //有效或待定，增加到备注表中
        Integer type = clientStatusVoteVO.getType();
//        String tabName = DBSplitUtil.getRemarkTabName(clientStatusVoteVO.getCompanyId());

        String kzStatusName = "";
        if (ClientStatusTypeConst.VALID_TYPE.equals(type)) {
            //有效
            kzStatusName = ClientConst.KZ_BZ_WATING_MAKE_ORDER;

            ClientStatusPO clientStatusPO = clientStatusDao.getClientStatusByStatusId(ClientStatusConst.BE_WAIT_MAKE_ORDER, clientStatusVoteVO.getCompanyId());
            clientStatusVoteVO.setStatusId(ClientStatusConst.BE_WAIT_MAKE_ORDER);
            clientStatusVoteVO.setClassId(clientStatusPO.getClassId());
        } else if (ClientStatusTypeConst.WATING_TYPE.equals(type)) {
            //待定
            kzStatusName = ClientConst.KZ_BZ_WATING_NAME;
            ClientStatusPO clientStatusPO = clientStatusDao.getClientStatusByStatusId(ClientStatusConst.BE_WAIT_MAKE_ORDER, clientStatusVoteVO.getCompanyId());
            clientStatusVoteVO.setStatusId(ClientStatusConst.BE_WAIT_WAITING);
            clientStatusVoteVO.setClassId(clientStatusPO.getClassId());

        } else if (ClientStatusTypeConst.INVALID_TYPE.equals(type)) {
            kzStatusName = ClientConst.KZ_BZ_INVALID_NAME;

            ClientStatusPO clientStatusPO = clientStatusDao.getClientStatusByStatusId(ClientStatusConst.BE_WAIT_MAKE_ORDER, clientStatusVoteVO.getCompanyId());
            clientStatusVoteVO.setStatusId(ClientStatusConst.BE_FILTER_INVALID);
            clientStatusVoteVO.setClassId(clientStatusPO.getClassId());

            //无效客资Goeasy推送一条消息
            ClientDTO clientDTO = new ClientDTO();
            clientDTO.setKzId(clientStatusVoteVO.getKzId());
            clientDTO.setChannelName(clientStatusVoteVO.getChannelName());
            clientDTO.setKzQq(clientStatusVoteVO.getKzQq());
            clientDTO.setKzWeChat(clientStatusVoteVO.getKzWeChat());
            clientDTO.setKzPhone(clientStatusVoteVO.getKzPhone());
            clientDTO.setKzName(clientStatusVoteVO.getKzName());
            clientDTO.setSrcName(clientStatusVoteVO.getSourceName());
            GoEasyUtil.pushInvalidKz(clientStatusVoteVO.getCompanyId(), clientStatusVoteVO.getCollectorId(), clientDTO, clientStatusVoteVO.getContent(), newsDao, staffDao);
        }


        //修改状态id
        clientDao.updateKzValidStatusByKzId(DBSplitUtil.getInfoTabName(clientStatusVoteVO.getCompanyId()), clientStatusVoteVO);
        String memo = "";
        //是否有备注
        //获取客资时候有备注
        if (StringUtil.isNotEmpty(clientStatusVoteVO.getContent())) {
            //对长度进行校验 -- 不能超过200
            if (StringUtil.isNotEmpty(clientStatusVoteVO.getReason())) {
                if (clientStatusVoteVO.getReason().length() >= 200) {
                    clientStatusVoteVO.setReason(clientStatusVoteVO.getReason().substring(0, 199));
                }
                memo = "无效原因:" + clientStatusVoteVO.getContent() + ",无效备注：" + clientStatusVoteVO.getReason();
            } else {
                clientStatusVoteVO.setContent(clientStatusVoteVO.getContent());
                memo = clientStatusVoteVO.getContent();
            }
            clientDao.updateDetailMemo(DBSplitUtil.getDetailTabName(clientStatusVoteVO.getCompanyId()), clientStatusVoteVO.getKzId(), clientStatusVoteVO.getCompanyId(), memo,clientStatusVoteVO.getContent(),clientStatusVoteVO.getReason());
        }
        //FIXME 废弃代码
//        ClientRemarkPO clientRemarkPO = new ClientRemarkPO();
//        clientRemarkPO.setKzId(clientStatusVoteVO.getKzId());
//        clientRemarkPO.setCompanyId(clientStatusVoteVO.getCompanyId());
//        clientRemarkPO.setContent(clientStatusVoteVO.getContent());
//
//        ClientRemarkPO clientRemark = clientRemarkDao.getById(tabName, clientRemarkPO);
//        if (null == clientRemark) {
//            clientRemarkDao.insert(tabName, clientRemarkPO);
//        } else {
//            clientRemarkDao.update(tabName, clientRemarkPO);
//        }

        //插入日志
        int addLogNum = clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(clientStatusVoteVO.getCompanyId()),
                new ClientLogPO(clientStatusVoteVO.getKzId(), clientStatusVoteVO.getOperaId(), clientStatusVoteVO.getOperaName(),
                        ClientLogConst.INFO_LOG_EDIT_BE_STATUS + kzStatusName + "；" + clientStatusVoteVO.getContent(),
                        ClientLogConst.INFO_LOGTYPE_EDIT, clientStatusVoteVO.getCompanyId()));
        if (addLogNum != 1) {
            log.error("修改客资状态日志失败");
        }
    }

    /**
     * 寻找 kz的主id
     *
     * @param kzId
     * @return
     */
    @Override
    public Integer findByKzId(String kzId, Integer companyId) {
        return clientDao.findId(kzId, companyId, DBSplitUtil.getInfoTabName(companyId));
    }

    /**
     * 查询已有客服的客资数量，用于分配
     *
     * @param kzIds
     * @param companyId
     * @return
     */
    @Override
    public int listExistAppointClientsNum(String kzIds, int companyId, String role) {
        String type = "APPOINTORID";
        if (RoleConstant.DSCJ.equals(role) || RoleConstant.DSSX.equals(role) || RoleConstant.ZJSSX.equals(role)) {
            type = "APPOINTORID";
        } else if (RoleConstant.DSYY.equals(role) || RoleConstant.ZJSYY.equals(role)
                || RoleConstant.MSJD.equals(role) || RoleConstant.CWZX.equals(role)) {
            type = "RECEPTORID";
        }
        return clientInfoDao.listExistAppointClientsNum(kzIds, companyId, DBSplitUtil.getInfoTabName(companyId), type);
    }

    /**
     * 新增客资日志
     *
     * @param clientLogPO
     */
    @Override
    public void addClientLog(ClientLogPO clientLogPO) {
        clientLogDao.addInfoLog(DBSplitUtil.getInfoLogTabName(clientLogPO.getCompanyId()), clientLogPO);
    }

    /**
     * 查询客资收款修改日志
     */
    public List<ClientLogPO> getCashEditLog(int companyId, String kzId) {
        return clientLogDao.getCashEditLog(DBSplitUtil.getInfoLogTabName(companyId), companyId, kzId, ClientLogConst.INFO_LOGTYPE_CASH);
    }

}
