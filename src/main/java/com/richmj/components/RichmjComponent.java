package com.richmj.components;



import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.richmj.constants.Constant;
import com.richmj.dao.RichmjMessageDao;
import com.richmj.models.CustomChatRecord;
import com.richmj.utils.JdbcUtil;

import tigase.conf.ConfigurationException;
import tigase.server.AbstractMessageReceiver;
import tigase.server.Message;
import tigase.server.Packet;
import tigase.util.TigaseStringprepException;
import tigase.xml.Element;
import tigase.xmpp.JID;

public class RichmjComponent extends AbstractMessageReceiver{

	private static final Logger logger = Logger.getLogger(RichmjComponent.class.getName());
	
	private String jdbcUrl = null;
	private static String jdbcUsername = null;
	private static String jdbcPassword = null;
	private String serverHost = null;
	
	@Override
	public void processPacket(Packet packet) {
		logger.info("自定义Component RichmjComponent收到 packet ---->：" + packet);
		if(isPacketFromRichMJ(packet)){
			handleRichMJPacket(packet);
			return;
		}
		saveMessage(packet);
	}

	private void handleRichMJPacket(Packet packet) {
		try {
			logger.info("这个packet是来自richMJ的，开始handleRichMJPacket");
			//创建一个新的packet，然后将这个packet的from/to设置为指定的用户
			Packet richmjPacket = packet.copyElementOnly();
			Element element = richmjPacket.getElement();
			//将这个packet的from/to,packetFrom/packetTo设置为指定的用户
			String desireFrom = element.getAttributeStaticStr(Constant.RICHMJ_STANZA_FROM);
			String desireTo = element.getAttributeStaticStr(Constant.RICHMJ_STANZA_TO);
			element.setAttribute("from", desireFrom);//richmjFrom表示代理的用户
			element.setAttribute("to", desireTo);//richmjTo表示实际希望抵达的目标地址
			richmjPacket.setPacketFrom(JID.jidInstance(desireFrom));
			richmjPacket.setPacketTo(JID.jidInstance(desireTo));
			element.removeAttribute(Constant.RICHMJ_STANZA_FROM);
			element.removeAttribute(Constant.RICHMJ_STANZA_TO);
			logger.info("RichmjComponent处理过之后的packet=====================>:" + richmjPacket);
			addOutPacket(richmjPacket);
		} catch (TigaseStringprepException e) {
			logger.log(Level.WARNING, "RichmjComponent handleRichMJPacket 异常:", e);
			e.printStackTrace();
		}
	}

	/**
	 * 判断packet是不是richmj发送的
	 * @param packet
	 * @return
	 */
	private boolean isPacketFromRichMJ(Packet packet) {
		String from = packet.getStanzaFrom().getBareJID().toString();
		String expectFrom = "richmj@" + serverHost;
		logger.info("from:" + from + ", expectFrom:" + expectFrom);
		if(from != null && expectFrom.toLowerCase().equals(from.toLowerCase())){
			return true;
		}
		return false;
	}

	/**
	 * 保存该消息
	 * @param packet
	 */
	private void saveMessage(Packet packet) {
		try {
			CustomChatRecord message = new CustomChatRecord();
			message.setFromId(getId(packet.getStanzaFrom()));
			message.setToId(getId(packet.getStanzaTo()));
			message.setType(packet.getElement().findChildStaticStr(Message.MESSAGE_BODY_PATH).getAttributeStaticStr("type"));
			message.setMessage(packet.getElement().toString());
			message.setBigId(getBigId(message));
			message.setSmallId(getSmallId(message));
			new RichmjMessageDao().saveMessage(message);
		} catch (Exception e) {
			logger.log(Level.WARNING, "保存 RichmjMessage 消息异常", e);
		}
	}
	
	/**
	 * 获取fromId/toId中的较大者
	 * @param message
	 * @return
	 */
	private Long getBigId(CustomChatRecord message){
		Long fromId = message.getFromId();
		Long toId = message.getToId();
		if(fromId == null){
			return toId == null ? 0 : toId;
		}
		if(toId == null){
			return fromId;
		}
		return Math.max(fromId, toId);
	}
	/**
	 * 获取fromId/toId中的较小者
	 * @param message
	 * @return
	 */
	private Long getSmallId(CustomChatRecord message){
		Long fromId = message.getFromId();
		Long toId = message.getToId();
		if(fromId == null){
			return toId == null ? 0 : toId;
		}
		if(toId == null){
			return fromId;
		}
		return Math.min(fromId, toId);
	}

	private Long getId(JID jid) {
		if(jid == null){
			return 0l;
		}
		String bareJID = jid.getBareJID().toString();
		if(bareJID == null){
			return 0l;
		}
		String id = bareJID.substring(0, bareJID.indexOf("@"));
		if(id.matches("[0-9]+")){
			return Long.valueOf(id);
		}
		return 0l;
	}

	@Override
	public Map<String, Object> getDefaults(Map<String, Object> params) {
		String serverHostPropertyKey = "--virt-hosts";
		Map<String, Object> result = super.getDefaults(params);
		if(params.containsKey(serverHostPropertyKey)){
			serverHost = String.valueOf(params.get(serverHostPropertyKey));
			logger.info("init serverHost :" + serverHost);
		}else{
			logger.log(Level.SEVERE, "serverHost为空");
		}
		return result;
	}

	@Override
	public void setProperties(Map<String, Object> props) throws ConfigurationException {
		String jdbcUrlPropertyKey = "business-db-uri";
		String jdbcUsernamePropertyKey = "business-db-username";
		String jdbcPasswordPropertyKey = "business-db-password";
		if(props.containsKey(jdbcUrlPropertyKey)){
			jdbcUrl = String.valueOf(props.get(jdbcUrlPropertyKey));
			logger.info("init jdbcUrl :" + jdbcUrl);
		}else{
			logger.log(Level.SEVERE, "jdbcUrl为空");
		}
		if(props.containsKey(jdbcUsernamePropertyKey)){
			jdbcUsername = String.valueOf(props.get(jdbcUsernamePropertyKey));
			logger.info("init jdbcUsername :" + jdbcUsername);
		}else{
			logger.log(Level.SEVERE, "jdbcUsername为空");
		}
		if(props.containsKey(jdbcPasswordPropertyKey)){
			jdbcPassword = String.valueOf(props.get(jdbcPasswordPropertyKey));
			logger.info("init jdbcPassword :" + jdbcPassword);
		}else{
			logger.log(Level.SEVERE, "jdbcPassword为空");
		}
	    super.setProperties(props);
	}
	
	@Override
	public void initializationCompleted() {
		super.initializationCompleted();
		JdbcUtil.init(jdbcUrl, jdbcUsername, jdbcPassword);
	}

	@Override
	public String getDiscoDescription() {
	  return "脉佳网络组件";
	}

}
