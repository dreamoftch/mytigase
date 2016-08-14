package com.richmj.models;

/**
 * 群聊中发送的自定义消息
 * @author TianChaohui
 */
public class CustomGroupChatRecord {

	private int id;
	
	private String uuid;
	/**
	 * 发消息的人
	 */
	private Long fromId;
	/**
	 * 群id
	 */
	private Long groupId;
	/**
	 * 类型
	 */
	private Integer type;
	/**
	 * 名字，用于搜索
	 */
	private String name;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "CustomGroupChatRecord [id=" + id + ", uuid=" + uuid + ", fromId=" + fromId + ", groupId=" + groupId
				+ ", type=" + type + ", name=" + name + ", message=" + message + "]";
	}
	
}
