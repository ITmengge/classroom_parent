package com.simon.classroom.vod.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simon.model.vod.Course;
import com.simon.vo.vod.CoursePublishVo;
import com.simon.vo.vod.CourseVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 根据id获得课程发布信息
     * @param id
     * @return
     */
    CoursePublishVo getCoursePublishVoById(Long id);

    /**
     * 根据课程id查询课程详情数据（与上面类似，只是多几个字段）
     * @param courseId
     * @return
     */
    CourseVo selectCourseVoById(Long courseId);
}
