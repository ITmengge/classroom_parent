package com.simon.classroom.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.live.LiveCourseConfig;

/**
 * <p>
 * 直播课程配置表 服务类
 * </p>
 *
 * @author simon
 * @since 2022-09-07
 */
public interface LiveCourseConfigService extends IService<LiveCourseConfig> {

    /**
     * 根据直播id获得直播课程的配置
     * @param id
     * @return
     */
    LiveCourseConfig getByCourseId(Long id);
}
