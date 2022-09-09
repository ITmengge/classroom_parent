package com.simon.classroom.client.activity;

import com.simon.model.activity.CouponInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-activity")
public interface CouponInfoFeignClient {

    @ApiOperation("获取优惠卷")
    @GetMapping("inner/getById/{couponId}")
    CouponInfo getCouponInfo(@PathVariable Long couponId);

    @ApiOperation("更新优惠卷使用状态")
    @GetMapping("inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId") Long couponUseId,
                                             @PathVariable("orderId") Long orderId);

    @ApiOperation("判断优惠卷是否过期")
    @GetMapping("inner/judgeCouponIsExpire/{couponId}")
    Boolean judgeCouponIsExpire(@PathVariable Long couponId);
}
