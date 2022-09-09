package com.simon.classroom.order.service;

import java.util.Map;

public interface WXPayService {
    /**
     * 微信支付
     * @param orderNo
     * @return
     */
    Map<String, Object> createJsapi(String orderNo);
}
