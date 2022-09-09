package com.simon.classroom.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.simon.classroom.live.mapper.LiveCourseConfigMapper;
import com.simon.classroom.live.service.LiveCourseConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.model.live.LiveCourseConfig;
import com.simon.model.live.LiveCourseDescription;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 直播课程配置表 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-09-07
 */
@Service
public class LiveCourseConfigServiceImpl extends ServiceImpl<LiveCourseConfigMapper, LiveCourseConfig> implements LiveCourseConfigService {

    /**
     * 根据直播id获得直播课程的配置
     * @param id
     * @return
     */
    @Override
    public LiveCourseConfig getByCourseId(Long id) {
        LambdaQueryWrapper<LiveCourseConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveCourseConfig::getLiveCourseId,id);
        LiveCourseConfig liveCourseConfig = baseMapper.selectOne(wrapper);
        return liveCourseConfig;
    }
}
