package com.simon.classroom.order.api;

import com.simon.classroom.order.service.WXPayService;
import com.simon.classroom.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "微信支付接口")
@RestController
@RequestMapping("/api/order/wxPay")
public class WxPayController {

    @Autowired
    private WXPayService wxPayService;

    @ApiOperation(value = "微信支付")
    @GetMapping("/createJsapi/{orderNo}")
    public Result createJsapi( @ApiParam(name = "orderNo", value = "订单No", required = true)
                                   @PathVariable("orderNo") String orderNo){
        Map<String,Object> map = wxPayService.createJsapi(orderNo);
        return Result.success(map);
    }
}
