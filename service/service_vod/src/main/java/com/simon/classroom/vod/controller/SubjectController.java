package com.simon.classroom.vod.controller;


import com.simon.classroom.result.Result;
import com.simon.classroom.vod.service.SubjectService;
import com.simon.model.vod.Subject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author simon
 * @since 2022-08-29
 */
@Api(tags = "课程分类管理")
@RestController
@RequestMapping("/admin/vod/subject")
//@CrossOrigin
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    /**
     * 获得课程分类列表，懒加载，每次查询一层数据
     * @param id
     * @return
     */
    @ApiOperation("查询下一层的课程分类")
    @GetMapping("getChildrenSubject/{id}")
    public Result getChildrenSubject(@PathVariable Long id){
        List<Subject> subjectList = subjectService.selectList(id);
        return Result.success(subjectList);
    }

    @ApiOperation("课程分类导出")
    @GetMapping("exportData")
    public void exportData(HttpServletResponse response ){
        subjectService.exportData(response);

    }

    @ApiOperation("课程分类导入")
    @PostMapping("importData")
    public Result importData(MultipartFile file){
        subjectService.importData(file);
        return Result.success(null);
    }
}

