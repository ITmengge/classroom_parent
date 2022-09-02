package com.simon.classroom.vod.controller;


import com.simon.classroom.result.Result;
import com.simon.classroom.vod.service.ChapterService;
import com.simon.model.vod.Chapter;
import com.simon.vo.vod.ChapterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
@Api(tags = "章节管理接口")
@RestController
@RequestMapping("/admin/vod/chapter")
//@CrossOrigin
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    // 1、获得大纲列表（章节和小节）
    @ApiOperation("嵌套章节数据列表")
    @GetMapping("getNestedTreeList/{courseId}")
    public Result getNestedTreeList(@PathVariable Long courseId){
        List<ChapterVo> chapterVoList = chapterService.getNestedTreeList(courseId);
        return Result.success(chapterVoList);
    }

    // 2、添加章节
    @ApiOperation(value = "添加章节")
    @PostMapping("save")
    public Result save(@RequestBody Chapter chapter){
        chapterService.save(chapter);
        return Result.success(chapter.getCourseId()).message("添加成功");
    }

    // 3、根据id查询章节
    @ApiOperation(value = "根据id查询章节")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        Chapter chapter = chapterService.getById(id);
        return Result.success(chapter);
    }

    // 4、修改章节
    @ApiOperation(value = "修改章节")
    @PostMapping("update")
    public Result update(@RequestBody Chapter chapter){
        chapterService.updateById(chapter);
        return Result.success(chapter.getCourseId()).message("修改成功");
    }

    // 5、删除章节
    @ApiOperation(value = "删除章节")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        chapterService.removeByChapterId(id);
        return Result.success(null);
    }
}

