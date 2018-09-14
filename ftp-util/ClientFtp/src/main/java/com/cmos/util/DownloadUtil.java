package com.cmos.util;
import com.cmos.task.QueryFileTask;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Author ：wx
 * Date ：Created in  2018/9/7 11:26
 */
public class DownloadUtil implements Runnable {
    private String hostname;
    private String username;
    private String password;
    private Integer port;
    private String downPath;
    private String localPath;
    private String backPath;

    public DownloadUtil() {
        hostname = PropertyUtil.p.getProperty("hostname");
        username = PropertyUtil.p.getProperty("username");
        password = PropertyUtil.p.getProperty("password");
        port = Integer.parseInt(PropertyUtil.p.getProperty("port"));
        downPath = PropertyUtil.p.getProperty("downPath");
        localPath = PropertyUtil.p.getProperty("localPath");
        backPath = PropertyUtil.p.getProperty("backPath");
    }

    Logger log = Logger.getLogger(DownloadUtil.class);

    @Override
    public void run() {
        if (QueryFileTask.blockingQueue.isEmpty()) {
            log.info(Thread.currentThread().getName() + "没有下载任务");
            return;
        }
        String filename = QueryFileTask.blockingQueue.poll();

        File localFile = new File(localPath + "/" + filename);

        FileOutputStream os = null;
        FileInputStream in = null;
        FTPClient ftpClient = null;
        try {
            ftpClient = getFTPClient();
            os = new FileOutputStream(localFile);
            //下载文件
            log.info(Thread.currentThread().getName() + "正在下载文件" + filename);
            boolean flag = ftpClient.retrieveFile(filename, os);
            //删除文件
            if (flag) {
                ftpClient.deleteFile(filename);
                log.info(Thread.currentThread().getName() + "下载完成，删除源文件" + filename);
                os.close();
                //远程备份 将Transfer目录内已经下载文件转写到Tranferred目录
                in = new FileInputStream(localFile);
                ftpClient.changeWorkingDirectory(backPath);
                boolean flag1 = ftpClient.storeFile(new String(filename.getBytes("utf-8"), "iso-8859-1"), in);
                ftpClient.changeWorkingDirectory(downPath);
                if (flag1) {
                    log.info(Thread.currentThread().getName() + "源文件备份成功！" + filename);
                }
                in.close();
                log.info(Thread.currentThread() + "下载成功！" + filename);
            }
           // ftpClient.logout();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {


        }


    }

    /**
     * 获取ftp连接
     * @return
     * @throws Exception
     */
    public FTPClient getFTPClient() throws Exception {
        FTPClient ftpClient = new FTPClient();
        boolean flag = false;
        int reply;
        ftpClient.connect(hostname, port);
        ftpClient.login(username, password);
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            return ftpClient;
        }
        ftpClient.changeWorkingDirectory(downPath);
        return ftpClient;
    }


}
