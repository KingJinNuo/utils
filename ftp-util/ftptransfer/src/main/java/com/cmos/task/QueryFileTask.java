package com.cmos.task;

import com.cmos.util.QueryftpFiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 文件下载任务类
 * Author ：wx
 * Date ：Created in  2018/9/3 18:12
 */
public class QueryFileTask extends TimerTask {
    private List<String> ftpfiles = new ArrayList<>();
    public static Queue<String> blockingQueue  = new LinkedBlockingQueue<>();


    @Override
    public void run() {
        try {
            ftpfiles = new QueryftpFiles().queryFtpFiles();
            for (String file : ftpfiles) {
                if (!blockingQueue.contains(file)) {
                    blockingQueue.add(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
