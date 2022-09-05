package com.simon.classroom.activity.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.classroom.activity.service.CouponInfoService;
import com.simon.classroom.result.Result;
import com.simon.model.activity.CouponInfo;
import com.simon.model.activity.CouponUse;
import com.simon.vo.activity.CouponUseQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 优惠券信息 前端控制器
 * </p>
 *
 * @author simon
 * @since 2022-09-02
 */
@RestController
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {

    @Autowired
    private CouponInfoService couponInfoService;

    @ApiOperation("获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result getCouponInfoList(@PathVariable Long page,
                                    @PathVariable Long limit){
        Page<CouponInfo> pageParam = new Page<>(page,limit);
        IPage<CouponInfo> pageModel = couponInfoService.page(pageParam);
        return Result.success(pageModel);
    }

    @ApiOperation("获取优惠卷")
    @GetMapping("get/{id}")
    public Result getCoupon(@PathVariable Long id){
        CouponInfo couponInfo = couponInfoService.getById(id);
        return Result.success(couponInfo);
    }

    @ApiOperation(value = "新增优惠券")
    @PostMapping("save")
    public Result save(@RequestBody CouponInfo couponInfo) {
        couponInfoService.save(couponInfo);
        return Result.success(null);
    }

    @ApiOperation("修改优惠卷")
    @PostMapping("update")
    public Result update(@RequestBody CouponInfo couponInfo){
        couponInfoService.updateById(couponInfo);
        return Result.success(null);
    }

    @ApiOperation("删除优惠卷")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        couponInfoService.removeById(id);
        return Result.success(null);
    }

    @ApiOperation("根据id列表删除优惠卷")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<String> idList){
        couponInfoService.removeByIds(idList);
        return Result.success(null);
    }

    @ApiOperation("获取已使用的优惠卷分页列表")
    @GetMapping("couponUse/{page}/{limit}")
    public Result getCouponUseList(@PathVariable Long page,
                                   @PathVariable Long limit,
                                   CouponUseQueryVo couponUseQueryVo){
        Page<CouponUse> pageParam = new Page<>(page,limit);
        IPage<CouponUse> pageModel = couponInfoService.selectCouponUsePage(pageParam, couponUseQueryVo);
        return Result.success(pageModel);
    }
}

