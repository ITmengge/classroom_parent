package com.simon.classroom.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simon.model.live.LiveCourse;
import com.simon.vo.live.LiveCourseVo;

import java.util.List;

/**
 * <p>
 * 直播课程表 Mapper 接口
 * </p>
 *
 * @author simon
 * @since 2022-09-07
 */
public interface LiveCourseMapper extends BaseMapper<LiveCourse> {

    /**
     * 获取最近的直播
     * @return
     */
    List<LiveCourseVo> findLateLyList();
}
