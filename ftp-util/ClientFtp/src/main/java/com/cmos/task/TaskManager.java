package com.cmos.task;

import java.io.IOException;
import java.util.Timer;
/**
 * Author ：wx
 * Date ：Created in  2018/9/3 19:08
 * 管理定时任务
 */
public class TaskManager {
    /**
     * 任务调度方法
     */
    public void TaskSchedule() throws IOException, InterruptedException {
  
        //下载任务调度 调度时间间隔应>单次下载总时间
        new DownloadTask().down();
        //定时上传转写结果   20分钟/次
        new UploadTask().uploadTask();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
       new TaskManager().TaskSchedule();
    }
}
