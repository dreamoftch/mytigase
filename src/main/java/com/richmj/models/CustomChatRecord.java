package com.richmj.models;

public class CustomChatRecord {

	private int id;
	/**
	 * 发消息的人
	 */
	private Long fromId;
	/**
	 * 接收消息的人
	 */
	private Long toId;
	/**
	 * fromId/toId中的较大者
	 */
	private Long bigId;
	/**
	 * fromId/toId中的较小者
	 */
	private Long smallId;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 消息内容
	 */
	private String message;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Long getFromId() {
		return fromId;
	}
	public void setFromId(Long fromId) {
		this.fromId = fromId;
	}
	public Long getToId() {
		return toId;
	}
	public void setToId(Long toId) {
		this.toId = toId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getBigId() {
		return bigId;
	}
	public void setBigId(Long bigId) {
		this.bigId = bigId;
	}
	public Long getSmallId() {
		return smallId;
	}
	public void setSmallId(Long smallId) {
		this.smallId = smallId;
	}
}
