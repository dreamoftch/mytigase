package com.richmj.components;



import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.richmj.dao.RichmjMessageDao;
import com.richmj.models.RichmjMessage;
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
			element.setAttribute("from", element.getAttributeStaticStr("richmjFrom"));//richmjFrom表示代理的用户
			element.setAttribute("to", element.getAttributeStaticStr("richmjTo"));//richmjTo表示实际希望抵达的目标地址
			richmjPacket.setPacketFrom(JID.jidInstance(element.getAttributeStaticStr("richmjFrom")));
			richmjPacket.setPacketTo(JID.jidInstance(element.getAttributeStaticStr("richmjTo")));
			element.removeAttribute("richmjFrom");
			element.removeAttribute("richmjTo");
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
			RichmjMessage message = new RichmjMessage();
			message.setFromId(getFromId(packet));
			message.setToId(getToId(packet));
			message.setType(packet.getElement().findChildStaticStr(Message.MESSAGE_BODY_PATH).getAttributeStaticStr("type"));
			message.setMessage(packet.getElement().toString());
			new RichmjMessageDao().saveMessage(message);
		} catch (Exception e) {
			logger.log(Level.WARNING, "保存 RichmjMessage 消息异常", e);
		}
	}
	
	private String getToId(Packet packet) {
		return packet.getStanzaTo() == null ? null : packet.getStanzaTo().getBareJID().toString();
	}

	private String getFromId(Packet packet) {
		return packet.getStanzaFrom() == null ? null : packet.getStanzaFrom().getBareJID().toString();
	}

	@Override
	public Map<String, Object> getDefaults(Map<String, Object> params) {
		String jdbcUrlPropertyKey = "--user-db-uri";
		String serverHostPropertyKey = "--virt-hosts";
		Map<String, Object> result = super.getDefaults(params);
		if(params.containsKey(jdbcUrlPropertyKey)){
			jdbcUrl = String.valueOf(params.get(jdbcUrlPropertyKey));
			logger.info("init jdbcUrl :" + jdbcUrl);
		}else{
			logger.log(Level.SEVERE, "jdbcUrl为空");
		}
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
	    super.setProperties(props);
	}
	
	@Override
	public void initializationCompleted() {
		super.initializationCompleted();
		JdbcUtil.init(jdbcUrl);
	}

	@Override
	public String getDiscoDescription() {
	  return "脉佳网络组件";
	}

}
