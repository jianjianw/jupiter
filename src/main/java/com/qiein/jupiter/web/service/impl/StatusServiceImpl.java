package com.qiein.jupiter.web.service.impl;

import com.qiein.jupiter.constant.ClientStatusConst;
import com.qiein.jupiter.constant.CommonConstant;
import com.qiein.jupiter.constant.DictionaryConstant;
import com.qiein.jupiter.exception.ExceptionEnum;
import com.qiein.jupiter.exception.RException;
import com.qiein.jupiter.web.dao.ClientInfoDao;
import com.qiein.jupiter.web.dao.ClientStatusDao;
import com.qiein.jupiter.web.entity.po.DictionaryPO;
import com.qiein.jupiter.web.entity.po.StatusPO;
import com.qiein.jupiter.web.service.DictionaryService;
import com.qiein.jupiter.web.service.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 状态管理
 *
 * @author JingChenglong 2018/05/11 11:50
 */
@Service
public class StatusServiceImpl implements StatusService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClientStatusDao statusDao;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ClientInfoDao clientInfoDao;

    /**
     * 获取企业状态列表
     *
     * @param companyId
     * @return
     */
    public List<StatusPO> getCompanyStatusList(int companyId) {
        return statusDao.getCompanyStatusList(companyId);
    }

    /**
     * 编辑状态
     *
     * @param statusPO
     * @return
     */
    public void editStatus(StatusPO statusPO) {
        statusDao.editStatus(statusPO);
        StatusPO statusById = statusDao.getStatusById(statusPO.getCompanyId(), statusPO.getId());
        //判断是否是待跟踪状态
        boolean b = statusById.getClassId() == ClientStatusConst.KZ_CLASS_TRACK;
        if (b) {
            //如果是，则更新对应的跟踪状态的名称
            DictionaryPO dictionaryPO = new DictionaryPO();
            dictionaryPO.setCompanyId(statusPO.getCompanyId());
            dictionaryPO.setDicType(DictionaryConstant.TRACK_STATUS);
            dictionaryPO.setDicCode(statusById.getStatusId());
            dictionaryPO.setDicName(statusById.getStatusName());
            dictionaryService.updateDictNameByTypeAndCode(dictionaryPO);
        }

    }

    /**
     * 修改状态颜色为默认值
     */
    @Override
    public void editColorToDefault(int companyId, int id, String column) {

        StatusPO statusColor = statusDao.getStatusById(companyId, id);

        if (statusColor == null) {
            throw new RException(ExceptionEnum.STS_GET_ERROR);
        }

        StatusPO defaultColor = statusDao.getStatusByStatusId(CommonConstant.DEFAULT_COMPID, statusColor.getStatusId());

        if (defaultColor == null) {
            throw new RException(ExceptionEnum.STS_DEFAULT_ERROR);
        }

        statusDao.editStatusDefault(new StatusPO(id,
                StatusPO.STS_BGCOLOR.equals(column) ? defaultColor.getBackColor() : statusColor.getBackColor(),
                StatusPO.STS_FONTCOLOR.equals(column) ? defaultColor.getFontColor() : statusColor.getFontColor(),
                companyId));
    }

    /**
     * 获取企业状态的字典信息
     *
     * @param companyId
     * @return
     */
    @Override
    public Map<String, StatusPO> getStatusDictMap(int companyId) {
        List<StatusPO> companyStatusList = statusDao.getCompanyStatusList(companyId);
        Map<String, StatusPO> map = new HashMap<>();
        for (StatusPO statusPO : companyStatusList) {
            map.put(String.valueOf(statusPO.getStatusId()), statusPO);
        }
        return map;
    }

    /**
     *
     */
    @Override
    public int editNameByClassIdAndStatusId(StatusPO statusPO) {
        return statusDao.editNameByClassIdAndStatusId(statusPO);
    }
    /**
     * 修改客资状态
     * @param showFlag
     * @param id
     */
    public void editClientStatus(boolean showFlag,int id){
        statusDao.editClientStatus(showFlag, id);
    }
    /**
     * 修改手机是否已加状态
     * @param kzId
     * @param kzphoneFlag
     * @param table
     */
    public void editKzphoneFlag(String kzId,Integer kzphoneFlag,String table){
        clientInfoDao.editKzphoneFlag(kzId,kzphoneFlag,table);
    }
}