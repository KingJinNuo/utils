package com.cmos.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接工具类
 * 
 * @author Wx
 *
 */
public class JDBCUtil {
	private static Properties ps = new Properties();
	static {
		// 读入配置文件
		InputStream is = JDBCUtil.class.getResourceAsStream("jdbc.properties");
		try {
			ps.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 建立连接方法
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		Connection conn = null;
		if (conn == null) {
			//加载驱动
			Class.forName(ps.getProperty("driver"));
			conn = DriverManager.getConnection(ps.getProperty("url"), ps.getProperty("username"),
					ps.getProperty("password"));
			return conn;
		} else {
			return conn;
		}
	}

	/**
	 * 关闭连接
	 * @param conn
	 * @param pre
	 * @param rs
	 */
	public static void close(Connection conn, PreparedStatement pre, ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		if (pre != null)
			try {
				pre.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	/**
	 * 关闭连接方法
	 * @param conn
	 * @param pre
	 */
	public static void close(Connection conn, PreparedStatement pre) {
		if (pre != null)
			try {
				pre.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
