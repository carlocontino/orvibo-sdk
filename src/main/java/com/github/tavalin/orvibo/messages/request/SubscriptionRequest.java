package com.github.tavalin.orvibo.messages.request;

import com.github.tavalin.orvibo.messages.OrviboMessage;

public class SubscriptionRequest extends OrviboMessage {

	public SubscriptionRequest(String deviceId) {
		setDeviceId(deviceId);
	}

}
