package com.simon.classroom.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.activity.CouponInfo;
import com.simon.model.activity.CouponUse;
import com.simon.vo.activity.CouponUseQueryVo;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author simon
 * @since 2022-09-02
 */
public interface CouponInfoService extends IService<CouponInfo> {

    /**
     * 获取已使用的优惠卷分页列表
     * @param pageParam
     * @param couponUseQueryVo
     * @return
     */
    IPage<CouponUse> selectCouponUsePage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo);

    /**
     * 更新优惠卷使用状态
     * @param couponUseId
     * @param orderId
     */
    Boolean updateCouponInfoUseStatus(Long couponUseId, Long orderId);

    /**
     * 判断优惠卷是否过期
     * @param couponId
     * @return
     */
    Boolean judgeCouponIsExpire(Long couponId);
}
