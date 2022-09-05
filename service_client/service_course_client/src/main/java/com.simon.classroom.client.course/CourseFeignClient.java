package com.simon.classroom.client.course;

import com.simon.model.vod.Course;
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
}
