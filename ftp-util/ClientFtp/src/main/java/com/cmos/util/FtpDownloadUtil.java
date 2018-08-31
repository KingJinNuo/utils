package com.cmos.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * 下载文件工具类
 * 
 * @author Wx
 *
 */
public class FtpDownloadUtil extends Thread {
	private String hostname;
	private String username;
	private String password;
	private Integer port;
	// 文件所在的目录
	private String pathname;
	// 所下载的文件名
	private String filename;
	// 下载后本机存放位置
	private String localpath;

	public FtpDownloadUtil(String hostname, String username, String password, Integer port, String pathname,
			String filename, String localpath) {
		super();
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.port = port;
		this.pathname = pathname;
		this.filename = filename;
		this.localpath = localpath;
	}

	FtpUtil ftpUtil = new FtpUtil();
	FTPClient ftpClient = new FTPClient();

	/**
	 * * 下载文件 *
	 * 
	 * @param pathname
	 *            FTP服务器文件目录 *
	 * @param filename
	 *            文件名称 *
	 * @param localpath
	 *            下载后的文件路径 *
	 * @return
	 */
	public synchronized boolean downloadFile(String hostname, String username, String password, Integer port,
			String pathname, String filename, String localpath) {
		boolean flag = false;
		OutputStream os = null;
		// 创建客户端连接
		ftpClient = ftpUtil.linkFtpClient(hostname, username, password, 21);
		try {
			System.out.println("开始下载文件");
			FtpUtil ftpUtil = new FtpUtil();
			// 切换到目录
			ftpClient.changeWorkingDirectory(pathname);
			//获取最子目录中文件列表
			FTPFile[] ftpFiles = ftpClient.listFiles();
			for (FTPFile file : ftpFiles) {
				if (filename.equalsIgnoreCase(file.getName())) {
					File localFile = new File(localpath + "/" + file.getName());
					os = new FileOutputStream(localFile);
					ftpClient.retrieveFile(file.getName(), os);
					os.close();
				}
			}
			ftpClient.logout();
			flag = true;
			System.out.println("下载文件成功");
		} catch (Exception e) {
			System.out.println("下载文件失败");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	@Override
	public void run() {
		this.downloadFile(hostname, username, password, port, pathname, filename, localpath);
	}

	/**
	 * * 删除文件 *
	 * 
	 * @param pathname
	 *            FTP服务器保存目录 *
	 * @param filename
	 *            要删除的文件名称 *
	 * @return
	 */
	public boolean deleteFile(String hostname, String username, String password, Integer port, String pathname,
			String filename) {
		boolean flag = false;
		ftpClient = ftpUtil.linkFtpClient(hostname, username, password, 21);
		try {
			System.out.println("开始删除文件");
			FtpUtil ftpUtil = new FtpUtil();
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.dele(filename);
			ftpClient.logout();
			flag = true;
			System.out.println("删除文件成功");
		} catch (Exception e) {
			System.out.println("删除文件失败");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

}
