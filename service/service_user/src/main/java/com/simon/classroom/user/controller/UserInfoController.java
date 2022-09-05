package com.simon.classroom.user.controller;


import com.simon.classroom.result.Result;
import com.simon.classroom.user.service.UserInfoService;
import com.simon.model.user.UserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author simon
 * @since 2022-09-03
 */
@RestController
@RequestMapping("/admin/user/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 获取优惠券详情时候，需要获取使用者的昵称和手机号，所以使用远程调用实现此功能。
     * @param id
     * @return
     */
    @ApiOperation("获取用户信息")
    @GetMapping("inner/getById/{id}")
    public UserInfo getById(@PathVariable Long id){
        // 为了方便取值，这里就不返回Result了，直接返回userInfo（因为都是后端，远程调用而已）
        UserInfo userInfo = userInfoService.getById(id);
        return userInfo;
    }

}

