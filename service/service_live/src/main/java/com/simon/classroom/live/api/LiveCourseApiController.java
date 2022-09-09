package com.simon.classroom.live.api;

import com.alibaba.fastjson.JSONObject;
import com.simon.classroom.live.service.LiveCourseService;
import com.simon.classroom.result.Result;
import com.simon.classroom.utils.AuthContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "公众号直播接口")
@RestController
@RequestMapping("/api/live/liveCourse")
public class LiveCourseApiController {

    @Autowired
    private LiveCourseService liveCourseService;

    @ApiOperation("获取用户access_token")
    @GetMapping("getPlayAuth/{id}")
    public Result getPlayAuth(@PathVariable Long id){
        // AuthContextHolder.getUserId()是用户授权登录后的信息，从信息中获取用户id
        JSONObject object = liveCourseService.getPlayAuth(id, AuthContextHolder.getUserId());
        return Result.success(object);
    }

    @ApiOperation("根据ID查询课程")
    @GetMapping("getInfo/{courseId}")
    public Result getInfo(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable Long courseId){
        Map<String, Object> map = liveCourseService.getInfoById(courseId);
        return Result.success(map);
    }
}
