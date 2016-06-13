package com.richmj.enums;

/**
 * 自定义的 message body type 枚举类
 * @author tianchaohui
 */
public enum RichmjMessageBodyEnum {

	TEXT("text", "文本"),
	IMAGE("image", "图片"),
	MICROSITE("microsite", "微站"),
	INTERVIEW("interview", "约见书"),
	CUSTOM_PUSH("custom-push", "客户推送"),
	PLAN_BOOK("plan_book", "计划书"),
	VCARD("vcard", "名片"),
	PROJECT("project", "协议书");
	
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
	
	private RichmjMessageBodyEnum(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	public static RichmjMessageBodyEnum getByValue(String value){
		for(RichmjMessageBodyEnum userTypeEnum : values()){
			if(userTypeEnum.getValue().equals(value)){
				return userTypeEnum;
			}
		}
		return null;
	}
	
}
