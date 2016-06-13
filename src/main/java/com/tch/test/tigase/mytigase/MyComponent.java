package com.tch.test.tigase.mytigase;


import java.util.Map;
import java.util.logging.Logger;

import tigase.conf.ConfigurationException;
import tigase.server.AbstractMessageReceiver;
import tigase.server.Packet;

public class MyComponent extends AbstractMessageReceiver{

	private static final Logger logger = Logger.getLogger(MyComponent.class.getName());
	
	@Override
	public void processPacket(Packet packet) {
		//send message to user1 when receive packet
		logger.info("MyComponent processPacket----> 接收到packet：" + packet);
	}

	@Override
	public Map<String, Object> getDefaults(Map<String, Object> params) {
		logger.info("MyComponent getDefaults");
	    Map<String, Object> defs = super.getDefaults(params);
	    logger.info("MyComponent getDefaults super.getDefaults(params): " + defs);
	    return defs;
	}

	@Override
	public void setProperties(Map<String, Object> props) throws ConfigurationException {
		logger.info("MyComponent getDefaults");
	    super.setProperties(props);
	    logger.info("MyComponent getDefaults props: " + props);
	}
	
	@Override
	public String getDiscoDescription() {
	  return "MyComponent for test";
	}

	@Override
	public String getDiscoCategoryType() {
	  return "spam";
	}

}
