package com.simon.classroom.vod.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.simon.classroom.exception.ClassroomException;
import com.simon.classroom.vod.listener.SubjectListener;
import com.simon.classroom.vod.mapper.SubjectMapper;
import com.simon.classroom.vod.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.model.vod.Subject;
import com.simon.vo.vod.SubjectEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-08-29
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Autowired
    private SubjectListener subjectListener;

    /**
     * 根据parent_id查询课程分类，获得课程分类列表，懒加载，每次查询一层数据
     * @param id
     * @return
     */
    @Override
    public List<Subject> selectList(Long id) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        List<Subject> subjectList = baseMapper.selectList(queryWrapper);
        // 遍历list集合
        for (Subject subject : subjectList){
            Long subjectId = subject.getId();
            // 查看再下一层是否有课程
            boolean isChild = this.isChildren(subjectId);
            subject.setHasChildren(isChild);
        }
        return subjectList;
//        /**
//         * 改进：不能循环查询数据库，先全部查询出来，然后再返回前端想要的数据
//         */
//        List<Subject> subjectList = selectAll();
//        List<Subject> subjects = new ArrayList<>();
//        for (Subject subject : subjectList){
//            if (subject.getId().equals(id)){
//
//            }
//        }
    }

    /**
     * 课程分类导出
     * @param response
     */
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            // 设置下载信息
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码，跟EasyExcel没有关系
            String fileName = URLEncoder.encode("课程分类","UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            // 查询课程分类表所有数据
            List<Subject> subjectList = baseMapper.selectList(null);
            // 将 List<Subject> 转变为 List<SubjectEeVo>
            List<SubjectEeVo> subjectEeVoList = new ArrayList<>();
            for(Subject subject : subjectList){
                SubjectEeVo subjectEeVo = new SubjectEeVo();
                BeanUtils.copyProperties(subject,subjectEeVo);
                subjectEeVoList.add(subjectEeVo);
            }
            // EasyExcel写操作
            EasyExcel.write(response.getOutputStream(), SubjectEeVo.class)
                    .sheet("课程分类")
                    .doWrite(subjectEeVoList);
        } catch (Exception e) {
            throw new ClassroomException(20001,"导出失败");
        }
    }

    /**
     * 课程分类导入
     * @param file
     */
    @Override
    public void importData(MultipartFile file) {
        try {
            // 注意这里不能直接 new 一个 SubjectListener()，要通过@Autowired注入的方式，让spring管理
//            EasyExcel.read(file.getInputStream(), SubjectEeVo.class, new SubjectListener()).sheet().doRead();
            EasyExcel.read(file.getInputStream(), SubjectEeVo.class, subjectListener).sheet().doRead();
        } catch (IOException e) {
            throw new ClassroomException(20001,"导入失败");
        }
    }

    /**
     * 通过parent_id查询对应的subject
     * @param subjectParentId
     * @return
     */
    @Override
    public Subject getByParentId(Long subjectParentId) {
        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subject::getId,subjectParentId);
        Subject subject = baseMapper.selectOne(queryWrapper);
        return subject;
    }

    /**
     * 判断id下面是否有子节点(前端根据 hasChildren 属性判断是否需要有下拉箭头)
     * @param id
     * @return
     */
    private boolean isChildren(Long id){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
