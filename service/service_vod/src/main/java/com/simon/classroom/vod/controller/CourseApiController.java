package com.simon.classroom.vod.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.simon.classroom.result.Result;
import com.simon.classroom.vod.service.CourseService;
import com.simon.model.vod.Course;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "service-wechat远程调用service-vod的方法")
@RestController
@RequestMapping("/api/vod/course")
public class CourseApiController {

    @Autowired
    private CourseService courseService;

    @ApiOperation("根据课程关键字查询课程")
    @GetMapping("inner/findByKeyword/{keyword}")
    public List<Course> findCourseByKeyWord(@PathVariable String keyword){
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.like("title",keyword);
        List<Course> courseList = courseService.list(wrapper);
        return courseList;
    }
}
