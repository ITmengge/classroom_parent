package com.simon.classroom.vod.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.classroom.exception.ClassroomException;
import com.simon.classroom.result.Result;
import com.simon.classroom.vod.service.TeacherService;
import com.simon.model.vod.Teacher;
import com.simon.vo.vod.TeacherQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author simon
 * @since 2022-08-24
 */
@Api(tags = "讲师管理接口")
@RestController
@RequestMapping(value="/admin/vod/teacher")
//@CrossOrigin    // 添加这个注解就能解决跨域问题
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    // 1、查询所有讲师
//    @ApiOperation("查询所有讲师")
//    @GetMapping("findAll")
//    public List<Teacher> findAllTeacher() {
////        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<Teacher>().select();
//        List<Teacher> list = teacherService.list();
//        return list;
//    }
    // 1、查询所有讲师
    @ApiOperation("查询所有讲师")
    @GetMapping("findAll")
    public Result findAllTeacher() {
//        try {
//            int i = 10 / 0;
//        } catch (Exception e){
//            // 自定义异常需要手动抛出异常
//            throw new ClassroomException(201,"执行自定义异常处理ClassroomException");
//        }
//        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<Teacher>().select();
        List<Teacher> list = teacherService.list();
        return Result.success(list).message("查询数据成功");
    }

    // 2、逻辑删除讲师（使用 postman + swagger2 进行测试）
//    @ApiOperation("逻辑删除讲师")
//    @DeleteMapping("remove/{id}")
//    public boolean removeTeacher(@ApiParam(name = "id", value = "ID", required = true)
//                                     @PathVariable Long id ){
//        boolean isSuccess = teacherService.removeById(id);
//        return isSuccess;
//    }
    // 2、逻辑删除讲师（使用 postman + swagger2 进行测试）
    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("remove/{id}")
    public Result removeTeacher(@ApiParam(name = "id", value = "ID", required = true)
                                 @PathVariable Long id ){
        boolean isSuccess = teacherService.removeById(id);
        if (isSuccess) {
            return Result.success(null);
        } else {
            return Result.fail(null);
        }
    }

    // 3、条件查询分页（@RequestBody需要和post请求一起使用）
    @ApiOperation("条件查询分页")
    @PostMapping("findQueryPage/{current}/{limit}")
    public Result findPage(@PathVariable long current,
                           @PathVariable long limit,
                           @RequestBody(required = false) TeacherQueryVo teacherQueryVo) {
        // 创建 page 对象
        Page<Teacher> pageParam = new Page<>(current,limit);
        // 判断查询条件teacherQueryVo是否为空
        if (teacherQueryVo == null) {
            // 查询全部
            IPage<Teacher> pageModel = teacherService.page(pageParam,null);
            return Result.success(pageModel);
        } else {
            // 根据查询条件分页查询
            IPage<Teacher> pageModel = teacherService.pageQuery(pageParam, teacherQueryVo);
            return Result.success(pageModel);
        }
    }

    // 4、添加讲师
    @ApiOperation("添加讲师")
    @PostMapping("saveTeacher")
    public Result addTeacher(@RequestBody Teacher teacher){
        boolean isSuccess = teacherService.save(teacher);
        if (isSuccess) {
            return Result.success(null);
        } else {
            return Result.fail(null);
        }
    }

    // 5、获取讲师
    @ApiOperation("根据id查询讲师")
    @GetMapping("getTeacher/{id}")
    public Result getTeacher( @PathVariable Long id) {
        Teacher teacher = teacherService.getById(id);
        return Result.success(teacher);
    }

    // 6、修改讲师
    @ApiOperation("修改讲师")
    @PostMapping("updateTeacher")
    public Result updateTeacher(@RequestBody Teacher teacher){
        boolean isSuccess = teacherService.updateById(teacher);
        if (isSuccess) {
            return Result.success(null);
        } else {
            return Result.fail(null);
        }
    }

    // 7、批量删除讲师（前端返回json数组[1,2,3]，后端用list集合接收）
    @ApiOperation("批量删除讲师")
    @DeleteMapping("removeBatch")
    public Result removeBatch(@RequestBody List<Long> idList){
        boolean isSuccess = teacherService.removeByIds(idList);
        if (isSuccess) {
            return Result.success(null);
        } else {
            return Result.fail(null);
        }
    }

    @ApiOperation("根据teacherId获得teacher")
    @GetMapping("inner/getTeacher/{id}")
    public Teacher getTeacherLive(@PathVariable Long id){
        Teacher teacher = teacherService.getById(id);
        return teacher;
    }

    @ApiOperation("获得所有teacher")
    @GetMapping("inner/getTeacherAll")
    public List<Teacher> getTeacherAll(){
        List<Teacher> teacherList = teacherService.getTeacherAll();
        return teacherList;
    }

}

