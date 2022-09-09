package com.simon.classroom.activity.api;

import com.simon.classroom.activity.service.CouponInfoService;
import com.simon.model.activity.CouponInfo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "优惠卷接口")
@RestController
@RequestMapping("/api/activity/couponInfo")
public class CouponInfoApiController {

    @Autowired
    private CouponInfoService couponInfoService;

    @ApiOperation("获取优惠卷")
    @GetMapping("inner/getById/{couponId}")
    public CouponInfo getCouponInfo(@PathVariable Long couponId){
        CouponInfo couponInfo = couponInfoService.getById(couponId);
        return couponInfo;
    }

    @ApiOperation("更新优惠卷使用状态")
    @GetMapping("inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId") Long couponUseId,
                                             @PathVariable("orderId") Long orderId){
         Boolean isSuccess = couponInfoService.updateCouponInfoUseStatus(couponUseId, orderId);
         return isSuccess;
    }

    @ApiOperation("判断优惠卷是否过期")
    @GetMapping("inner/judgeCouponIsExpire/{couponId}")
    public Boolean judgeCouponIsExpire(@PathVariable Long couponId){
        Boolean isExpire = couponInfoService.judgeCouponIsExpire(couponId);
        return isExpire;
    }
}
