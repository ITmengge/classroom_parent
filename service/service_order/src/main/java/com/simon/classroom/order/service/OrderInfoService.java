package com.simon.classroom.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.order.OrderInfo;
import com.simon.vo.order.OrderFormVo;
import com.simon.vo.order.OrderInfoQueryVo;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务类
 * </p>
 *
 * @author simon
 * @since 2022-09-02
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 获取分页列表
     * @param pageParam
     * @param orderInfoQueryVo
     * @return
     */
    Map<String, Object> findPageOrderInfo(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo);

    /**
     * 新增点播课程订单
     * @param orderFormVo
     * @return
     */
    Long submitOrder(OrderFormVo orderFormVo);
}
