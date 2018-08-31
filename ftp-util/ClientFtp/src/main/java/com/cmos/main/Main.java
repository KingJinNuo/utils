package com.cmos.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cmos.util.FtpDownloadUtil;
import com.cmos.util.FtpUploadUtil;

public class Main {
	public static void main(String[] args) {

		// 创建文件流
		FileInputStream in;
		try {
			in = new FileInputStream(new File("C:\\Users\\Wx\\Desktop\\TestFtp\\1.txt"));
			FtpUploadUtil ftpUploadUtil = new FtpUploadUtil("192.168.133.142", "today", "123456", 21,
					"/home/today/upload", "11.txt", in);
			// ftpUploadUtil.start();
			
			/**
			 * 文件下载
			 * Hadoop-权威指南.pdf
			 */
			
			FtpDownloadUtil ftpDownloadUtil = new FtpDownloadUtil("192.168.133.142", "today", "123456", 21,
					"/home/today/upload", "1.jpg", "H:\\");
			ftpDownloadUtil.start();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
