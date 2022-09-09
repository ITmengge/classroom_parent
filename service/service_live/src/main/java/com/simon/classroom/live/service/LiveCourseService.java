package com.simon.classroom.live.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.live.LiveCourse;
import com.simon.vo.live.LiveCourseConfigVo;
import com.simon.vo.live.LiveCourseFormVo;
import com.simon.vo.live.LiveCourseVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播课程表 服务类
 * </p>
 *
 * @author simon
 * @since 2022-09-07
 */
public interface LiveCourseService extends IService<LiveCourse> {

    /**
     * 获得分页的直播课程列表
     * @param pageParam
     * @return
     */
    IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam);

    /**
     * 直播课程添加
     * @param liveCourseFormVo
     */
    void saveLive(LiveCourseFormVo liveCourseFormVo);

    /**
     * 直播课程删除
     * @param id
     */
    void removeLive(Long id);

    /**
     * 根据id查询直播课程基本信息和描述信息
     * @param id
     * @return
     */
    LiveCourseFormVo getLiveCourseFormVo(Long id);

    /**
     * 更新直播信息
     * @param liveCourseFormVo
     */
    void updateById(LiveCourseFormVo liveCourseFormVo);

    /**
     * 获取直播课程的配置信息和商品列表
     * @param id
     * @return
     */
    LiveCourseConfigVo getCourseConfig(Long id);

    /**
     * 更新配置信息
     * @param liveCourseConfigVo
     */
    void updateConfig(LiveCourseConfigVo liveCourseConfigVo);

    /**
     * 获取最近的五条直播
     * @return
     */
    List<LiveCourseVo> findLatelyList();

    /**
     * 获取用户access_token
     * @param id
     * @param userId
     * @return
     */
    JSONObject getPlayAuth(Long id, Long userId);

    /**
     * 根据ID查询课程
     * @param courseId
     * @return
     */
    Map<String, Object> getInfoById(Long courseId);
}
