package com.cmos.task;

import com.cmos.util.PropertyUtil;
import com.cmos.util.UploadUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 文件上传任务类
 * Author ：wx
 * Date ：Created in  2018/9/4 8:27
 */
public class UploadTask {
    public static File fileDir;
    private static String[] filelist;
    public static Queue<String> uploadqueue = new LinkedBlockingQueue<>();
    Logger log = Logger.getLogger(UploadTask.class);
    /**
     * 文件上传，任务定时
     */
    public void uploadTask() {
        // 进行文件传输
        int uploadThreadcount = Integer.parseInt(PropertyUtil.p.getProperty("UploadThreadPoolSize"));
        ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1 + uploadThreadcount);
        UploadUtil ftpUploadUtil = new UploadUtil();
        threadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                fileDir = new File(PropertyUtil.p.getProperty("localResult"));
                filelist = fileDir.list();
                for (int i = 0; i < filelist.length; i++) {
                    uploadqueue.add(filelist[i]);
                }
            }
        }, 0, Integer.parseInt(PropertyUtil.p.getProperty("UploadResultTime")), TimeUnit.MINUTES);
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < filelist.length; i++) {
            threadPoolExecutor.scheduleAtFixedRate(ftpUploadUtil, 0,
                    Integer.parseInt(PropertyUtil.p.getProperty("UploadResultTime")), TimeUnit.MINUTES);
        }
    }

}
