package com.simon.classroom.order.service.impl;
import java.util.Date;
import com.google.common.collect.Maps;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.classroom.client.activity.CouponInfoFeignClient;
import com.simon.classroom.client.course.CourseFeignClient;
import com.simon.classroom.client.user.UserInfoFeignClient;
import com.simon.classroom.exception.ClassroomException;
import com.simon.classroom.order.mapper.OrderInfoMapper;
import com.simon.classroom.order.service.OrderDetailService;
import com.simon.classroom.order.service.OrderInfoService;
import com.simon.classroom.utils.AuthContextHolder;
import com.simon.classroom.utils.OrderNoUtils;
import com.simon.model.activity.CouponInfo;
import com.simon.model.order.OrderDetail;
import com.simon.model.order.OrderInfo;
import com.simon.model.user.UserInfo;
import com.simon.model.vod.Course;
import com.simon.vo.order.OrderFormVo;
import com.simon.vo.order.OrderInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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

    // 优惠卷远程调用接口
    @Autowired
    private CouponInfoFeignClient couponInfoFeignClient;

    // 课程远程调用接口
    @Autowired
    private CourseFeignClient courseFeignClient;

    // 用户远程调用接口
    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

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
     * 新增点播课程订单
     * @param orderFormVo
     * @return
     */
    @Override
    public Long submitOrder(OrderFormVo orderFormVo) {
        // 1、获取生成订单条件值
        Long userId = AuthContextHolder.getUserId();    // 从工具类中获取请求头中的token值，解析token获得userId
        Long courseId = orderFormVo.getCourseId();
        Long couponId = orderFormVo.getCouponId();

        // 2、查询当前用户是否存在当前课程的订单
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getCourseId,courseId);
        wrapper.eq(OrderDetail::getUserId,userId);
        OrderDetail orderDetailExist = orderDetailService.getOne(wrapper);
        if (orderDetailExist != null){
            return orderDetailExist.getId();     // 订单已经存在，直接返回订单id
        }

        // 3、根据课程id查询课程信息
        Course course = courseFeignClient.getCourseById(courseId);
        if (course == null){
            throw new ClassroomException(20001,"课程不存在");
        }

        // 4、根据用户id查询用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);
        if (userInfo == null){
            throw new ClassroomException(20001,"用户不存在");
        }

        // 5、根据优惠卷id查询优惠卷信息（可能没有优惠卷）
        BigDecimal couponReduce = new BigDecimal(0);    // 优惠卷金额处理
        if (couponId != null){      // 前端有传优惠卷id过来，说明使用了优惠卷，需要进行查询
            CouponInfo couponInfo = couponInfoFeignClient.getCouponInfo(couponId);
            couponReduce = couponInfo.getAmount();
        }

        // 6、封装订单生成的数据到对象，完成添加订单
        // 6.1、封装数据到OrderInfo对象里面，添加订单基本信息表
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setNickName(userInfo.getNickName());
        orderInfo.setPhone(userInfo.getPhone());
        orderInfo.setProvince(userInfo.getProvince());
        orderInfo.setOriginAmount(course.getPrice());
        orderInfo.setCouponReduce(couponReduce);
        orderInfo.setFinalAmount(orderInfo.getOriginAmount().subtract(orderInfo.getCouponReduce()));// 课程价格 - 优惠卷价格
        orderInfo.setOrderStatus("0");          // 订单生成成功，但未支付
        orderInfo.setOutTradeNo(OrderNoUtils.getOrderNo()); // 使用工具类生成订单流水号
        orderInfo.setTradeBody(course.getTitle());
        baseMapper.insert(orderInfo);

        // 6.2、封装数据到OrderDetail对象里面，添加订单详情信息表
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCourseId(courseId);
        orderDetail.setCourseName(course.getTitle());
        orderDetail.setCover(course.getCover());
        orderDetail.setOrderId(orderInfo.getId());
        orderDetail.setUserId(userId);
        orderDetail.setOriginAmount(course.getPrice());
        orderDetail.setCouponReduce(couponReduce);
        orderDetail.setFinalAmount(orderDetail.getOriginAmount().subtract(orderDetail.getCouponReduce()));
        orderDetailService.save(orderDetail);

        // 7、更新优惠卷状态
        if (couponInfoFeignClient.judgeCouponIsExpire(couponId)){   // 判断优惠卷是否过期
            throw new ClassroomException(20001,"优惠卷已过期");
        }
        if (orderFormVo.getCouponUseId() != null){
            couponInfoFeignClient.updateCouponInfoUseStatus(orderFormVo.getCouponUseId(), orderInfo.getId());
        }
        // 8、返回订单id
        return orderInfo.getId();
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
