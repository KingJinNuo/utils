package com.cmos.task;

import com.cmos.util.PropertyUtil;
import com.cmos.util.UploadUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Queue;
import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 文件上传任务类
 * Author ：wx
 * Date ：Created in  2018/9/4 8:27
 */
public class UploadTask {
    Logger log = Logger.getLogger(UploadTask.class);
    /**
     * 文件上传，任务定时
     */
    public void uploadTask() {
        // 进行文件传输
        Timer timer = new Timer();
        UploadUtil ftpUploadUtil = new UploadUtil();
        timer.scheduleAtFixedRate(ftpUploadUtil,0,Integer.parseInt(PropertyUtil.p.getProperty("UploadResultTime")));

    }

}
