package com.simon.classroom.vod.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.classroom.result.Result;
import com.simon.classroom.vod.service.CourseService;
import com.simon.model.vod.Course;
import com.simon.model.vod.Teacher;
import com.simon.vo.vod.CourseFormVo;
import com.simon.vo.vod.CoursePublishVo;
import com.simon.vo.vod.CourseQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
@Api(tags = "课程管理接口")
@RestController
@RequestMapping("/admin/vod/course")
//@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 前端是使用data传的数据，所以要添加@RequestBody注解
    @ApiOperation(value = "添加课程基本信息")
    @PostMapping("save")
    public Result saveClass(@RequestBody CourseFormVo courseFormVo){
        Long courseId = courseService.saveCourseInfo(courseFormVo);
        // 返回新增的课程id，后面操作章节、小节、课程描述方便点
        return Result.success(courseId).message("保存成功");
    }

    // 前端是通过params传的数据，所以参数不需要添加@RequestBody注解
    @ApiOperation(value = "获得分页列表")
    @GetMapping("{page}/{limit}")
    public Result getCourseList(@PathVariable Long page, @PathVariable Long limit, CourseQueryVo courseQueryVo){
        Page<Course> pageParam = new Page<>(page,limit);
        // 由于不确定返回什么类型，一般返回map类型，取值存值方便
        Map<String,Object> map = courseService.findPage(pageParam,courseQueryVo);
        return Result.success(map);
    }

    @ApiOperation(value = "根据课程id获取课程基本信息")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id ){
        CourseFormVo courseFormVo = courseService.getCourseFormVoById(id);
        return Result.success(courseFormVo);
    }

    @ApiOperation(value = "修改课程基本信息")
    @PostMapping("update")
    public Result updateById(@RequestBody CourseFormVo courseFormVo){
        courseService.updateCourseById(courseFormVo);
        // 注意这里需要返回课程id，保存到下一步时传递参数到章节页面
        // 否则会出现 http://localhost:8301/admin/vod/chapter/getNestedTreeList/null，报空指针异常
        return Result.success(courseFormVo.getId()).message("修改成功");
    }

    @ApiOperation("根据id获得课程发布信息")
    @GetMapping("getCoursePublishVoById/{id}")
    public Result gerCoursePublishVo(@PathVariable Long id){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVoById(id);
        return Result.success(coursePublishVo);
    }

    @ApiOperation("根据id发布课程")
    @PostMapping("publishCourseById/{id}")
    public Result publishCourseById(@PathVariable Long id){
        boolean isSuccess = courseService.publishCourseById(id);
        if (isSuccess){
            return Result.success(null).message("发布成功");
        } else {
            return Result.fail(null).message("发布失败");
        }
    }

    @ApiOperation("删除课程")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        courseService.removeCourse(id);
        return Result.success(null);
    }

    @ApiOperation("查询所有课程以及讲师和分类名")
    @GetMapping("findAll")
    public Result findAll() {
        List<Course> list = courseService.findlist();
        return Result.success(list);
    }
}

