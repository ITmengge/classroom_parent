package com.simon.classroom.live.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.classroom.live.service.LiveCourseAccountService;
import com.simon.classroom.live.service.LiveCourseService;
import com.simon.classroom.result.Result;
import com.simon.model.live.LiveCourse;
import com.simon.model.live.LiveCourseAccount;
import com.simon.vo.live.LiveCourseConfigVo;
import com.simon.vo.live.LiveCourseFormVo;
import com.simon.vo.live.LiveCourseVo;
import io.lettuce.core.Limit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 直播课程表 前端控制器
 * </p>
 *
 * @author simon
 * @since 2022-09-07
 */
@Api(tags = "直播课程管理")
@RestController
@RequestMapping("/admin/live/liveCourse")
public class LiveCourseController {

    @Autowired
    private LiveCourseService liveCourseService;

    @Autowired
    private LiveCourseAccountService liveCourseAccountService;

    @ApiOperation("获得分页的直播课程列表")
    @GetMapping("{page}/{limit}")
    public Result getLiveCourse(@PathVariable Long page,
                                @PathVariable Long limit){
        Page<LiveCourse> pageParam = new Page<>(page,limit);
        IPage<LiveCourse> liveCourseList = liveCourseService.selectPage(pageParam);
        return Result.success(liveCourseList);
    }

    @ApiOperation("直播课程添加")
    @PostMapping("save")
    public Result save(@RequestBody LiveCourseFormVo liveCourseFormVo){
        liveCourseService.saveLive(liveCourseFormVo);
        return Result.success(null);
    }

    @ApiOperation("直播课程删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        liveCourseService.removeLive(id);
        return Result.success(null);
    }

    @ApiOperation("根据id查询直播课程基本信息")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        LiveCourse liveCourse = liveCourseService.getById(id);
        return Result.success(liveCourse);
    }

    @ApiOperation("根据id查询直播课程基本信息和描述信息")
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Long id){
        LiveCourseFormVo liveCourseFormVo = liveCourseService.getLiveCourseFormVo(id);
        return Result.success(liveCourseFormVo);
    }

    @ApiOperation("更新直播课程信息")
    @PutMapping("update")
    public Result updateById(@RequestBody LiveCourseFormVo liveCourseFormVo){
        liveCourseService.updateById(liveCourseFormVo);
        return Result.success(null);
    }

    @ApiOperation("获取直播课程的账号信息")
    @GetMapping("getLiveCourseAccount/{id}")
    public Result getLiveCourseAccount(@PathVariable Long id){
        LiveCourseAccount liveCourseAccount = liveCourseAccountService.getByLiveCourseId(id);
        return Result.success(liveCourseAccount);
    }

    @ApiOperation("获取直播课程的配置信息")
    @GetMapping("getCourseConfig/{id}")
    public Result getCourseConfig(@PathVariable Long id) {
        LiveCourseConfigVo liveCourseConfigVo = liveCourseService.getCourseConfig(id);
        return Result.success(liveCourseConfigVo);
    }

    @ApiOperation(value = "更新配置信息")
    @PutMapping("updateConfig")
    public Result updateConfig(@RequestBody LiveCourseConfigVo liveCourseConfigVo) {
        liveCourseService.updateConfig(liveCourseConfigVo);
        return Result.success(null);
    }

    @ApiOperation(value = "获取最近的五条直播")
    @GetMapping("findLatelyList")
    public Result findLatelyList() {
        List<LiveCourseVo> liveCourseVoList = liveCourseService.findLatelyList();
        return Result.success(liveCourseVoList);
    }


}

