package com.simon.classroom.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.classroom.activity.mapper.CouponInfoMapper;
import com.simon.classroom.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.classroom.activity.service.CouponUseService;
import com.simon.classroom.client.user.UserInfoFeignClient;
import com.simon.model.activity.CouponInfo;
import com.simon.model.activity.CouponUse;
import com.simon.model.user.UserInfo;
import com.simon.vo.activity.CouponUseQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-09-02
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {


    @Autowired
    private CouponUseService couponUseService;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    /**
     * 获取已使用的优惠卷分页列表
     * @param pageParam
     * @param couponUseQueryVo
     * @return
     */

    @Override
    public IPage<CouponUse> selectCouponUsePage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo) {
        // 1、从查询条件中获得数据
        Long couponId = couponUseQueryVo.getCouponId();
        String couponStatus = couponUseQueryVo.getCouponStatus();
        String getTimeBegin = couponUseQueryVo.getGetTimeBegin();
        String getTimeEnd = couponUseQueryVo.getGetTimeEnd();

        // 2、判断条件是否为空，不为空则添加到查询构造器中
        QueryWrapper<CouponUse> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(couponId)){
            wrapper.eq("coupon_id",couponId);
        }
        if(!StringUtils.isEmpty(couponStatus)) {
            wrapper.eq("coupon_status",couponStatus);
        }
        if(!StringUtils.isEmpty(getTimeBegin)) {
            wrapper.ge("get_time",getTimeBegin);
        }
        if(!StringUtils.isEmpty(getTimeEnd)) {
            wrapper.le("get_time",getTimeEnd);
        }

        // 3、查询
        IPage<CouponUse> pages = couponUseService.page(pageParam, wrapper);
        List<CouponUse> couponUseList = pages.getRecords();
        // 4、遍历结果集，封装用户昵称和手机号
        couponUseList.stream().forEach(item -> {
            this.getUserInfoByCouponUse(item);
        });

        return pages;
    }

    /**
     * 封装用户昵称和手机号
     * @param couponUse
     */
    private CouponUse getUserInfoByCouponUse(CouponUse couponUse) {
        Long userId = couponUse.getUserId();
        if (!StringUtils.isEmpty(userId)){
            // 通过远程调用接口获取用户信息
            UserInfo userInfo = userInfoFeignClient.getById(userId);
            if (userInfo != null){
                couponUse.getParam().put("nickName",userInfo.getNickName());
                couponUse.getParam().put("phone",userInfo.getPhone());
            }
        }
        return couponUse;
    }
}
