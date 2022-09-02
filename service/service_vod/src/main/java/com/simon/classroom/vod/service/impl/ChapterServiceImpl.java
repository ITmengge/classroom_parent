package com.simon.classroom.vod.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.simon.classroom.vod.mapper.ChapterMapper;
import com.simon.classroom.vod.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.classroom.vod.service.VideoService;
import com.simon.classroom.vod.service.VodService;
import com.simon.model.vod.Chapter;
import com.simon.model.vod.Video;
import com.simon.vo.vod.ChapterVo;
import com.simon.vo.vod.VideoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VodService vodService;

    /**
     * 通过课程id获得嵌套的章节列表
     * @param courseId
     * @return
     */
    @Override
    public List<ChapterVo> getNestedTreeList(Long courseId) {
        List<ChapterVo> chapterVoList = new ArrayList<>();

        // 根据courseId获得课程的所有章节信息
        LambdaQueryWrapper<Chapter> chapterLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chapterLambdaQueryWrapper.eq(Chapter::getCourseId,courseId);
        // 当getCourseId相同时，比较sort
        chapterLambdaQueryWrapper.orderByAsc(Chapter::getSort, Chapter::getCourseId);
        List<Chapter> chapterList = baseMapper.selectList(chapterLambdaQueryWrapper);

        // 根据courseId获得课程的所有小节信息
        LambdaQueryWrapper<Video> videoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        videoLambdaQueryWrapper.eq(Video::getCourseId,courseId);
        videoLambdaQueryWrapper.orderByAsc(Video::getSort,Video::getCourseId);
        List<Video> videoList = videoService.list(videoLambdaQueryWrapper);

        // 封装章节
        // 遍历所有章节
        for (int i = 0; i < chapterList.size();i++){
            Chapter chapter = chapterList.get(i);
            // 创建ChapterVo对象，chapter -》 chapterVo
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            chapterVoList.add(chapterVo);

            // 遍历所有小节
            List<VideoVo> videoVoList = new ArrayList<>();
            for (int j = 0; j < videoList.size(); j++){
                Video video = videoList.get(j);
                // 通过比较课程对应的章节id与小节id，而不是直接拿章节id与数据库比较小节，避免for数据库
                if (chapter.getId().equals(video.getChapterId())){
                    // 当小节的对应的章节id与上面的章节id相同时，则添加到小节列表中
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoList.add(videoVo);
                }
            }
            // 把章节里面的所有小节集合放到每个章节里面
            chapterVo.setChildren(videoVoList);
        }
        return chapterVoList;
    }

    /**
     * 根据courseId删除小节
     * @param id
     */
    @Override
    public void removeByCourseId(Long id) {
        LambdaQueryWrapper<Chapter> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Chapter::getCourseId,id);
        baseMapper.delete(wrapper);
    }

    /**
     * 根据章节id删除章节，小节以及小节对应的视频
     * @param id
     */
    @Override
    public void removeByChapterId(Long id) {
        // 根据章节id获得章节
        Chapter chapter = baseMapper.selectById(id);
        // 通过章节id删除小节以及对应的视频
        videoService.removeVideoByChapterId(id);
        // 删除章节
        baseMapper.deleteById(id);
    }

}
