package com.qiein.jupiter.web.timer;

import com.qiein.jupiter.web.service.ClientPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: shiTao
 */
@Component
public class TimerTask {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClientPushService clientPushService;

    /**
     * 每天8-23点  2分钟执行一次
     */
    @Scheduled(cron = "0 */2  8-23 * * ?")
    public void pushClientNotice() {
        clientPushService.pushClientNoticeInfo();
        log.info("执行了定时推送提醒任务...");
    }
}
