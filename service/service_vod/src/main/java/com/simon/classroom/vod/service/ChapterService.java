package com.simon.classroom.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.vod.Chapter;
import com.simon.vo.vod.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
public interface ChapterService extends IService<Chapter> {

    /**
     * 通过课程id获得嵌套的章节列表
     * @param courseId
     * @return
     */
    List<ChapterVo> getNestedTreeList(Long courseId);

    /**
     * 根据courseId删除章节
     * @param id
     */
    void removeByCourseId(Long id);

    /**
     * 根据章节id删除章节，小节以及小节对应的视频
     * @param id
     */
    void removeByChapterId(Long id);
}
