package com.cmos.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
	private static Connection conn = null;
	private static PreparedStatement pstmt = null;
	private static ResultSet rs = null;
	
	/**
	 * 增删改操作
	 * @param sql
	 * @param paramsValue
	 * @return
	 */
	public static int execute(String sql, Object[] paramsValue) {
		int result = 0;
		try {
			// 1. 数据库连接
			conn = JDBCUtil.getConnection();
			// 2. 获取PreparedStatement
			pstmt = conn.prepareStatement(sql);
			// 3. 得到参数元数据个数
			int count = pstmt.getParameterMetaData().getParameterCount();
			// 4. 利用参数元数据给SQL语句的占位符需要的参数赋值
			if (paramsValue != null && paramsValue.length > 0) {
				for (int i = 0; i < count; i++) {
					// 循环结束，可以给SQL语句完整赋值
					pstmt.setObject(i + 1, paramsValue[i]);
				}
			}
			// 5. 执行
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			JDBCUtil.close(conn, pstmt);
		}
		return result;
	}

	/**
	 * 查询操作
	 * @param sql
	 * @param params
	 * @return
	 */
	public static List<Map<String, Object>> executeQuery(String sql, Object[] params) {
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			conn = JDBCUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			// 设置参数
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
				}
			}
			rs = pstmt.executeQuery();
			// 得到ResultSetMetaData对象，这个对象包括列的信息(列的名称和类型等等)
			ResultSetMetaData metaData = rs.getMetaData();
			// 得到结果集中列的个数
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					// 得到指定的列名的别名
					String name = metaData.getColumnLabel(i);
					Object value = rs.getObject(i);
					map.put(name, value);
				}
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn, pstmt, rs);
		}
		return list;
	}

}
