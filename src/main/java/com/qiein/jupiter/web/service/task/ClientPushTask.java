package com.qiein.jupiter.web.service.task;

import com.qiein.jupiter.util.CollectionUtils;
import com.qiein.jupiter.web.dao.CompanyDao;
import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import com.qiein.jupiter.web.entity.po.CompanyPO;
import com.qiein.jupiter.web.service.impl.ClientPushServiceImpl;
import com.qiein.jupiter.web.service.quene.PushQueue;
import com.qiein.jupiter.web.service.quene.PushSkQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时客资分配
 *
 * @author JingChenglong 2018/05/24 19:09
 */
@Service
public class ClientPushTask {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    private ClientPushServiceImpl pushService;
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private PushQueue lpPushQueue;
    @Autowired
    private PushSkQueue pushSkQueue;

    /**
     * 定时任务-推送客资
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 10 * 1000)
    public void taskPushLp() {
        //如果是测试环境，就不再推送
//        if (active.equals("dev")) {
//            log.info("测试环境，暂不推送...");
//            return;
//        }
        //先判断下队列是否为空,只有队列为空，才去数据库找客资推送
        if (!lpPushQueue.isEmpty()) {
            log.info("客资队列不为空，暂不推送...");
            return;
        }
        log.info("执行定时推送任务");
        List<CompanyPO> compList = companyDao.listComp();
        for (CompanyPO comp : compList) {
            //超时时间设置是秒
            int overTime = comp.getOverTime();
            List<ClientPushDTO> infoList = pushService.getInfoListBeReadyPush(comp.getId(), overTime);
            if (CollectionUtils.isEmpty(infoList)) {
                continue;
            }
            for (ClientPushDTO info : infoList) {
                pushClient(new ClientPushDTO(pushService, info.getPushRule(), comp.getId(), info.getKzId(), info.getSrcType(), comp.getOverTime(),
                        comp.getKzInterval(), info.getSourceId()));
            }
            log.info("推送了客资：" + infoList.size() + " 个");
        }
    }


    /**
     * 客资队列分配
     *
     * @param info
     */
    private void pushClient(ClientPushDTO info) {
        //老版本推送
//        tpm.pushInfo(info);
        //新版本推送
        lpPushQueue.offer(info);
    }

    /**
     * 定时任务-推送客资给筛客
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 60 * 1000)
    public void taskPushTo() {
        //如果是测试环境，就不再推送
        if (active.equals("dev")) {
            log.info("测试环境，暂不推送...");
            return;
        }
        //先判断下队列是否为空,只有队列为空，才去数据库找客资推送
        if (!pushSkQueue.isEmpty()) {
            log.info("筛客队列不为空，暂不推送...");
            return;
        }
        log.info("执行定时推送筛客任务");
        List<CompanyPO> compList = companyDao.listSkAvgComp();
        for (CompanyPO comp : compList) {
            //超时时间设置是秒
            List<ClientPushDTO> kzList = pushService.getSkInfoList(comp.getId(), comp.getOverTime());
            if (CollectionUtils.isEmpty(kzList)) {
                continue;
            }
            for (ClientPushDTO info : kzList) {
                pushSkQueue.offer(new ClientPushDTO(pushService, comp.getKzInterval(), comp.getOverTime(), info.getKzId(), comp.getId(), info.getSrcType()));
            }
            log.info("推送了客资：" + kzList.size() + " 个");
        }
    }

}