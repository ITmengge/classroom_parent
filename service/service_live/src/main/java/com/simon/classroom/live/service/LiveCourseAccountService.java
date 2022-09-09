package com.simon.classroom.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.live.LiveCourseAccount;

/**
 * <p>
 * 直播课程账号表（受保护信息） 服务类
 * </p>
 *
 * @author simon
 * @since 2022-09-07
 */
public interface LiveCourseAccountService extends IService<LiveCourseAccount> {

    /**
     * 获取直播课程的账号信息
     * @param id
     * @return
     */
    LiveCourseAccount getByLiveCourseId(Long id);
}
