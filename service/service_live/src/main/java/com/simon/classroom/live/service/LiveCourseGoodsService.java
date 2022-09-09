package com.simon.classroom.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.live.LiveCourseGoods;

import java.util.List;

/**
 * <p>
 * 直播课程关联推荐表 服务类
 * </p>
 *
 * @author simon
 * @since 2022-09-07
 */
public interface LiveCourseGoodsService extends IService<LiveCourseGoods> {

    /**
     * 根据直播id获得商品列表
     * @param id
     * @return
     */
    List<LiveCourseGoods> getGoodsListByLiveCourseId(Long id);
}
