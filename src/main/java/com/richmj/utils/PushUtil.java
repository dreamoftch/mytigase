package com.richmj.utils;


import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.richmj.constants.Constant;

import cn.jiguang.commom.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import tigase.server.Message;
import tigase.server.Packet;
import tigase.xml.Element;
import tigase.xmpp.JID;

/**
 * 推送工具类
 * @author TianChaohui
 */
public class PushUtil {
	
	private static final Logger logger = Logger.getLogger(PushUtil.class.getName());

    private static ClientConfig clientConfig = ClientConfig.getInstance();
    private static JPushClient jpushClient = new JPushClient(Constant.JIGUANG_MASTER_SECRET, Constant.JIGUANG_APP_KEY, null, clientConfig);

	public static void main(String[] args) {
		//Map<String, String> extras = new HashMap<>();
		//extras.put("key1", "value1");
		//extras.put("key2", "value2");
		//checkPushResult(push(buildPushObject_all_alias_alert("脉佳网络-title", "脉佳网络-alert", extras, "abcd")));
		//checkPushResult(push(buildPushObject_all_tag_alert("脉佳网络-title", "脉佳网络-alert", extras, "aaaa")));
		//checkPushResult(push(buildPushObject_all_all("title-1", "alert-1", extras)));
		//push(buildPushObject_all_all("title-2", "alert-2", extras));
		//push(buildPushObject_all_all("title-3", "alert-3", extras));
	}
	
	/**
	 * 根据packet推送消息
	 * @param packet
	 */
	public static void pushMessage(Packet packet){
		try {
			if(packet == null){
				logger.info("packet 为空, " + packet );
				return;
			}
			Element element = packet.getElement();
			if(element == null){
				logger.info("packet 中的element 为空, " + packet );
				return;
			}
			JID from = packet.getStanzaFrom();
			JID to = packet.getStanzaTo();
			if(from == null || to == null){
				logger.info("packet from或to 为空, " + packet );
				return;
			}
			Long fromUserId = getUserIdByTigaseUserId(from.getBareJID().toString());
			if(fromUserId == null){
				logger.info("从Element中解析出来的用户id为空, 尝试从packet直接提取用户id, " + packet);
				fromUserId = getUserIdByTigaseUserId(packet.getPacketFrom().getBareJID().toString());
			}
			Long toUserId = getUserIdByTigaseUserId(to.getBareJID().toString());
			if(fromUserId == null || toUserId == null){
				logger.info("从packet中解析出来的用户id为空, " + packet);
				return;
			}
			Element bodyElement = packet.getElement().findChildStaticStr(Message.MESSAGE_BODY_PATH);
			String message = null;
			if(bodyElement == null || (message = bodyElement.getCData()) == null){
				logger.info("从packet中解析出来的body内容为空, " + packet);
				return;
			}
			logger.info("开始推送解析出来的用户消息：" + message);
			push(buildPushObject_all_alias_alert(message, message, null, String.valueOf(toUserId)));
		} catch (Exception e) {
			error("发送推送消息异常, packet:" + packet, e);
		}
	}
	
	/**
	 * 根据tigase中的userId解析出后台系统的用户id
	 * @param tigaseUserId
	 * @return
	 */
	private static Long getUserIdByTigaseUserId(String tigaseUserId){
		int index = tigaseUserId.indexOf("@");
		if(index == -1){
			return null;
		}
		String userIdStr = tigaseUserId.substring(0, tigaseUserId.indexOf("@"));
		if(userIdStr.matches("\\d+")){
			return Long.parseLong(userIdStr);
		}
		return null;
	}

	private static void error(String msg, Throwable e){
		logger.log(Level.SEVERE, msg, e);
	}
	
	/**
	 * 发送push请求
	 */
	public static PushResult push(PushPayload payload) {
        try {
        	if(payload == null){
        		logger.info("payload为空，不进行推送");
        		return null;
        	}
            PushResult pushResult = jpushClient.sendPush(payload);
            return pushResult;
        } catch (APIConnectionException e) {
        	error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
        	error("推送消息异常。。。", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
        }
        return null;
	}
	
	/**
	 * 判断推送是否成功
	 * @param pushResult
	 * @return
	 */
	public static boolean checkPushResult(PushResult pushResult){
		if(pushResult != null && pushResult.isResultOK()){
			return true;
		}
		return false;
	}
	
	/**
	 * 发给所有平台-所有人的PushPayload
	 * @param alert
	 * @return
	 */
	public static PushPayload buildPushObject_all_all(String title, String alert, Map<String, String> extras) {
	    return new Builder()
	            .setPlatform(Platform.all())
	            .setAudience(Audience.all())
	            .setNotification(buildNotification_all(title, alert, extras))
	            .build();
	}
	
	/**
	 * 构建一个支持android和ios平台的Notification
	 * @param title
	 * @param alert
	 * @param extras
	 * @return
	 */
	private static Notification buildNotification_all(String title, String alert, Map<String, String> extras){
		return buildNotification(title, alert, extras, true, true);
	}
	
	/**
	 * 构建一个支持android平台的Notification
	 * @param title
	 * @param alert
	 * @param extras
	 * @return
	 */
	private static Notification buildNotification_android(String title, String alert, Map<String, String> extras){
		return buildNotification(title, alert, extras, true, false);
	}
	
	/**
	 * 构建一个支持ios平台的Notification
	 * @param title
	 * @param alert
	 * @param extras
	 * @return
	 */
	private static Notification buildNotification_ios(String title, String alert, Map<String, String> extras){
		return buildNotification(title, alert, extras, false, true);
	}
	
	/**
	 * 构建一个支持指定平台的Notification
	 * @param title
	 * @param alert
	 * @param extras
	 * @param supportAndorid
	 * @param supportIOS
	 * @return
	 */
	private static Notification buildNotification(String title, String alert, Map<String, String> extras, boolean supportAndorid, boolean supportIOS){
		cn.jpush.api.push.model.notification.Notification.Builder builder = Notification.newBuilder().setAlert(alert);
		if(supportAndorid){
			builder.addPlatformNotification(buildAndroidNotification(title, extras));
		}
		if(supportIOS){
			builder.addPlatformNotification(buildIosNotification(extras));
		}
        return builder.build();
	}
	
	private static IosNotification buildIosNotification(Map<String, String> extras){
		return IosNotification.newBuilder().addExtras(extras).build();
	}
	
	private static AndroidNotification buildAndroidNotification(String title, Map<String, String> extras){
		return AndroidNotification.newBuilder().setTitle(title).addExtras(extras).build();
	}
	
	/**
	 * 发给android平台-所有人的PushPayload
	 * @param alert
	 * @return
	 */
	public static PushPayload buildPushObject_android_all_alert(String title, String alert, Map<String, String> extras) {
	    return new Builder()
	            .setPlatform(Platform.android())
	            .setAudience(Audience.all())
	            .setNotification(buildNotification_android(title, alert, extras))
	            .build();
	}
	
	/**
	 * 发给android平台-所有人的PushPayload
	 * @param alert
	 * @return
	 */
	public static PushPayload buildPushObject_ios_all_alert(String title, String alert, Map<String, String> extras) {
	    return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.all())
                .setNotification(buildNotification_ios(title, alert, extras))
                .build();
	}
	
	/**
	 * 发给所有平台-指定alias的用户的PushPayload
	 * @param alias
	 * @return
	 */
    public static PushPayload buildPushObject_all_alias_alert(String title, String alert, Map<String, String> extras, String... alias) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))  //设置alias
                .setNotification(buildNotification_all(title, alert, extras))
                .build();
    }
    
    /**
	 * 发给所有平台-指定tag的用户的PushPayload
	 * @param tag
	 * @return
	 */
    public static PushPayload buildPushObject_all_tag_alert(String title, String alert, Map<String, String> extras, String... tag) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(tag))  //设置tag
                .setNotification(buildNotification_all(title, alert, extras))
                .build();
    }

}

