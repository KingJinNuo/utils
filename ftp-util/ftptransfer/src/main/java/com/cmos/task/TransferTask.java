package com.cmos.task;
import com.cmos.util.PropertyUtil;
import com.cmos.util.TransferUtil;

import java.util.Timer;

/**
 * 转写调度
 * Author ：wx
 * Date ：Created in  2018/9/6 15:43
 */
public class TransferTask {
    //创建转写任务
    public void transferTask() {
        TransferUtil transferUtil = new TransferUtil();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(transferUtil,0,Integer.parseInt(PropertyUtil.p.getProperty("TransferScheduleTime")));
    }

}

