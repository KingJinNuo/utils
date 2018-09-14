package com.cmos.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询ftp服务器要下载的文件
 * 获取指定条数给下载线程
 * @author Wx
 */
public class QueryftpFiles {
    private String downPath;
    private String hostname;
    private String username;
    private String password;
    private Integer port;
    public QueryftpFiles() {
        hostname = PropertyUtil.p.getProperty("hostname");
        username = PropertyUtil.p.getProperty("username");
        password = PropertyUtil.p.getProperty("password");
        port = Integer.parseInt(PropertyUtil.p.getProperty("port"));
        downPath = PropertyUtil.p.getProperty("downPath");

    }
    Logger log = Logger.getLogger(QueryftpFiles.class);
    public FTPClient ftpClient = null;

    /**
     * 在每次执行下载任务之前查询ftp目标目录上要下载的文件
     * @return
     * @throws IOException
     */
    public  List<String> queryFtpFiles() throws IOException {
        ftpClient = new FTPClient();
        FTPFile[] ftpFiles = {};
        List<String> list = new ArrayList<>();
        ftpClient.setControlEncoding("utf-8");
        try {
            ftpClient.connect(hostname, port); //连接ftp服务器
            ftpClient.login(username, password); //登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
            // 设置为二进制格式下载
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            // 切换到下载目录
            ftpClient.changeWorkingDirectory(downPath);
            //获取最子目录中文件列表
            ftpFiles = ftpClient.listFiles();
            for(int i = 0; i<Integer.parseInt(PropertyUtil.p.getProperty("FilesEveryDownloadTask"))&&i<ftpFiles.length; i++){
                list.add(ftpFiles[i].getName());
            }
            log.info(this.getClass().getName()+"----------------获取下载目录成功----------------");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ftpClient.disconnect();
        }
        return list;
    }
}
