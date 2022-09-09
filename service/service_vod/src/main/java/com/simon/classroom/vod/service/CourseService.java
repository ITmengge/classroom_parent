package com.simon.classroom.vod.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.vod.Course;
import com.simon.vo.vod.CourseFormVo;
import com.simon.vo.vod.CoursePublishVo;
import com.simon.vo.vod.CourseQueryVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
public interface CourseService extends IService<Course> {

    /**
     * 条件查询带分页的点播课程列表
     * @param pageParam
     * @param courseQueryVo
     * @return
     */
    Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    /**
     * 新增课程基本信息
     * @param courseFormVo
     * @return
     */
    Long saveCourseInfo(CourseFormVo courseFormVo);

    /**
     * 根据id获取课程基本信息对象
     * @param id
     * @return
     */
    CourseFormVo getCourseFormVoById(Long id);

    /**
     * 修改课程基本对象
     * @param courseFormVo
     * @return
     */
    void updateCourseById(CourseFormVo courseFormVo);

    /**
     * 根据id获得课程发布信息
     * @param id
     * @return
     */
    CoursePublishVo getCoursePublishVoById(Long id);

    /**
     * 根据id发布课程
     * @param id
     * @return
     */
    boolean publishCourseById(Long id);

    /**
     * 删除课程
     * @param id
     */
    void removeCourse(Long id);

    /**
     * 根据ID查询课程
     * @param courseId
     * @return
     */
    Map<String, Object> getInfoById(Long courseId);

    /**
     * 查询所有课程以及讲师和分类名
     * @return
     */
    List<Course> findlist();
}
