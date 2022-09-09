package com.simon.classroom.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.simon.classroom.live.mapper.LiveCourseAccountMapper;
import com.simon.classroom.live.service.LiveCourseAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.model.live.LiveCourseAccount;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 直播课程账号表（受保护信息） 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-09-07
 */
@Service
public class LiveCourseAccountServiceImpl extends ServiceImpl<LiveCourseAccountMapper, LiveCourseAccount> implements LiveCourseAccountService {

    /**
     * 获取直播课程的账号信息
     * @param id
     * @return
     */
    @Override
    public LiveCourseAccount getByLiveCourseId(Long id) {
        LambdaQueryWrapper<LiveCourseAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveCourseAccount::getLiveCourseId,id);
        LiveCourseAccount liveCourseAccount = baseMapper.selectOne(wrapper);
        return liveCourseAccount;
    }
}
