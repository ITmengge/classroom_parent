package com.simon.classroom.vod.controller;


import com.simon.classroom.result.Result;
import com.simon.classroom.vod.service.VideoVisitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 前端控制器
 * </p>
 *
 * @author simon
 * @since 2022-08-31
 */
@Api(tags = "课程统计接口")
@RestController
@RequestMapping("/admin/vod/video-visitor")
@CrossOrigin
public class VideoVisitorController {

    @Autowired
    private VideoVisitorService videoVisitorService;

    @ApiOperation("显示统计数据")
    @GetMapping("findCount/{courseId}/{startDate}/{endDate}")
    public Result findCount(@ApiParam("课程id") @PathVariable Long courseId,
                            @ApiParam("开始时间") @PathVariable String startDate,
                            @ApiParam("结束时间")@PathVariable String endDate){
        // 不知道返回什么，就返回一个map集合
        Map<String, Object> map = videoVisitorService.findCount(courseId, startDate, endDate);
        return Result.success(map);
    }
}

