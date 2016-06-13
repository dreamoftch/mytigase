package com.tch.test.tigase.mytigase;

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
import tigase.xmpp.StanzaType;
import tigase.xmpp.XMPPException;
import tigase.xmpp.XMPPProcessorIfc;
import tigase.xmpp.XMPPResourceConnection;
import tigase.xmpp.impl.C2SDeliveryErrorProcessor;
import tigase.xmpp.impl.annotation.AnnotatedXMPPProcessor;
import tigase.xmpp.impl.annotation.Handle;
import tigase.xmpp.impl.annotation.Handles;
import tigase.xmpp.impl.annotation.Id;

@Id(MyMessageArchivePlugin.ID)
@Handles({ @Handle(path = { Message.ELEM_NAME }, xmlns = "jabber:client")})
public class MyMessageArchivePlugin extends AnnotatedXMPPProcessor implements XMPPProcessorIfc {

	protected static final String ID = "MyMessageArchivePlugin";
	
	/**
	 * 参考MessageArchivePlugin的实现方式
	 */
	
	private JID jid = null;
	private static final Logger logger = Logger.getLogger(MyMessageArchivePlugin.class.getName());
	
	@Override
	public void init(Map<String, Object> settings) throws TigaseDBException {
		super.init(settings);
		String componentJidStr = (String) settings.get("component-jid");
		if (componentJidStr != null) {
			jid = JID.jidInstanceNS(componentJidStr);
		} else {
			String defHost = DNSResolver.getDefaultHostname();
			jid = JID.jidInstanceNS("mycomponent", defHost, null);
		}
	}

	public void process(Packet packet, XMPPResourceConnection session, NonAuthUserRepository repo,
			Queue<Packet> results, Map<String, Object> settings) throws XMPPException {
		logger.info("我自己的plugin执行啦 packet:" + packet);
		if (session == null) {
			return;
		}
		if (Message.ELEM_NAME == packet.getElemName()) {
			BareJID id = (packet.getStanzaFrom() != null)
					? packet.getStanzaFrom().getBareJID()
							: null;
			if(!session.isUserId(id)){
				logger.info("this message is not belong to current session, skip");
				return;
			}
			if (C2SDeliveryErrorProcessor.isDeliveryError(packet))
				return;
			StanzaType type = packet.getType();
			if ((packet.getElement().findChildStaticStr(Message.MESSAGE_BODY_PATH) ==
					null) || ((type != null) && (type != StanzaType.chat))) {
				logger.info("return directly ... here...");
				return;
			}
			if (packet.getElemCDataStaticStr(Message.MESSAGE_BODY_PATH) != null) {
				// redirecting to message archiving component
				Packet result = packet.copyElementOnly();
				result.setPacketTo(jid);
				Element message = result.getElement();
				message.setAttribute("type", "custom-chat");
				results.offer(result);
				logger.info("send real message... result:" + result+", origin packet:" + packet);
			}
		}

	}

}
