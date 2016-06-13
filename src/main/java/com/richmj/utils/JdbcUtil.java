package com.richmj.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.richmj.models.RichmjMessage;

/**
 * 数据库操作工具类
 * @author tianchaohui
 */
public final class JdbcUtil {
	
	private static final Logger logger = Logger.getLogger(JdbcUtil.class.getName());
	private static String jdbcUrl = null;

	/**
	 * 加载mysql数据库驱动
	 * @param jdbcUrl
	 */
	public static void init(String jdbcUrl){
		if(jdbcUrl == null){
			logger.log(Level.WARNING, "jdbcUrl为空");
		}
		try {
			JdbcUtil.jdbcUrl = jdbcUrl;
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "初始化mysql数据库驱动异常：", e);
		}
	}

	/**
	 * 获取数据库连接
	 * @return
	 */
	private static Connection getConnection() {
	    try {
	        return DriverManager.getConnection(jdbcUrl);
	    } catch (Exception e) {
	    	logger.log(Level.SEVERE, "获取数据库连接异常：", e);
	    }
	    return null;
	}
	
	/**
	 * 插入RichmjMessage
	 * @param message
	 */
	public static void insertMessage(RichmjMessage message){
		Connection conn = getConnection();
		PreparedStatement insertMessageStatement = null;
		if(conn == null){
			logger.log(Level.SEVERE, "数据库连接为空：");
			return;
		}
		try {
			insertMessageStatement = conn.prepareStatement("insert into richmj_message(fromId, toId, type, message) values(?, ?, ?, ?)");
			insertMessageStatement.setString(1, message.getFromId());
			insertMessageStatement.setString(2, message.getToId());
			insertMessageStatement.setString(3, message.getType());
			insertMessageStatement.setString(4, message.getMessage());
			insertMessageStatement.executeUpdate();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "执行插入RichmjMessage记录异常：", e);
		}finally {
			closeJdbcResource(insertMessageStatement, conn);
		}
		
	}
	
	/**
	 * 关闭释放资源
	 * @param preparedStatement
	 * @param conn
	 */
	private static void closeJdbcResource(PreparedStatement preparedStatement, Connection conn){
		if(preparedStatement != null){
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "关闭insertMessageStatement异常：", e);
			}
		}
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "关闭Connection异常：", e);
			}
		}
	}
	
}
