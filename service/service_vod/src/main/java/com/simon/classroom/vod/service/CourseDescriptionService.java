package com.simon.classroom.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.vod.CourseDescription;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
public interface CourseDescriptionService extends IService<CourseDescription> {

    /**
     * 通过课程ID获得课程描述对象
     * @param id
     * @return
     */
    CourseDescription queryByCourseId(Long id);

    /**
     * 根据courseId删除课程描述
     * @param id
     */
    void removeByCourseId(Long id);
}
