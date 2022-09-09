package com.simon.classroom.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.live.LiveCourseDescription;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author simon
 * @since 2022-09-07
 */
public interface LiveCourseDescriptionService extends IService<LiveCourseDescription> {

    /**
     * 通过直播课程id获得直播课程
     * @param id
     */
    LiveCourseDescription getByLiveCourseId(Long id);
}
