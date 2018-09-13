package com.qiein.jupiter.web.service.quene;

import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 推送队列
 *
 * @Author: shiTao
 */
@Component
public class PushQueue implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    /**
     * 队列
     */
    private BlockingQueue<ClientPushDTO> queue = new LinkedBlockingQueue<>();
    /**
     * 单线程线程池
     */
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    /**
     * 定时调度线程池
     */
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1000);

    private PushQueue() {
    }

    /**
     * 队列添加客资
     *
     * @param clientDTO
     */
    public void offer(ClientPushDTO clientDTO) {
        this.queue.offer(clientDTO);
        log.info("队列客资数量:" + queue.size());
        scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                doPush();
            }
        });
    }

    /**
     * 取出队列客资并进行寻找合适人员进行推送
     */
    private void doPush() {
        try {
            //阻塞获取
            ClientPushDTO take = this.queue.take();
            take.getService().pushLp(take.getPushRule(), take.getCompanyId(),
                    take.getKzId(), take.getSrcType(), take.getOverTime(), take.getPushInterval(), take.getSourceId());
//            log.info("推送了一条客资");
            //继续获取
        } catch (InterruptedException e) {
            log.info("推送队列客资出错！");
            e.printStackTrace();
        }
    }

    /**
     * 守护线程
     *
     * @param strings
     * @throws Exception
     */
    @Override
    public void run(String... strings) throws Exception {
        log.info("开始定时队列推送任务");
        //休眠十秒开始执行
        int initDelay = 10;
        //时间间隔
        int period = 1;
        //定时推送队列中的客资
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (!queue.isEmpty()) {
                    doPush();
                }
            }
        }, initDelay, period, TimeUnit.SECONDS);
    }

    /**
     * 队列是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
