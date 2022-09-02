package com.simon.classroom.vod.service.impl;

import com.simon.classroom.vod.mapper.VideoVisitorMapper;
import com.simon.classroom.vod.service.VideoVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.model.vod.VideoVisitor;
import com.simon.vo.vod.VideoVisitorCountVo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频来访者记录表 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-08-31
 */
@Service
public class VideoVisitorServiceImpl extends ServiceImpl<VideoVisitorMapper, VideoVisitor> implements VideoVisitorService {

    /**
     * 根据课程id以及开始、结束时间获得课程统计数据
     * @param courseId
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Map<String, Object> findCount(Long courseId, String startDate, String endDate) {
        // 获得访问课程的所有数据（进入时间以及用户个数）
        List<VideoVisitorCountVo> videoVisitorCountVoList = baseMapper.findCount(courseId,startDate,endDate);
        System.out.println("获得访问课程的所有数据（进入时间以及用户个数）" + videoVisitorCountVoList.toString());
        Map<String,Object> map = new HashMap<>();
        // 使用ECharts展示图表，最基本的折线图需要有横纵坐标，分别是两个list集合
        // 所以需要创建两个list集合，一个代表所有日期，一个代表日期对应的用户个数
        // 解释：通过stream流的方式将列表中每个join_time转换成list集合，Collectors是工具类
        List<String> dateList = videoVisitorCountVoList.stream().map(VideoVisitorCountVo::getJoinTime).collect(Collectors.toList());
        List<Integer> countList = videoVisitorCountVoList.stream().map(VideoVisitorCountVo::getUserCount).collect(Collectors.toList());

        // 放到map集合
        map.put("xData", dateList);
        map.put("yData", countList);
        return map;
    }
}
