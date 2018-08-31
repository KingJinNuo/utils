package com.cmos.util;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
/**
 * 连接ftp服务器工具类
 * @author Wx
 *
 */
public class FtpUtil {
	public FTPClient ftpClient=null;
	//连接服务器方法
	public FTPClient linkFtpClient(String hostname,String username,String password,int port) {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            System.out.println("connecting...ftp服务器:"+hostname+":"+port); 
            ftpClient.connect(hostname, port); //连接ftp服务器
            ftpClient.login(username, password); //登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
            if(!FTPReply.isPositiveCompletion(replyCode)){
                System.out.println("connect failed...ftp服务器:"+hostname+":"+port); 
            }
            System.out.println("connect successful...ftp服务器:"+hostname+":"+port); 
        }catch (MalformedURLException e) { 
           e.printStackTrace(); 
        }catch (IOException e) { 
           e.printStackTrace(); 
        }
        return ftpClient;
    }
	
	
}
