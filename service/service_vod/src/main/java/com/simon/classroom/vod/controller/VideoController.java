package com.simon.classroom.vod.controller;


import com.simon.classroom.result.Result;
import com.simon.classroom.vod.service.VideoService;
import com.simon.model.vod.Video;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
@Api(tags = "课程小节管理接口")
@RestController
@RequestMapping("/admin/vod/video")
@CrossOrigin
public class VideoController {

    @Autowired
    private VideoService videoService;

    @ApiOperation(value = "获取课程小节")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        Video video = videoService.getById(id);
        return Result.success(video);
    }

    @ApiOperation(value = "新增课程小节")
    @PostMapping("save")
    public Result save(@RequestBody Video video){
        videoService.save(video);
        return Result.success(null);
    }

    @ApiOperation(value = "修改课程小节")
    @PostMapping("update")
    public Result update(@RequestBody Video video){
        videoService.updateById(video);
        return Result.success(null);
    }

    @ApiOperation(value = "删除课程小节")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        videoService.removeVideoById(id);
        return Result.success(null);
    }
}

