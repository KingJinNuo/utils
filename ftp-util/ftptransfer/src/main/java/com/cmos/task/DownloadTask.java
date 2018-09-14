package com.cmos.task;

import com.cmos.util.DownloadUtil;
import com.cmos.util.PropertyUtil;
import org.apache.log4j.Logger;

import java.util.Timer;

/**
 * 下载任务
 * @author Wx
 */
public class DownloadTask {
    /**
     * 文件下载
     */
    public void down() throws InterruptedException {
        Logger log = Logger.getLogger(DownloadTask.class);
        //定时查询将要下载的文件，放在BlockingQueue队列
        QueryFileTask timeTaskQuery = new QueryFileTask();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timeTaskQuery,0,Integer.parseInt(PropertyUtil.p.getProperty("DownloadScheduleTime")));
        Thread.sleep(500);
        log.info("---------------------遍历下载资源-----------------------");
        //创建下载任务，创建下载线程
        DownloadUtil downloadUtil = new DownloadUtil();
        timer.scheduleAtFixedRate(downloadUtil,2,Integer.parseInt(PropertyUtil.p.getProperty("DownloadScheduleTime")));
    }

}
