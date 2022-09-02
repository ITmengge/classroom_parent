package com.simon.classroom.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.vod.Video;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
public interface VideoService extends IService<Video> {

    /**
     * 根据courseId删除小节
     * @param id
     */
    void removeByCourseId(Long id);

    /**
     * 根据小节id删除小节以及小节对应的视频
     * @param id
     */
    void removeVideoById(Long id);

    /**
     * 根据章节id删除小节以及小节视频
     */
    void removeVideoByChapterId(Long id);

}
