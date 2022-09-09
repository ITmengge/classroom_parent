package com.simon.classroom.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.simon.classroom.result.Result;
import com.simon.classroom.vod.mapper.TeacherMapper;
import com.simon.classroom.vod.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.model.vod.Teacher;
import com.simon.vo.vod.TeacherQueryVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-08-24
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private TeacherService teacherService;

    /**
     * 分页查询
     * @param teacherQueryVo
     * @return
     */
    @Override
    public IPage<Teacher> pageQuery(IPage<Teacher> pageParam, TeacherQueryVo teacherQueryVo) {
        // 获取查询条件值
        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)) {
            queryWrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(joinDateBegin)) {
            queryWrapper.ge("join_date",joinDateBegin);
        }
        if (!StringUtils.isEmpty(joinDateEnd)) {
            queryWrapper.le("join_date",joinDateEnd);
        }
        // 查询结果
        IPage<Teacher> pageModel = teacherService.page(pageParam,queryWrapper);
        return pageModel;
    }

    /**
     * 获得所有教师
     * @return
     */
    @Override
    public List<Teacher> getTeacherAll() {
        List<Teacher> teacherList = baseMapper.selectList(null);
        return teacherList;
    }

}
