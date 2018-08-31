package com.cmos.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * 文件上传工具类
 * 
 * @author Wx
 *
 */
public class FtpUploadUtil extends Thread {
	// ftp服务器地址
	private String hostname;
	private String username;
	private String password;
	private Integer port;
	// 文件上传位置
	private String pathname;
	// 上传后的文件名
	private String fileName;
	private InputStream inputStream;

	/**
	 * 文件上传构造方法
	 * 
	 * @param hostname
	 * @param username
	 * @param password
	 * @param port
	 * @param pathname
	 * @param fileName
	 * @param inputStream
	 */
	public FtpUploadUtil(String hostname, String username, String password, Integer port, String pathname,
			String fileName, InputStream inputStream) {
		super();
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		this.port = port;
		this.pathname = pathname;
		this.fileName = fileName;
		this.inputStream = inputStream;
	}

	// 创建初始化工具类
	FtpUtil ftpUtil = new FtpUtil();
	FTPClient ftpClient = new FTPClient();

	/**
	 * 
	 * @param hostname
	 *            ftp服务器地址
	 * @param username
	 *            用户名
	 * @param password
	 *            用户密码
	 * @param port
	 *            端口号
	 * @param pathname
	 *            服务保存地址
	 * @param fileName
	 *            上传到ftp的文件名
	 * @param inputStream
	 *            输入文件流
	 * @return
	 */
	public synchronized boolean uploadFile(String hostname, String username, String password, Integer port,
			String pathname, String fileName, InputStream inputStream) {
		boolean flag = false;
		ftpClient = ftpUtil.linkFtpClient(hostname, username, password, 21);
		try {
			System.out.println("开始上传文件...");
			// 设置为二进制格式上传
			ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
			// 判断文件是否存在，若不存在则新建
			CreateDirecroty(pathname);
			// 客户端指定上传位置
			ftpClient.makeDirectory(pathname);
			// 跳转到指定文件夹
			ftpClient.changeWorkingDirectory(pathname);
			// 进行文件传输
			ftpClient.storeFile(fileName, inputStream);
			inputStream.close();
			ftpClient.logout();
			flag = true;
			System.out.println("上传文件成功");
		} catch (Exception e) {
			System.out.println("上传文件失败");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 改变文件路径
	 * 
	 * @param directory
	 * @return
	 */
	public boolean changeWorkingDirectory(String directory) {
		boolean flag = true;
		try {
			flag = ftpClient.changeWorkingDirectory(directory);
			if (flag) {
				System.out.println("进入文件夹" + directory + " 成功！");

			} else {
				System.out.println("进入文件夹" + directory + " 失败！开始创建文件夹");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return flag;
	}

	/**
	 * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
	 * 
	 * @param remote
	 * @return
	 * @throws IOException
	 */
	public boolean CreateDirecroty(String remote) throws IOException {
		boolean success = true;
		String directory = remote + "/";
		// 如果远程目录不存在，则递归创建远程服务器目录
		if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
			//第一个 / 的位置
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			String path = "";
			String paths = "";
			while (true) {
				String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
				path = path + "/" + subDirectory;
				if (!existFile(path)) {
					if (makeDirectory(subDirectory)) {
						changeWorkingDirectory(subDirectory);
					} else {
						System.out.println("创建目录[" + subDirectory + "]失败");
						changeWorkingDirectory(subDirectory);
					}
				} else {
					changeWorkingDirectory(subDirectory);
				}

				paths = paths + "/" + subDirectory;
				start = end + 1;
				end = directory.indexOf("/", start);
				// 检查所有目录是否创建完毕，创建到最后一个目录查询不到 / 返回end = -1
				if (end <= start) {
					break;
				}
			}
		}
		return success;
	}

	/**
	 * 判断ftp服务器文件是否存在
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public boolean existFile(String path) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		if (ftpFileArr.length > 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 创建目录
	 * 
	 * @param dir
	 * @return
	 */
	public boolean makeDirectory(String dir) {
		boolean flag = true;
		try {
			flag = ftpClient.makeDirectory(dir);
			if (flag) {
				System.out.println("创建文件夹" + dir + " 成功！");

			} else {
				System.out.println("创建文件夹" + dir + " 失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public void run() {

		this.uploadFile(hostname, username, password, port, pathname, fileName, inputStream);

	}

}
