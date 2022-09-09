package com.simon.classroom.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.classroom.vod.mapper.CourseMapper;
import com.simon.classroom.vod.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.model.vod.Course;
import com.simon.model.vod.CourseDescription;
import com.simon.model.vod.Subject;
import com.simon.model.vod.Teacher;
import com.simon.vo.vod.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-08-30
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CourseDescriptionService courseDescriptionService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private ChapterService chapterService;

    /**
     * 条件查询带分页的点播课程列表
     * @param pageParam
     * @param courseQueryVo
     * @return
     */
    @Override
    public Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        // 1、获得条件值
        Long teacherId = courseQueryVo.getTeacherId();  // 讲师
        Long subjectId = courseQueryVo.getSubjectId();  // 一级分类
        Long subjectParentId = courseQueryVo.getSubjectParentId();  // 二级分类
        String title = courseQueryVo.getTitle();        // 名称

        // 2、判断条件为空，封装条件
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(teacherId)){
            wrapper.eq("teacher_id",teacherId);
        }
        if (!StringUtils.isEmpty(subjectId)){
            wrapper.eq("subject_id",subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)){
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }

        // 3、调用方法实现条件查询分页
        Page<Course> pages  = baseMapper.selectPage(pageParam, wrapper);
        long totalCount = pages.getTotal();  // 总记录数
        long totalPage = pages.getPages();   // 总页数
        List<Course> records = pages.getRecords();  // 每页数据集合

        // 根据 讲师id 和 分类id（一层和二层） 获取对应的name
        // 使用stream流的方式进行遍历
        records.stream().forEach(item -> {
            this.getNameById(item);
        });

        // 4、封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",records);
        return map;
    }

    /**
     * 新增课程基本信息
     * @param courseFormVo
     * @return
     */
    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {
        // 保存课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo, course);
        baseMapper.insert(course);

        // 保存课程详情信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setCourseId(course.getId());
        courseDescription.setDescription(courseFormVo.getDescription());
        courseDescriptionService.save(courseDescription);
        return course.getId();
    }

    /**
     * 根据id获取课程基本信息对象
     * @param id
     * @return
     */
    @Override
    public CourseFormVo getCourseFormVoById(Long id) {
        CourseFormVo courseFormVo = new CourseFormVo();
        // 从course表中取数据
        Course course = baseMapper.selectById(id);
        if (course == null){
            return null;
        }
        BeanUtils.copyProperties(course,courseFormVo);

        // 从课程描述标中获得课程简介
        CourseDescription courseDescription = courseDescriptionService.queryByCourseId(course.getId());
        if (courseDescription != null){
            courseFormVo.setDescription(courseDescription.getDescription());
        }
        return courseFormVo;
    }

    /**
     * 修改课程基本对象
     * @param courseFormVo
     * @return
     */
    @Override
    public void updateCourseById(CourseFormVo courseFormVo) {
        // 修改课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        baseMapper.updateById(course);
        // 修改课程详情信息
        CourseDescription courseDescription = courseDescriptionService.queryByCourseId(course.getId());
        courseDescription.setDescription(courseFormVo.getDescription());
        courseDescription.setCourseId(course.getId());
        courseDescriptionService.updateById(courseDescription);
    }

    /**
     * 根据id获得课程发布信息
     * @param id
     * @return
     */
    @Override
    public CoursePublishVo getCoursePublishVoById(Long id) {
//        Course course = baseMapper.selectById(id);
//        CoursePublishVo coursePublishVo = new CoursePublishVo();
//        BeanUtils.copyProperties(course,coursePublishVo);
//        // 1、课程ID
//        coursePublishVo.setId(String.valueOf(course.getId()));
//        // 2、一级分类标题
//        Subject subjectParent = subjectService.getByParentId(course.getSubjectParentId());
//        coursePublishVo.setSubjectParentTitle(subjectParent.getTitle());
//        // 3、二级分类标题
//        Subject subject = subjectService.getById(course.getSubjectId());
//        coursePublishVo.setSubjectTitle(subject.getTitle());
//        // 4、讲师姓名
//        Teacher teacher = teacherService.getById(course.getTeacherId());
//        coursePublishVo.setTeacherName(teacher.getName());
//        // 5、课程价格
//        coursePublishVo.setPrice(String.valueOf(course.getPrice()));

        // 涉及三张表，多表查询，使用mapper文件实现
        // 注意要在yml配置文件中配置扫描mapper的位置，还要在pom.xml文件中配置扫描xml文件（或者将mapper文件放到resource目录下，我这里没有修改文件位置）
        // 否则会出现org.apache.ibatis.binding.BindingException: Invalid bound statement (not found): com.simon.classroom.vod.mapper.CourseMapper.coursePublishVoById
        // 绑定异常，要么是字段写错，要么是没扫描到mapper文件
        return baseMapper.getCoursePublishVoById(id);
    }

    /**
     * 根据id发布课程
     * @param id
     * @return
     */
    @Override
    public boolean publishCourseById(Long id) {
        Course course = new Course();
        course.setId(id);
        // 发布课程即添加发布时间，以及修改状态
        course.setPublishTime(new Date());
        course.setStatus(1);
        return this.updateById(course);
    }

    /**
     * 删除课程（从小的开始删除：小节=》章节=》课程描述=》课程基本信息）
     * @param id
     */
    @Override
    public void removeCourse(Long id) {
        // 1、删除课程对应的小节
        videoService.removeByCourseId(id);
        // 2、删除课程对应的章节
        chapterService.removeByCourseId(id);
        // 3、删除课程描述表
        courseDescriptionService.removeByCourseId(id);
        // 4、根据删除课程表中的课程
        baseMapper.deleteById(id);
    }

    /**
     * 根据ID查询课程
     * @param courseId
     * @return
     */
    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        // view_count 浏览数量+1
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);

        // 根据课程id查询
        // 课程详情数据（多表查询）
        CourseVo courseVo = baseMapper.selectCourseVoById(courseId);
        // 课程分类数据
        List<ChapterVo> chapterVoList = chapterService.getNestedTreeList(courseId);
        // 课程描述信息
        CourseDescription courseDescription = courseDescriptionService.queryByCourseId(courseId);
        // 课程所属讲师信息
        Teacher teacher = teacherService.getById(course.getTeacherId());

        // 封装map集合，返回
        Map<String, Object> map = new HashMap<>();
        map.put("courseVo", courseVo);
        map.put("chapterVoList", chapterVoList);
        map.put("description", null != courseDescription ? courseDescription.getDescription() : "");// 判断课程描述是否为空
        map.put("teacher", teacher);
        map.put("isBuy", false);//是否购买,后续完善
        return map;
    }

    /**
     * 查询所有课程以及讲师和分类名
     * @return
     */
    @Override
    public List<Course> findlist() {
        List<Course> list = baseMapper.selectList(null);
        list.stream().forEach(item -> {
            this.getNameById(item);
        });
        return list;
    }

    /**
     * 根据 讲师id 和 分类id（一层和二层） 获取对应的name
     * 在 BaseEntity 中有一个 Map集合 param，可以往里面存取数据库的表中没有的字段
     *     @ApiModelProperty(value = "其他参数")
     *     @TableField(exist = false)
     *     private Map<String,Object> param = new HashMap<>();
     * @param course
     */
    private Course getNameById(Course course) {
        // 查询讲师名称
        Teacher teacher = teacherService.getById(course.getTeacherId());
        if (teacher != null ){
            // 将查询出来的讲师名称和分类名称（一层和二层存入 map 集合中）
            course.getParam().put("teacherName", teacher.getName());
        }
        // 查询分类名称
        Subject subject = subjectService.getById(course.getSubjectId());
        if (subject != null){
            course.getParam().put("subjectTitle", subject.getTitle());
        }
        Subject subjectParent = subjectService.getById(course.getSubjectParentId());
        if (subjectParent != null) {
            course.getParam().put("subjectParentTitle",subjectParent.getTitle());
        }
        return course;
    }
}
