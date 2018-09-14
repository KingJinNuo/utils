package com.cmos.task;

import java.io.*;
import java.util.concurrent.*;

import com.cmos.util.DownloadUtil;
import com.cmos.util.PropertyUtil;
import org.apache.log4j.Logger;

/**
 * 下载任务
 * @author Wx
 */
public class DownloadTask {
    /**
     * 文件下载
     */
    public void down() throws IOException, InterruptedException {
        Logger log = Logger.getLogger(DownloadTask.class);
        //创建下载线程池
        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1 + Integer.parseInt(PropertyUtil.p.getProperty("DownloadThreadPoolSize")));
        //定时查询将要下载的文件，放在BlockingQueue队列
        QueryFileTask timeTaskQueryLoad = new QueryFileTask();
        threadPoolExecutor.scheduleAtFixedRate(timeTaskQueryLoad, 0,
                Integer.parseInt(PropertyUtil.p.getProperty("DownloadScheduleTime")), TimeUnit.MINUTES);
        Thread.sleep(500);
        log.info("---------------------遍历下载资源、分配下载线程-----------------------");
        //创建下载任务，创建下载线程
        DownloadUtil downloadUtil = new DownloadUtil();
        for (int i = 0; i < Integer.parseInt(PropertyUtil.p.getProperty("FilesEveryDownloadTask")); i++)
            threadPoolExecutor.scheduleAtFixedRate(downloadUtil, 0,
                    Integer.parseInt(PropertyUtil.p.getProperty("DownloadScheduleTime")), TimeUnit.MINUTES);
    }

}
