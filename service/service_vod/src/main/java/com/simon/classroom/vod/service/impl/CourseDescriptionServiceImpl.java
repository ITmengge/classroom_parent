package com.simon.classroom.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.simon.classroom.vod.mapper.CourseDescriptionMapper;
import com.simon.classroom.vod.service.CourseDescriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.model.vod.Chapter;
import com.simon.model.vod.CourseDescription;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
@Service
public class CourseDescriptionServiceImpl extends ServiceImpl<CourseDescriptionMapper, CourseDescription> implements CourseDescriptionService {

    /**
     * 通过课程ID获得课程描述对象
     * @param id
     * @return
     */
    @Override
    public CourseDescription queryByCourseId(Long id) {
        QueryWrapper<CourseDescription> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",id);
        CourseDescription courseDescription = baseMapper.selectOne(wrapper);
        return courseDescription;
    }

    /**
     * 根据courseId删除课程描述
     * @param id
     */
    @Override
    public void removeByCourseId(Long id) {
        LambdaQueryWrapper<CourseDescription> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseDescription::getCourseId,id);
        baseMapper.delete(wrapper);
    }
}
