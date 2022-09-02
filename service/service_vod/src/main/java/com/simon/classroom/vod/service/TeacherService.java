package com.simon.classroom.vod.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.vod.Teacher;
import com.simon.vo.vod.TeacherQueryVo;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author simon
 * @since 2022-08-24
 */
public interface TeacherService extends IService<Teacher> {

    IPage<Teacher> pageQuery(IPage<Teacher> pageParam, TeacherQueryVo teacherQueryVo);

}
