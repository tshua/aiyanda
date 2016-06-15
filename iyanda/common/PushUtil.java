package com.iyanda.common;


import net.sf.json.JSONObject;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.baidu.yun.push.model.PushMsgToTagRequest;
import com.baidu.yun.push.model.PushMsgToTagResponse;

public class PushUtil {
	
	private static String apiKey = "1ZcC1t3G7g70Q6Zpr1g2jGxh";
	private static String secretKey = "tfyUQ7IeB6eF31uHwse5M8IsZxUCcCfY";
	
	
	
	
	

	public static void pushToAll(String title,String content) throws PushClientException, PushServerException {

		// 1. get apiKey and secretKey from developer console

		PushKeyPair pair = new PushKeyPair(apiKey, secretKey);

		// 2. build a BaidupushClient object to access released interfaces
		BaiduPushClient pushClient = new BaiduPushClient(pair,
				BaiduPushConstants.CHANNEL_REST_URL);

		// 3. register a YunLogHandler to get detail interacting information
		// in this request.
		pushClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println(event.getMessage());
			}
		});

		try {

			// 4. specify request arguments
			// 创建 Android的通知
			JSONObject notification = new JSONObject();
			notification.put("title", title);
			notification.put("description", content);
			notification.put("notification_builder_id", 0);
			notification.put("notification_basic_style", 4);
			notification.put("open_type", 1);
			notification.put("url", "http://push.baidu.com");
			JSONObject jsonCustormCont = new JSONObject();
			jsonCustormCont.put("key", "value"); // 自定义内容，key-value
			notification.put("custom_content", jsonCustormCont);

			// 4. specify request arguments
			PushMsgToAllRequest request = new PushMsgToAllRequest()
					.addMsgExpires(new Integer(3600)).addMessageType(1)
					.addMessage(notification.toString()) // 添加透传消息
					.addSendTime(System.currentTimeMillis() / 1000 + 120) // 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例2分钟后推送
					.addDeviceType(3);

			// 5. http request
			PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
			// Http请求结果解析打印
			System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
					+ response.getSendTime() + ",timerId: "
					+ response.getTimerId());
		} catch (PushClientException e) {
			if (BaiduPushConstants.ERROROPTTYPE) {
				throw e;
			} else {
				e.printStackTrace();
			}
		} catch (PushServerException e) {
			if (BaiduPushConstants.ERROROPTTYPE) {
				throw e;
			} else {
				System.out.println(String.format(
						"requestId: %d, errorCode: %d, errorMessage: %s",
						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
			}
		}
	}
	
	
	
	
	
	public static void pushToTag(String tagname,String title,String content) throws PushClientException, PushServerException {

		// 1. get apiKey and secretKey from developer console

		
		PushKeyPair pair = new PushKeyPair(apiKey, secretKey);

		// 2. build a BaidupushClient object to access released interfaces
		BaiduPushClient pushClient = new BaiduPushClient(pair,
				BaiduPushConstants.CHANNEL_REST_URL);

		// 3. register a YunLogHandler to get detail interacting information
		// in this request.
		pushClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				System.out.println(event.getMessage());
			}
		});

		try {
			// 4. specify request arguments
			// 创建 Android的通知
			JSONObject notification = new JSONObject();
			notification.put("title", title);
			notification.put("description", content);
			notification.put("notification_builder_id", 0);
			notification.put("notification_basic_style", 4);
			notification.put("open_type", 1);
			notification.put("url", "http://push.baidu.com");
			JSONObject jsonCustormCont = new JSONObject();
			jsonCustormCont.put("key", "value"); // 自定义内容，key-value
			notification.put("custom_content", jsonCustormCont);
			
			
			// 4. specify request arguments
			// pushTagTpye = 1 for common tag pushing
			PushMsgToTagRequest request = new PushMsgToTagRequest()
					.addTagName(tagname)
					.addMsgExpires(new Integer(3600))
					.addMessageType(1)  // 添加通知
					// .addSendTime(System.currentTimeMillis() / 1000 + 120) //设置定时任务
					.addMessage(notification.toString())
					.addDeviceType(3);
			// 5. http request
			PushMsgToTagResponse response = pushClient.pushMsgToTag(request);
			// Http请求结果解析打印
			System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
					+ response.getSendTime() + ",timerId: "
					+ response.getTimerId());
		} catch (PushClientException e) {
			if (BaiduPushConstants.ERROROPTTYPE) {
				throw e;
			} else {
				e.printStackTrace();
			}
		} catch (PushServerException e) {
			if (BaiduPushConstants.ERROROPTTYPE) {
				throw e;
			} else {
				System.out.println(String.format(
						"requestId: %d, errorCode: %d, errorMessage: %s",
						e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
			}
		}
	}
}
