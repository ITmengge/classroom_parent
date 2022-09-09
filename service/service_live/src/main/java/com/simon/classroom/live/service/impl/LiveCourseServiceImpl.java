package com.simon.classroom.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.classroom.client.course.CourseFeignClient;
import com.simon.classroom.client.user.UserInfoFeignClient;
import com.simon.classroom.exception.ClassroomException;
import com.simon.classroom.live.mapper.LiveCourseMapper;
import com.simon.classroom.live.mtcloud.CommonResult;
import com.simon.classroom.live.mtcloud.MTCloud;
import com.simon.classroom.live.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.classroom.utils.DateUtil;
import com.simon.model.live.*;
import com.simon.model.user.UserInfo;
import com.simon.model.vod.Teacher;
import com.simon.vo.live.LiveCourseConfigVo;
import com.simon.vo.live.LiveCourseFormVo;
import com.simon.vo.live.LiveCourseGoodsView;
import com.simon.vo.live.LiveCourseVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * <p>
 * 直播课程表 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-09-07
 */
@Service
public class LiveCourseServiceImpl extends ServiceImpl<LiveCourseMapper, LiveCourse> implements LiveCourseService {

    @Autowired
    private MTCloud mtCloudClient;

    @Autowired
    private LiveCourseDescriptionService liveCourseDescriptionService;

    @Autowired
    private LiveCourseAccountService liveCourseAccountService;

    @Autowired
    private LiveCourseConfigService liveCourseConfigService;

    @Autowired
    private LiveCourseGoodsService liveCourseGoodsService;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    @Autowired
    private CourseFeignClient courseFeignClient;

    /**
     * 获得分页的直播课程列表
     * @param pageParam
     * @return
     */
    @Override
    public IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam) {
        IPage<LiveCourse> page = baseMapper.selectPage(pageParam, null);
        List<LiveCourse> liveCourseList = page.getRecords();
        List<Teacher> teacherList = courseFeignClient.getTeacherAll(); // 通过远程调用获得所有教师
        // 遍历直播课程列表，比对teacher_id获得teacher_name和teacher_level
        for (LiveCourse liveCourse:liveCourseList){
            for (Teacher teacher: teacherList){
                if (liveCourse.getTeacherId().equals(teacher.getId())){
                    liveCourse.getParam().put("teacherName",teacher.getName());
                    liveCourse.getParam().put("teacherLevel",teacher.getLevel());
                }
            }
        }
        return page;
    }

    /**
     * 直播课程添加
     * @param liveCourseFormVo
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void saveLive(LiveCourseFormVo liveCourseFormVo) {
        try {
            // 1、保存直播课程信息
            LiveCourse liveCourse = new LiveCourse();
            BeanUtils.copyProperties(liveCourseFormVo,liveCourse);

            // 2、保存直播账号信息
            // 2.1、根据讲师id获得讲师信息
            Teacher teacherLive = courseFeignClient.getTeacherLive(liveCourseFormVo.getTeacherId());

            // 2.2、调用方法添加直播课程
            // 创建map集合，封装直播课程其它参数
            HashMap<Object, Object> options = new HashMap<>();
            options.put("scenes", 2);//直播类型。1: 教育直播，2: 生活直播。默认 1，说明：根据平台开通的直播类型填写
            options.put("password", liveCourseFormVo.getPassword());
            // 欢拓云提供的MTCloud中的courseAdd添加直播方法有课程名称，直播账号（用讲师id），课程开始/结束时间，主播名字，主播介绍，hashmap集合存放其它参数
            String res = mtCloudClient.courseAdd(liveCourse.getCourseName(),
                                    teacherLive.getId().toString(),
                                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                                    teacherLive.getName(),
                                    teacherLive.getIntro(),
                                    options);
            System.out.println("直播信息：" + res);

            // 2.3、转换返回结果并判断创建直播是否成功
            // CommonResult工具类：结果转换，将string转换为json格式的数据
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {
                // 3、创建直播成功
                // 3.1、添加直播基本信息
                JSONObject object = commonResult.getData();
                Long course_id = object.getLong("course_id");   // 获得欢拓云返回的直播课程id
                liveCourse.setCourseId(course_id);
                baseMapper.insert(liveCourse);

                // 3.2、添加直播描述信息
                LiveCourseDescription liveCourseDescription = new LiveCourseDescription();
                liveCourseDescription.setLiveCourseId(liveCourse.getId());
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription());
                liveCourseDescriptionService.save(liveCourseDescription);

                // 3.3、添加直播账号信息
                LiveCourseAccount liveCourseAccount = new LiveCourseAccount();
                liveCourseAccount.setLiveCourseId(liveCourse.getId());
                liveCourseAccount.setZhuboAccount(object.getString("bid"));
                liveCourseAccount.setZhuboPassword(liveCourseFormVo.getPassword());
                liveCourseAccount.setAdminKey(object.getString("admin_key"));
                liveCourseAccount.setUserKey(object.getString("user_key"));
                liveCourseAccount.setZhuboKey(object.getString("zhubo_key"));
                liveCourseAccountService.save(liveCourseAccount);
            } else {
                System.out.println(commonResult.getmsg());
                throw new ClassroomException(20001,"创建直播课程失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 直播课程删除
     * @param id
     */
    @Override
    public void removeLive(Long id) {
        // 根据id查询直播课程信息
        LiveCourse liveCourse = baseMapper.selectById(id);
        if (liveCourse != null){
            // 获得直播课程id
            Long courseId = liveCourse.getCourseId();
            try {
                // 根据直播课程id删除直播即可
                mtCloudClient.courseDelete(courseId.toString());
                // 删除直播课程表数据
                baseMapper.deleteById(id);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassroomException(20001,"删除直播课程失败");
            }
        }
    }

    /**
     * 根据id查询直播课程基本信息和描述信息
     * @param id
     * @return
     */
    @Override
    public LiveCourseFormVo getLiveCourseFormVo(Long id) {
        LiveCourse liveCourse = baseMapper.selectById(id);
        LiveCourseFormVo liveCourseFormVo = new LiveCourseFormVo();
        // 通过直播课程id获得直播课程
        LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getByLiveCourseId(id);
        BeanUtils.copyProperties(liveCourse, liveCourseFormVo);
        liveCourseFormVo.setDescription(liveCourseDescription.getDescription());
        return liveCourseFormVo;
    }

    /**
     * 更新直播信息
     * @param liveCourseFormVo
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateById(LiveCourseFormVo liveCourseFormVo) {
        try {
            // 1、根据id获得直播课程基本信息
            LiveCourse liveCourse = baseMapper.selectById(liveCourseFormVo.getId());
            BeanUtils.copyProperties(liveCourseFormVo,liveCourse);

            // 2、更新直播账号信息
            // 2.1、根据讲师id获得讲师信息
            Teacher teacherLive = courseFeignClient.getTeacherLive(liveCourseFormVo.getTeacherId());

            // 2.2、调用方法添加直播课程
            // 创建map集合，封装直播课程其它参数（更新不需要其它参数，为空即可）
            HashMap<Object, Object> options = new HashMap<>();
            // 欢拓云提供的MTCloud中的courseUpdate添加直播方法有课程Id，直播账号（用讲师id），课程名称，课程开始/结束时间，主播名字，主播介绍，hashmap集合存放其它参数
            String res = mtCloudClient.courseUpdate(liveCourse.getCourseId().toString(),
                    teacherLive.getId().toString(),
                    liveCourse.getCourseName(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacherLive.getName(),
                    teacherLive.getIntro(),
                    options);
            System.out.println("直播信息：" + res);

            // 2.3、转换返回结果并判断创建直播是否成功
            // CommonResult工具类：结果转换，将string转换为json格式的数据
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {
                // 3、创建直播成功
                // 3.1、更新直播基本信息
                JSONObject object = commonResult.getData();
                Long course_id = object.getLong("course_id");   // 获得欢拓云返回的直播课程id
                liveCourse.setCourseId(course_id);
                baseMapper.updateById(liveCourse);

                // 3.2、更新直播描述信息
                LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getByLiveCourseId(liveCourse.getId());
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription());
                liveCourseDescriptionService.updateById(liveCourseDescription);

                // 3.3、更新直播账号信息（liveCourseFormVo.getPassword()没变，所以不需要更新）
//                LiveCourseAccount liveCourseAccount = new LiveCourseAccount();
//                liveCourseAccount.setLiveCourseId(liveCourse.getId());
//                liveCourseAccount.setZhuboAccount(object.getString("bid"));
//                liveCourseAccount.setZhuboPassword(liveCourseFormVo.getPassword());
//                liveCourseAccount.setAdminKey(object.getString("admin_key"));
//                liveCourseAccount.setUserKey(object.getString("user_key"));
//                liveCourseAccount.setZhuboKey(object.getString("zhubo_key"));
//                liveCourseAccountService.save(liveCourseAccount);
            } else {
                System.out.println(commonResult.getmsg());
                throw new ClassroomException(20001,"更新直播课程失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取直播课程的配置信息和商品列表
     * @param id
     * @return
     */
    @Override
    public LiveCourseConfigVo getCourseConfig(Long id) {
        LiveCourseConfigVo liveCourseConfigVo = new LiveCourseConfigVo();
        // 1、根据直播id获得直播课程的配置
        LiveCourseConfig liveCourseConfig = liveCourseConfigService.getByCourseId(id);
        if (liveCourseConfig != null){
            // 2、根据直播id获得商品列表
            List<LiveCourseGoods> liveCourseGoodsList = liveCourseGoodsService.getGoodsListByLiveCourseId(id);
            BeanUtils.copyProperties(liveCourseConfig,liveCourseConfigVo);
            liveCourseConfigVo.setLiveCourseGoodsList(liveCourseGoodsList);
        }
        return liveCourseConfigVo;
    }

    /**
     * 更新配置信息
     * @param liveCourseConfigVo
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateConfig(LiveCourseConfigVo liveCourseConfigVo) {
        // 1、更新数据库中的直播配置表
        LiveCourseConfig liveCourseConfig = new LiveCourseConfig();
        try {
            BeanUtils.copyProperties(liveCourseConfigVo,liveCourseConfig);
            if (liveCourseConfig.getId() == null){
                // 没有数据则添加
                liveCourseConfigService.save(liveCourseConfig);
            } else {
                // 有数据则更新
                liveCourseConfigService.updateById(liveCourseConfig);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2、更新数据库中的直播商品表（先删除再添加）
        // 2.1、根据课程id删除直播商品
        LambdaQueryWrapper<LiveCourseGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveCourseGoods::getLiveCourseId,liveCourseConfigVo.getLiveCourseId());
        liveCourseGoodsService.remove(wrapper);
        if (!CollectionUtils.isEmpty(liveCourseConfigVo.getLiveCourseGoodsList())){
            // 如果有更新商品列表,批量添加
            liveCourseGoodsService.saveBatch(liveCourseConfigVo.getLiveCourseGoodsList());
        }

        // 3、更新欢拓云直播平台的信息
        this.updateLifeConfig(liveCourseConfigVo);
    }

    /**
     * 获取最近的五条直播
     * @return
     */
    @Override
    public List<LiveCourseVo> findLatelyList() {
        List<LiveCourseVo>  liveCourseVoList = baseMapper.findLateLyList();
        for (LiveCourseVo liveCourseVo:liveCourseVoList){
            // 封装开始和结束时间
            liveCourseVo.setStartTimeString(new DateTime(liveCourseVo.getStartTime()).toString("yyyy年MM月dd HH:mm"));
            liveCourseVo.setEndTimeString(new DateTime(liveCourseVo.getEndTime()).toString("HH:mm"));

            // 封装讲师信息
            Long teacherId = liveCourseVo.getTeacherId();
            Teacher teacher = courseFeignClient.getTeacherLive(teacherId);
            liveCourseVo.setTeacher(teacher);

            // 封装直播状态 0：未开始 1：直播中 2：直播结束
            liveCourseVo.setLiveStatus(this.getLiveStatus(liveCourseVo));
        }
        return liveCourseVoList;
    }

    /**
     * 获取用户access_token
     * @param id
     * @param userId
     * @return
     */
    @Override
    public JSONObject getPlayAuth(Long id, Long userId) {
        // 根据直播课程id获取直播课程信息
        LiveCourse liveCourse = baseMapper.selectById(id);
        // 根据用户id获取用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);

        /**
         *  进入一个课程
         *  @param  String  course_id      课程ID
         *  @param  String  uid            用户唯一ID
         *  @param  String  nickname       用户昵称
         *  @param  String  role           用户角色，枚举见:ROLE 定义
         *  @param  Int     expire         有效期,默认:3600(单位:秒)
         *  @param  Array   options        可选项，包括:gender:枚举见上面GENDER定义,avatar:头像地址
         */
        HashMap<Object,Object> options = new HashMap<>();
        try {
             String res = mtCloudClient.courseAccess(liveCourse.getCourseId().toString(),
                                       userId.toString(),
                                       userInfo.getNickName(),
                                       MTCloud.ROLE_USER,
                                       3600,
                                       options);
             CommonResult<JSONObject> commonResult = JSONObject.parseObject(res, CommonResult.class);
             if (Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS){
                 JSONObject object = commonResult.getData();
                 System.out.println("access::" + object.getString("access_token"));
                 return object;
             } else {
                 throw new ClassroomException(20001,"获取access_token失败");
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据ID查询课程
     * @param courseId
     * @return
     */
    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        // 通过直播课程id获取直播课程信息
        LiveCourse liveCourse = this.getById(courseId);
        liveCourse.getParam().put("startTimeString", new DateTime(liveCourse.getStartTime()).toString("yyyy年MM月dd HH:mm"));
        liveCourse.getParam().put("endTimeString", new DateTime(liveCourse.getEndTime()).toString("yyyy年MM月dd HH:mm"));
        // 根据讲师id获取讲师
        Teacher teacher = courseFeignClient.getTeacherLive(liveCourse.getTeacherId());
        // 根据直播课程id获得直播课程描述信息
        LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getByLiveCourseId(courseId);

        Map<String, Object> map = new HashMap<>();
        map.put("liveCourse", liveCourse);
        map.put("liveStatus", this.getLiveStatus(liveCourse));
        map.put("teacher", teacher);
        if(null != liveCourseDescription) {
            map.put("description", liveCourseDescription.getDescription());
        } else {
            map.put("description", "");
        }
        return map;
    }

    /**
     * 封装直播状态 0：未开始 1：直播中 2：直播结束
     * @param liveCourse
     * @return
     */
    private int getLiveStatus(LiveCourse liveCourse) {
        int liveStatus = 0;
        Date curTime = new Date();
        if(DateUtil.dateCompare(curTime, liveCourse.getStartTime())) {
            liveStatus = 0;
        } else if(DateUtil.dateCompare(curTime, liveCourse.getEndTime())) {
            liveStatus = 1;
        } else {
            liveStatus = 2;
        }
        return liveStatus;
    }

    /**
     * 更新欢拓云直播平台的信息
     * @param liveCourseConfigVo
     */
    private void updateLifeConfig(LiveCourseConfigVo liveCourseConfigVo) {
        LiveCourse liveCourse = baseMapper.selectById(liveCourseConfigVo.getLiveCourseId());

        // 参数设置
        HashMap<Object,Object> options = new HashMap<Object, Object>();
        // 界面模式
        options.put("pageViewMode", liveCourseConfigVo.getPageViewMode());
        // 观看人数开关
        JSONObject number = new JSONObject();
        number.put("enable", liveCourseConfigVo.getNumberEnable());
        options.put("number", number.toJSONString());
        // 观看人数开关
        JSONObject store = new JSONObject();
        number.put("enable", liveCourseConfigVo.getStoreEnable());
        number.put("type", liveCourseConfigVo.getStoreType());
        options.put("store", number.toJSONString());
        // 商品列表
        List<LiveCourseGoods> liveCourseGoodsList = liveCourseConfigVo.getLiveCourseGoodsList();
        if(!CollectionUtils.isEmpty(liveCourseGoodsList)) {
            List<LiveCourseGoodsView> liveCourseGoodsViewList = new ArrayList<>();
            for(LiveCourseGoods liveCourseGoods : liveCourseGoodsList) {
                LiveCourseGoodsView liveCourseGoodsView = new LiveCourseGoodsView();
                try {
                    BeanUtils.copyProperties(liveCourseGoods, liveCourseGoodsView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                liveCourseGoodsViewList.add(liveCourseGoodsView);
            }
            JSONObject goodsListEdit = new JSONObject();
            goodsListEdit.put("status", "0");
            options.put("goodsListEdit ", goodsListEdit.toJSONString());
            options.put("goodsList", JSON.toJSONString(liveCourseGoodsViewList));
        }
        // 上面的不需要敲，能看懂就行，封装商品的信息用的
        // 更新欢拓云的直播配置，所需要的参数：直播id，配置（用hashmap集合接收）
        try {
            String res = mtCloudClient.updateLifeConfig(Integer.parseInt(liveCourse.getCourseId().toString()), options);
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) != MTCloud.CODE_SUCCESS) {
                throw new ClassroomException(20001,"更新配置信息失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
