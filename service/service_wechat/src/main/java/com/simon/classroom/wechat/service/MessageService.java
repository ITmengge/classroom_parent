package com.simon.classroom.wechat.service;

import java.util.Map;

public interface MessageService {
    /**
     * 接收消息
     * @param param
     * @return
     */
    String receiveMessage(Map<String, String> param);

    /**
     * 模板消息，如订单支付成功的通知
     * @param orderId
     */
    void pushPayMessage(Long orderId);
}
