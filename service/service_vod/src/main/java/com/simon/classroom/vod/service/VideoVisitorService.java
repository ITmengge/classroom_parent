package com.simon.classroom.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.vod.VideoVisitor;

import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 服务类
 * </p>
 *
 * @author simon
 * @since 2022-08-31
 */
public interface VideoVisitorService extends IService<VideoVisitor> {

    /**
     * 根据课程id以及开始、结束时间获得课程统计数据
     * @param courseId
     * @param startDate
     * @param endDate
     * @return
     */
    Map<String, Object> findCount(Long courseId, String startDate, String endDate);
}
