package com.simon.classroom.vod.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.simon.classroom.vod.mapper.VideoMapper;
import com.simon.classroom.vod.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.classroom.vod.service.VodService;
import com.simon.model.vod.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VodService vodService;

    /**
     * 根据courseId删除小节，同时删除每个小节中的视频
     * @param id
     */
    @Override
    public void removeByCourseId(Long id) {
        LambdaQueryWrapper<Video> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Video::getCourseId,id);
        // 通过课程id获得所有的小节
        List<Video> videoList = baseMapper.selectList(lambdaQueryWrapper);
        // 遍历每个小节，获得每个小节中的视频id
        for (Video video : videoList) {
            String videoSourceId = video.getVideoSourceId();
            // 判断小节对应的视频id是否为空，为空则不删除
            if (!StringUtils.isEmpty(videoSourceId)){
                // 不为空则删除（删除腾讯云点播上的视频）
                vodService.removeVideo(videoSourceId);
            }
        }
        baseMapper.delete(lambdaQueryWrapper);
    }

    /**
     * 根据小节id删除小节以及小节对应的视频
     * @param id
     */
    @Override
    public void removeVideoById(Long id) {
        // 根据小节id获得小节
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();
        // 判断小节对应的视频id是否为空，为空则不删除
        if (!StringUtils.isEmpty(videoSourceId)){
            // 不为空则删除（删除腾讯云点播上的视频）
            vodService.removeVideo(videoSourceId);
        }
        // 删除小节
        baseMapper.deleteById(id);
    }

    /**
     * 根据章节id删除小节以及小节视频
     * @param id
     */
    @Override
    public void removeVideoByChapterId(Long id) {
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getChapterId,id);
        // 根据章节id获得所有的小节
        List<Video> videoList = baseMapper.selectList(wrapper);
        for (Video video : videoList){
            // 根据小节id删除小节以及小节对应的视频
            removeVideoById(video.getId());
        }
    }
}
