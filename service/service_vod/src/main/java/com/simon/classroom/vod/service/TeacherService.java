package com.simon.classroom.vod.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.vod.Teacher;
import com.simon.vo.vod.TeacherQueryVo;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author simon
 * @since 2022-08-24
 */
public interface TeacherService extends IService<Teacher> {

    /**
     * 分页查询教师列表
     * @param pageParam
     * @param teacherQueryVo
     * @return
     */
    IPage<Teacher> pageQuery(IPage<Teacher> pageParam, TeacherQueryVo teacherQueryVo);

    /**
     * 获得所有教师
     * @return
     */
    List<Teacher> getTeacherAll();
}
