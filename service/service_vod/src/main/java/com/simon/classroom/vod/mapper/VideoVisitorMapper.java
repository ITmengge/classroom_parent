package com.simon.classroom.vod.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simon.model.vod.VideoVisitor;
import com.simon.vo.vod.VideoVisitorCountVo;

import java.util.List;

/**
 * <p>
 * 视频来访者记录表 Mapper 接口
 * </p>
 *
 * @author simon
 * @since 2022-08-31
 */
public interface VideoVisitorMapper extends BaseMapper<VideoVisitor> {

    /**
     * // 获得访问课程的所有数据（进入时间以及用户个数）
     * @param courseId
     * @param startDate
     * @param endDate
     * @return
     */
    List<VideoVisitorCountVo> findCount(Long courseId, String startDate, String endDate);
}
