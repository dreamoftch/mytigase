package com.richmj.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.richmj.models.CustomChatRecord;

/**
 * 数据库操作工具类
 * @author tianchaohui
 */
public final class JdbcUtil {
	
	private static final Logger logger = Logger.getLogger(JdbcUtil.class.getName());
	private static String jdbcUrl = null;
	private static String jdbcUsername = null;
	private static String jdbcPassword = null;
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "初始化mysql数据库驱动异常：", e);
		}
	}

	/**
	 * 加载mysql数据库驱动
	 * @param jdbcUrl
	 */
	public static void init(String jdbcUrl, String jdbcUsername, String jdbcPassword){
		if(jdbcUrl == null){
			logger.log(Level.WARNING, "jdbcUrl为空");
		}
		JdbcUtil.jdbcUrl = jdbcUrl;
		JdbcUtil.jdbcUsername = jdbcUsername;
		JdbcUtil.jdbcPassword = jdbcPassword;
	}

	/**
	 * 获取数据库连接
	 * @return
	 */
	private static Connection getConnection() {
	    try {
	        return DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
	    } catch (Exception e) {
	    	logger.log(Level.SEVERE, String.format("获取数据库连接异常, jdbcUrl:%s, jdbcUsername:%s, jdbcPassword:%s ：", jdbcUrl, jdbcUsername, jdbcPassword), e);
	    }
	    return null;
	}
	
	/**
	 * 插入RichmjMessage
	 * @param message
	 */
	public static void insertMessage(CustomChatRecord message){
		Connection conn = getConnection();
		PreparedStatement insertMessageStatement = null;
		if(conn == null){
			logger.log(Level.SEVERE, "数据库连接为空：");
			return;
		}
		try {
			insertMessageStatement = conn.prepareStatement("insert into custom_chat_record(from_id, to_id, type, message, big_id, small_id) values(?, ?, ?, ?, ?, ?)");
			insertMessageStatement.setLong(1, message.getFromId());
			insertMessageStatement.setLong(2, message.getToId());
			insertMessageStatement.setString(3, message.getType());
			insertMessageStatement.setString(4, message.getMessage());
			insertMessageStatement.setLong(5, message.getBigId());
			insertMessageStatement.setLong(6, message.getSmallId());
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
