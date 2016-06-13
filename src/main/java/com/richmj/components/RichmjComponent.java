package com.richmj.components;


import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.richmj.dao.RichmjMessageDao;
import com.richmj.models.RichmjMessage;

import tigase.conf.ConfigurationException;
import tigase.server.AbstractMessageReceiver;
import tigase.server.Message;
import tigase.server.Packet;

public class RichmjComponent extends AbstractMessageReceiver{

	private static final Logger logger = Logger.getLogger(RichmjComponent.class.getName());
	
	@Override
	public void processPacket(Packet packet) {
		logger.info("RichmjComponent 接收到packet ---->：" + packet);
		saveMessage(packet);
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
	    return super.getDefaults(params);
	}

	@Override
	public void setProperties(Map<String, Object> props) throws ConfigurationException {
	    super.setProperties(props);
	}
	
	@Override
	public String getDiscoDescription() {
	  return "脉佳网络component";
	}

}
