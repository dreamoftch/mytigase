package com.richmj.models;

public class RichmjMessage {

	private int id;
	/**
	 * 发消息的人
	 */
	private String fromId;
	/**
	 * 接收消息的人
	 */
	private String toId;
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
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
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
}
