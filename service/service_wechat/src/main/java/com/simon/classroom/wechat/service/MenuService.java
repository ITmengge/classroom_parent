package com.simon.classroom.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simon.model.wechat.Menu;
import com.simon.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 服务类
 * </p>
 *
 * @author simon
 * @since 2022-09-03
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取所有菜单，按照一级和二级菜单封装
     * @return
     */
    List<MenuVo> findMenuInfo();

    /**
     * 获得一级菜单
     * @return
     */
    List<Menu> findOneMenuInfo();

    /**
     * 同步菜单
     */
    void createMenu();

    /**
     * 删除菜单
     */
    void removeMenu();
}
