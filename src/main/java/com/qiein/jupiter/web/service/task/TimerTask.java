package com.qiein.jupiter.web.service.task;

import com.qiein.jupiter.util.TimeUtil;
import com.qiein.jupiter.web.service.ApolloService;
import com.qiein.jupiter.web.service.ClientPushService;
import com.qiein.jupiter.web.service.StaffService;
import com.qiein.jupiter.web.service.SystemLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private SystemLogService systemLogService;

    @Autowired
    private ApolloService apolloService;

    @Value("${spring.profiles.active}")
    private String active;

    /**
     * 每天8-23点  2分钟执行一次
     */
    @Scheduled(cron = "0 */2  8-23 * * ?")
    public void pushClientNotice() {
        //如果是测试环境，就不再推送
        if (active.equals("dev")) {
            log.info("测试环境，暂不推送...");
            return;
        }
        //todo 根据公司类型 推送
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

    /**
     * 凌晨三点下线
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void offLine() {
        //TODO 定时下线
        log.info("定时让所有在线员工全部离线...");
    }

    /**
     * 每天4点清空日志
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void clearSysLog() {
        int systemInt = TimeUtil.getSystemInt();
        //60天前
        systemInt -= 60 * 60 * 24 * 60;
        systemLogService.clearLog(systemInt);
        log.info("清空了60天之前的日志");
    }

    /**
     * 定时获取阿波罗地址
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 3 * 60 * 1000)
    public void getApolloIp() {
        log.info("定时获取阿波罗地址");
        apolloService.getApolloIp();
    }

}
