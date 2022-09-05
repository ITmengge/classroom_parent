package com.simon.classroom.order.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.classroom.order.mapper.OrderInfoMapper;
import com.simon.classroom.order.service.OrderDetailService;
import com.simon.classroom.order.service.OrderInfoService;
import com.simon.model.order.OrderDetail;
import com.simon.model.order.OrderInfo;
import com.simon.vo.order.OrderInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-09-02
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 获取分页列表
     * @param pageParam
     * @param orderInfoQueryVo
     * @return
     */
    @Override
    public Map<String, Object> findPageOrderInfo(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo) {
        // 1、取出查询条件
        Long userId = orderInfoQueryVo.getUserId();     // 用户id
        String phone = orderInfoQueryVo.getPhone();     // 用户电话号码
        Integer orderStatus = orderInfoQueryVo.getOrderStatus();    // 订单状态
        String outTradeNo = orderInfoQueryVo.getOutTradeNo();       // 订单交易编号
        String createTimeBegin = orderInfoQueryVo.getCreateTimeBegin(); // 创建时间起始
        String createTimeEnd = orderInfoQueryVo.getCreateTimeEnd();     // 创建时间结束

        // 2、判断条件是否为空，不为空则添加到wrapper中
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(userId)){
            wrapper.eq("user_id",userId);
        }
        if (!StringUtils.isEmpty(phone)){
            wrapper.eq("phone",phone);
        }
        if (!StringUtils.isEmpty(orderStatus)){
            wrapper.eq("order_status",orderStatus);
        }
        if (!StringUtils.isEmpty(outTradeNo)){
            wrapper.eq("out_trade_no",outTradeNo);
        }
        if (!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge("create_time",createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)){
            wrapper.le("create_time",createTimeEnd);
        }

        // 3、分页查询，获得总条数，总页数以及每条记录
        Page<OrderInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        long total = pages.getTotal();
        long pageCount = pages.getPages();
        List<OrderInfo> records = pages.getRecords();

        // 4、遍历订单，根据订单id获得订单名称
        records.stream().forEach(item -> {
            this.getOrderDetailById(item);
        });

        // 5、将查询出来的数据封装到map集合并返回
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("pageCount",pageCount);
        map.put("records",records);
        return map;
    }

    /**
     * 根据订单id获得订单名称
     * @param orderInfo
     */
    private OrderInfo getOrderDetailById(OrderInfo orderInfo) {
        // 根据订单id获得订单详情
        OrderDetail orderDetail = orderDetailService.getById(orderInfo.getId());
        // 根据id查询出的对象，最好都判空一下再进行操作
        if (orderDetail != null){
            String courseName = orderDetail.getCourseName();
            // 将课程名字放进param集合中
            orderInfo.getParam().put("courseName",courseName);
        }
        return orderInfo;
    }
}
