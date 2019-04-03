package com.jellyfish.sell.wechat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 */
@Component
public class TemplateThreadPool {

    @PostConstruct
    public void init() {
        synchronized (lock) {
            BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
                    queueSize);
            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize, keepAliveTimeInMillSeconds,
                    TimeUnit.MILLISECONDS, workQueue);
        }
    }

    public int addTask(Runnable task) {
        synchronized (lock) {
            if (task == null) {
                logger.warn("task is null");
                return 0;
            }
            int activeCount = threadPoolExecutor.getActiveCount();
            int maxCount = threadPoolExecutor.getMaximumPoolSize();
            if (activeCount >= maxCount) {
                logger.info("Add task docRead[" + task + "] FAIL, work queue full");
                return 2;
            }
            logger.info("Add task docRead[" + task + "] SUCCESS");
            threadPoolExecutor.execute(task);
            return 0;
        }
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setKeepAliveTimeInMillSeconds(long keepAliveTimeInMillSeconds) {
        this.keepAliveTimeInMillSeconds = keepAliveTimeInMillSeconds;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    private byte[] lock = new byte[0];
    @Value("${Thread.corePoolSize}")
    public int corePoolSize = 5;
    @Value("${Thread.maximumPoolSize}")
    public int maximumPoolSize = 10;
    private long keepAliveTimeInMillSeconds = 1000;
    @Value("${Thread.queueSize}")
    public int queueSize = 2048;
    private ThreadPoolExecutor threadPoolExecutor;
    private static final Logger logger = LoggerFactory.getLogger(TemplateThreadPool.class);
}
