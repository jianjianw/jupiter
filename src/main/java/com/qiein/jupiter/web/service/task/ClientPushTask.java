package com.qiein.jupiter.web.service.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.web.dao.CompanyDao;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.service.impl.ClientPushServiceImpl;
import com.qiein.jupiter.web.service.quene.ThreadTaskPushManager;

/**
 * 定时客资分配
 *
 * @author JingChenglong 2018/05/24 19:09
 */
@Service
public class ClientPushTask {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClientPushServiceImpl pushService;
    @Autowired
    private CompanyDao companyDao;

    // 客资推送线程池
    ThreadTaskPushManager tpm = ThreadTaskPushManager.getInstance();

    /**
     * 定时任务-推送客资
     */
  //  @Scheduled(initialDelay = 1000, fixedDelay = 30 * 1000)
    public void taskPushLp() {
        log.info("执行定时推送任务");
        List<CompanyPO> compList = companyDao.listComp();
        for (CompanyPO comp : compList) {
            //超时时间设置是秒，但是两个数据库是毫秒时间戳
            int overTime = comp.getOvertime() * 1000;
            List<ClientPushDTO> infoList = pushService.getInfoListBeReadyPush(comp.getId(), overTime);
            if (CollectionUtils.isEmpty(infoList)) {
                continue;
            }
            for (ClientPushDTO info : infoList) {
                tpm.pushInfo(new ClientPushDTO(pushService, info.getPushRule(), comp.getId(), info.getKzId(),
                        info.getShopId(), info.getChannelId(), info.getChannelTypeId(), comp.getOvertime(),
                        comp.getKzInterval(), info.getSourceId()));
            }
            log.info("推送了客资：" + infoList.size() + " 个");
        }
    }
}