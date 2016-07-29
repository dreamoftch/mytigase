package com.richmj.plugins;

import java.util.Map;
import java.util.Queue;
import java.util.logging.Logger;

import tigase.db.NonAuthUserRepository;
import tigase.db.TigaseDBException;
import tigase.server.Message;
import tigase.server.Packet;
import tigase.util.DNSResolver;
import tigase.xml.Element;
import tigase.xmpp.BareJID;
import tigase.xmpp.JID;
import tigase.xmpp.NotAuthorizedException;
import tigase.xmpp.XMPPException;
import tigase.xmpp.XMPPProcessorIfc;
import tigase.xmpp.XMPPResourceConnection;
import tigase.xmpp.impl.C2SDeliveryErrorProcessor;
import tigase.xmpp.impl.annotation.AnnotatedXMPPProcessor;
import tigase.xmpp.impl.annotation.Handle;
import tigase.xmpp.impl.annotation.Handles;
import tigase.xmpp.impl.annotation.Id;

/**
 * 脉佳网络 plugin
 * @author tianchaohui
 */
@Id(RichmjPlugin.ID)
@Handles({ @Handle(path = { Message.ELEM_NAME }, xmlns = "jabber:client")})
public class RichmjPlugin extends AnnotatedXMPPProcessor implements XMPPProcessorIfc {
	
	protected static final String ID = "richmj-plugin";
	
	/**
	 * 目标component的jid
	 */
	private JID componentJid = null;
	
	private static final Logger logger = Logger.getLogger(RichmjPlugin.class.getName());
	
	@Override
	public void init(Map<String, Object> settings) throws TigaseDBException {
		super.init(settings);
		String componentJidStr = (String) settings.get("component-jid");
		if (componentJidStr != null) {
			componentJid = JID.jidInstanceNS(componentJidStr);
		} else {
			String defHost = DNSResolver.getDefaultHostname();
			componentJid = JID.jidInstanceNS("richmj-component", defHost, null);
		}
	}

	public void process(Packet packet, XMPPResourceConnection session, NonAuthUserRepository repo,
			Queue<Packet> results, Map<String, Object> settings) throws XMPPException {
		//判断是否是需要处理的message
		if(needHandle(packet, session)){
			logger.info(ID + ", begin to deal with richmj data");
			//转发该消息到componnet
			results.offer(generateNewPacket(packet));
		}
	}

	/**
	 * 产生新的packet
	 * @param packet
	 */
	private Packet generateNewPacket(Packet packet) {
		Packet result = packet.copyElementOnly();
		result.setPacketTo(componentJid);
		//result.getElement().setAttribute("type", "richmj-message");
		return result;
	}

	/**
	 * 判断是否是需要处理的message
	 * @param packet
	 * @param session
	 * @return
	 * @throws NotAuthorizedException
	 */
	private boolean needHandle(Packet packet, XMPPResourceConnection session) throws NotAuthorizedException {
		if (session == null) {
			logger.info(ID + ", 当前用户离线");
			return false;
		}
		if (C2SDeliveryErrorProcessor.isDeliveryError(packet)){
			return false;
		}
		BareJID id = (packet.getStanzaFrom() != null) ? packet.getStanzaFrom().getBareJID() : null;
		if(!session.isUserId(id)){
			logger.info(ID + ", this message does not belong to current session");
			return false;
		}
		logger.info(ID + ", this message belongs to current session");
		if (Message.ELEM_NAME != packet.getElemName()) {
			return false;
		}
		Element bodyElement = packet.getElement().findChildStaticStr(Message.MESSAGE_BODY_PATH);
		logger.info(ID + ", bodyElement:" + bodyElement);
		if(bodyElement == null){
			return false;
		}
		String bodyType = bodyElement.getAttributeStaticStr("type");
		logger.info(ID + ", bodyType:" + bodyType);
		
		if(bodyType == null){
			return false;
		}
		return true;
	}

}
