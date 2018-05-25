package com.qiein.jupiter.web.service.quene;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.qiein.jupiter.web.entity.dto.ClientPushDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客资定时分配任务
 *
 * @author JingChenglong 2018/05/25 10:05
 */
public class ThreadTaskPushManager {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static ThreadTaskPushManager tpm = new ThreadTaskPushManager();

    public static ThreadTaskPushManager getInstance() {
        return tpm;
    }

    private ThreadTaskPushManager() {
    }

    private final static int CORE_POOL_SIZE = 1;// 线程池维护线程的最小数量。
    private final static int MAX_POOL_SIZE = 1;// 线程池维护线程的最大数量。
    private final static int KEEP_ALIVE_TIME = 100;// 线程池维护线程所允许的空闲时间。
    private final static int WORK_QUEUE_SIZE = 50;// 线程池所使用的缓冲队列大小。

    public void pushInfo(ClientPushDTO pushVO) {
        threadPool.execute(new PushThread(pushVO));
    }

    final RejectedExecutionHandler handler = new RejectedExecutionHandler() {

        public void rejectedExecution(Runnable arg0, ThreadPoolExecutor arg1) {
            if (!infoQueue.offer(((PushThread) arg0).getPushVO())) {
                log.info(
                        "*************************队列已满，ThreadInfoPushManager--offer失败*******************************");
            }
        }
    };

    // 管理工作线程的线程池
    @SuppressWarnings({"unchecked", "rawtypes"})
    final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, new LinkedBlockingQueue(WORK_QUEUE_SIZE), this.handler);

    Queue<ClientPushDTO> infoQueue = new LinkedList<ClientPushDTO>();// 客资信息缓冲队列

    // 访问消息缓存的调度线程
    // 查看是否有待定请求，如果有，则创建一个新的AccessDBThread并添加到线程池中。
    final Runnable accessBufferThread = new Runnable() {

        public void run() {

            if (hasMoreAcquire()) {
                PushThread vo = new PushThread(infoQueue.poll());
                threadPool.execute(vo);
            }
        }
    };

    // 调度线程池
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1000);

    @SuppressWarnings("rawtypes")
    final ScheduledFuture taskHandler = scheduler.scheduleAtFixedRate(accessBufferThread, 0, 1, TimeUnit.SECONDS);

    private boolean hasMoreAcquire() {
        return !infoQueue.isEmpty();
    }
}