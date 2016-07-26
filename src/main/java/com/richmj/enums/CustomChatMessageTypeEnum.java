package com.richmj.enums;

/**
 * 自定义的 message body type 枚举类
 * namecard customer corperation interview microsite
 * @author tianchaohui
 */
public enum CustomChatMessageTypeEnum {

	NAME_CARD(1, "namecard", "名片"),
	CUSTOMER(2, "customer", "推荐客户"),
	COOPERATION(3, "cooperation", "代理人合作"),
	INTERVIEW(4, "interview", "约见书"),
	MICROSITE(5, "microsite", "微站");
	
	private Integer code;
	private String value;
	private String desc;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	private CustomChatMessageTypeEnum(Integer code, String value, String desc) {
		this.code = code;
		this.value = value;
		this.desc = desc;
	}
	
	public static CustomChatMessageTypeEnum getByValue(String value){
		for(CustomChatMessageTypeEnum userTypeEnum : values()){
			if(userTypeEnum.getValue().equalsIgnoreCase(value)){
				return userTypeEnum;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String string = "";
		for(CustomChatMessageTypeEnum userTypeEnum : values()){
			string += (userTypeEnum.getCode() + ":" + userTypeEnum.getValue() + ":" + userTypeEnum.getDesc() + ", ");
		}
		System.out.println(string);
	}
	
}
