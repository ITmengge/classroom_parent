package com.simon.classroom.client.course;

import com.simon.classroom.result.Result;
import com.simon.model.vod.Course;
import com.simon.model.vod.Teacher;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "service-vod")     // 为service-vod模块服务的
public interface CourseFeignClient {

    @ApiOperation("根据课程关键字查询课程")
    @GetMapping("/api/vod/course/inner/findByKeyword/{keyword}")
    List<Course> findCourseByKeyWord(@PathVariable String keyword);

    @ApiOperation("根据课程id查询课程")
    @GetMapping("/api/vod/course/inner/getById/{courseId}")
    Course getCourseById(@PathVariable Long courseId);

    @ApiOperation("根据teacherId获得teacher")
    @GetMapping("/admin/vod/teacher/inner/getTeacher/{id}")
    Teacher getTeacherLive(@PathVariable Long id);

    @ApiOperation("获得所有teacher")
    @GetMapping("/admin/vod/teacher/inner/getTeacherAll")
    List<Teacher> getTeacherAll();
}
