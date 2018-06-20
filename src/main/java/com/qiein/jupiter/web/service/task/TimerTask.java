package com.qiein.jupiter.web.service.task;

import com.qiein.jupiter.web.service.ClientPushService;
import com.qiein.jupiter.web.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @Author: shiTao
 */
@Component
public class TimerTask {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClientPushService clientPushService;

    @Autowired
    private StaffService staffService;

    /**
     * 每天8-23点  2分钟执行一次
     */
    @Scheduled(cron = "0 */2  8-23 * * ?")
    public void pushClientNotice() {
        clientPushService.pushClientNoticeInfo();
        log.info("执行了定时推送提醒任务...");
    }


    /**
     * 每天 0 点重置客服今天接单数目
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetKfOderNum() {
        staffService.taskClockInit();
        log.info("定时清空了客服今天接单数目...");
    }
}
