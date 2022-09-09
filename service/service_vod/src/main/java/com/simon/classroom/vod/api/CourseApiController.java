package com.simon.classroom.vod.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.classroom.result.Result;
import com.simon.classroom.vod.service.CourseService;
import com.simon.model.vod.Course;
import com.simon.vo.vod.CourseQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @ApiOperation("根据课程分类查询课程列表")
    @GetMapping("{subjectParentId}/{page}/{limit}")
    public Result findPageCourse(@PathVariable Long subjectParentId,
                                 @PathVariable Long page,
                                 @PathVariable Long limit){
        // 封装条件，根据一级分类查询课程
        CourseQueryVo courseQueryVo = new CourseQueryVo();
        courseQueryVo.setSubjectParentId(subjectParentId);
        // 创建page对象查询
        Page<Course> pageParam = new Page<>(page,limit);
        Map<String,Object> map = courseService.findPage(pageParam, courseQueryVo);
        return Result.success(map);
    }

    @ApiOperation("根据ID查询课程（详细版）")
    @GetMapping("getInfo/{courseId}")
    public Result getInfo(@PathVariable Long courseId){
        Map<String,Object> map = courseService.getInfoById(courseId);
        return Result.success(map);
    }

    @ApiOperation("根据课程id查询课程")
    @GetMapping("inner/getById/{courseId}")
    public Course getCourseById(@PathVariable Long courseId){
        Course course = courseService.getById(courseId);
        return course;
    }
}
