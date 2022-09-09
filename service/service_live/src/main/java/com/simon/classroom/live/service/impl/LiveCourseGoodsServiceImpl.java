package com.simon.classroom.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.simon.classroom.live.mapper.LiveCourseGoodsMapper;
import com.simon.classroom.live.service.LiveCourseGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.model.live.LiveCourseGoods;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 直播课程关联推荐表 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-09-07
 */
@Service
public class LiveCourseGoodsServiceImpl extends ServiceImpl<LiveCourseGoodsMapper, LiveCourseGoods> implements LiveCourseGoodsService {

    /**
     * 根据直播id获得商品列表
     * @param id
     * @return
     */
    @Override
    public List<LiveCourseGoods> getGoodsListByLiveCourseId(Long id) {
        LambdaQueryWrapper<LiveCourseGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveCourseGoods::getLiveCourseId,id);
        List<LiveCourseGoods> liveCourseGoodsList = baseMapper.selectList(wrapper);
        return liveCourseGoodsList;
    }
}
