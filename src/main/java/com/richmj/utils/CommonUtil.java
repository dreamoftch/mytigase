package com.richmj.utils;

import com.richmj.constants.Constant;

import tigase.server.Packet;

public final class CommonUtil {

	/**
	 * 判断packet是不是richmj发送的
	 * @param packet
	 * @return
	 */
	public static boolean isPacketFromRichMJ(Packet packet) {
		String from = packet.getStanzaFrom().getBareJID().toString();
		String expectFrom = Constant.SYSTEM_ACCOUNT + "@" + Constant.SERVER_HOST;
		if(from != null && expectFrom.toLowerCase().equals(from.toLowerCase())){
			return true;
		}
		return false;
	}
	
}
