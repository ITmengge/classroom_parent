package com.simon.classroom.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.classroom.order.service.OrderInfoService;
import com.simon.classroom.result.Result;
import com.simon.model.order.OrderInfo;
import com.simon.vo.order.OrderInfoQueryVo;
import com.simon.vo.order.OrderInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 前端控制器
 * </p>
 *
 * @author simon
 * @since 2022-09-02
 */
@Api(tags = "订单管理")
@RestController
@RequestMapping("/admin/order/orderInfo")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation("获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result getOrderList(@PathVariable Long page,
                               @PathVariable Long limit,
                               OrderInfoQueryVo orderInfoQueryVo){
        Page<OrderInfo> pageParam = new Page<>(page, limit);
        Map<String, Object> map = orderInfoService.findPageOrderInfo(pageParam, orderInfoQueryVo);
        return Result.success(map);
    }
}

