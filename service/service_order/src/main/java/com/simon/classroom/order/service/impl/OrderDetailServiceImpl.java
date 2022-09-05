package com.simon.classroom.order.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.classroom.order.mapper.OrderDetailMapper;
import com.simon.classroom.order.service.OrderDetailService;
import com.simon.model.order.OrderDetail;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-09-02
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
