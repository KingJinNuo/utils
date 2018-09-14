package com.cmos.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 文件上传工具类
 */
public class UploadUtil extends TimerTask {
    Logger log = Logger.getLogger(UploadUtil.class);
    static Queue<String> uploadqueue = new LinkedBlockingQueue<>();
    private String hostname;
    private String username;
    private String password;
    private Integer port;
    private String localResult;
    private String resultPath;

    public UploadUtil() {
        hostname = PropertyUtil.p.getProperty("hostname");
        username = PropertyUtil.p.getProperty("username");
        password = PropertyUtil.p.getProperty("password");
        port = Integer.parseInt(PropertyUtil.p.getProperty("port"));
        localResult = PropertyUtil.p.getProperty("localResult");
        resultPath = PropertyUtil.p.getProperty("resultPath");
    }

    @Override
    public void run() {
        File fileDir;
        String[] filelist;
        //获得本地要上传的文件
        if(uploadqueue.isEmpty()) {
            fileDir = new File(PropertyUtil.p.getProperty("localResult"));
            filelist = fileDir.list();
            if (filelist.length==0) {
                log.info(Thread.currentThread().getName() + "没有上传任务");
                return;
            }
            for (int i = 0; i < filelist.length; i++) {
                try {
                    ((LinkedBlockingQueue<String>) uploadqueue).put(filelist[i]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        File file = new File(localResult + "/" + uploadqueue.poll());
        if (!file.exists()) {
            return;
        }
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(hostname, port);
            ftpClient.login(username, password);
            // 设置为二进制格式上传
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            // 跳转到指定文件夹
            ftpClient.changeWorkingDirectory(resultPath);
            InputStream in;
            in = new FileInputStream(file);
            log.info(Thread.currentThread().getName() + "开始上传文件:" + file.getName());
            //将本文件传到ftp
            ftpClient.storeFile(new String(file.getName().getBytes("utf-8"), "iso-8859-1"), in);
            log.info(Thread.currentThread().getName() + file.getName() + "上传成功");
            in.close();
            //删除文件
            file.delete();
            ftpClient.logout();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
