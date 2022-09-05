package com.simon.classroom.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.user.UserInfo;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author simon
 * @since 2022-09-03
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 根据openId查询数据库信息
     * @param openId
     * @return
     */
    UserInfo getUserInfoByOpenid(String openId);
}
