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
        clientDao.editClientBaseInfo(clientStatusVO);

        int addLogNum = clientLogDao.addInfoLog(new ClientLogPO(clientStatusVO.getKzId(), clientStatusVO.getOperaId(), clientStatusVO.getOperaName(),
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
        clientDao.editClientBaseInfo(clientStatusVO);

        int addLogNum = clientLogDao.addInfoLog(new ClientLogPO(clientStatusVO.getKzId(), clientStatusVO.getOperaId(), clientStatusVO.getOperaName(),
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
        clientDao.editClientMemoLabel(companyId, kzId, "【微信已扫码】");
        clientLogDao.addInfoLog(new ClientLogPO(kzId,
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
        result.put("notAllot", clientDao.getKzNumByStatusId(ClientStatusConst.BE_WAIT_MAKE_ORDER, companyId));
        result.put("beAlloting", clientDao.getKzNumByStatusId(ClientStatusConst.BE_ALLOTING, companyId));
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
        clientDao.updateKzValidStatusByKzId(clientStatusVoteVO);
        //是否有备注

        //获取客资时候有备注
        if (StringUtil.isNotEmpty(clientStatusVoteVO.getContent())) {
            clientDao.updateDetailMemo(clientStatusVoteVO.getKzId(), clientStatusVoteVO.getCompanyId(), clientStatusVoteVO.getContent());
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
        int addLogNum = clientLogDao.addInfoLog(new ClientLogPO(clientStatusVoteVO.getKzId(), clientStatusVoteVO.getOperaId(), clientStatusVoteVO.getOperaName(),
                ClientLogConst.INFO_LOG_EDIT_BE_STATUS + kzStatusName,
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
    public Integer findId(String kzId, Integer companyId) {
        return clientDao.findId(kzId, companyId);
    }

    /**
     * 查询已有客服的客资数量，用于分配
     *
     * @param kzIds
     * @param companyId
     * @return
     */
    public int listExistAppointClientsNum(String kzIds, int companyId) {
        return clientInfoDao.listExistAppointClientsNum(kzIds, companyId);
    }

    /**
     * 新增客资日志
     *
     * @param clientLogPO
     */
    @Override
    public void addClientLog(ClientLogPO clientLogPO) {
        clientLogDao.addInfoLog(clientLogPO);
    }

    /**
     * 查询客资收款修改日志
     *
     * @param logTabName
     * @param companyId
     * @param kzId
     * @param logType
     * @return
     */
    public List<ClientLogPO> getCashEditLog(int companyId, String kzId) {
        return clientLogDao.getCashEditLog(companyId, kzId, ClientLogConst.INFO_LOGTYPE_CASH);
    }

}
