package com.simon.classroom.order.api;

import com.simon.classroom.order.service.OrderInfoService;
import com.simon.classroom.result.Result;
import com.simon.vo.order.OrderFormVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderInfoApiController {

    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation("新增点播课程订单")
    @PostMapping("submitOrder")
    public Result submitOrder(@RequestBody OrderFormVo orderFormVo,
                              HttpServletRequest request){
        // 返回订单id
        Long orderId = orderInfoService.submitOrder(orderFormVo);
        return Result.success(orderId);
    }
}
