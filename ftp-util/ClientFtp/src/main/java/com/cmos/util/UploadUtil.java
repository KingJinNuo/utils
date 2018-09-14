package com.cmos.util;

import java.io.*;
import java.net.SocketException;
import com.cmos.task.UploadTask;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

/**
 * 文件上传工具类
 */
public class UploadUtil implements Runnable {
    Logger log = Logger.getLogger(UploadUtil.class);
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
        //获得本地要上传的文件
        if(UploadTask.uploadqueue.isEmpty()){
            log.info(Thread.currentThread().getName() + "没有上传任务");
            return;
        }
        File file = new File(localResult +"/"+ UploadTask.uploadqueue.poll());
        if (!file.exists()) {
            log.info("------------文件不存在--------------");
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
            log.info(Thread.currentThread().getName()+ "开始上传文件:"+file.getName());
            Thread.currentThread();
            //将本文件传到ftp
            ftpClient.storeFile(new String(file.getName().getBytes("utf-8"), "iso-8859-1"), in);
            log.info(Thread.currentThread().getName()+file.getName()+"上传成功");
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
