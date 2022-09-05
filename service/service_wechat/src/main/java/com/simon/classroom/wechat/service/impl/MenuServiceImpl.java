package com.simon.classroom.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonArray;
import com.simon.classroom.exception.ClassroomException;
import com.simon.classroom.wechat.mapper.MenuMapper;
import com.simon.classroom.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simon.model.wechat.Menu;
import com.simon.vo.wechat.MenuVo;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author simon
 * @since 2022-09-03
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    // 注入微信配置类
    @Autowired
    private WxMpService wxMpService;

    /**
     * 获取所有菜单，按照一级和二级菜单封装
     * @return
     */
    @Override
    public List<MenuVo> findMenuInfo() {
        // 1、创建一个list集合，封装最终的数据返回
        List<MenuVo> menuVoList = new ArrayList<>();

        // 2、获得所有菜单
        List<Menu> menuList = baseMapper.selectList(null);

        // 3、从所有菜单数据中获取所有一级菜单数据(parent_id = 0)
        // 使用stream流遍历menuList，过滤出想要的数据，再转换成list集合
        List<Menu> oneMenuList = menuList.stream()
                                         .filter(menu -> menu.getParentId().longValue() == 0)
                                         .collect(Collectors.toList());

        // 4、封装一级菜单数据，封装到最终数据list集合
        for (Menu oneMenu:oneMenuList){
            // menu =》 menuVo
            MenuVo oneMenuVo = new MenuVo();
            BeanUtils.copyProperties(oneMenu,oneMenuVo);

            // 5、封装二级菜单数据（判断一级菜单的id和二级菜单的parent_id是否相同）
            // 如果相同，把二级菜单数据放到一级菜单里面
            List<Menu> twoMenuList = menuList.stream()
                                             .filter(menu -> menu.getParentId().longValue() == oneMenu.getId())
                                             .sorted(Comparator.comparing(Menu::getSort))       // 排序
                                             .collect(Collectors.toList());
            // 6、遍历二级菜单，List<Menu> =》 List<MenuVo>
            List<MenuVo> children = new ArrayList<>();
            for (Menu twoMenu:twoMenuList){
                // menu =》 menuVo
                MenuVo twoMenuVo = new MenuVo();
                BeanUtils.copyProperties(twoMenu, twoMenuVo);
                children.add(twoMenuVo);
            }

            // 7、把二级菜单放到一级菜单里
            oneMenuVo.setChildren(children);

            // 8、把一级菜单放到最终list集合并返回
            menuVoList.add(oneMenuVo);
        }
        return menuVoList;
    }

    /**
     * 获得一级菜单
     * @return
     */
    @Override
    public List<Menu> findOneMenuInfo() {
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("parent_id",0);
        List<Menu> menuList = baseMapper.selectList(menuQueryWrapper);
        return menuList;
    }

    /**
     * 同步菜单
     * 说明：
     * 自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
     * 一级菜单最多4个汉字，二级菜单最多8个汉字，多出来的部分将会以“...”代替。
     * 创建自定义菜单后，菜单的刷新策略是，在用户进入公众号会话页或公众号profile页时，如果发现上一次拉取菜单的请求在5分钟以前，就会拉取一下菜单，
     * 如果菜单有更新，就会刷新客户端的菜单。测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果。
     */
//    @SneakyThrows
    @Override
    public void createMenu() {
        //获取所有菜单数据
        List<MenuVo> menuVoList = this.findMenuInfo();
        //封装button里面结构，数组格式
        JSONArray buttonList = new JSONArray();
        for (MenuVo oneMenuVo:menuVoList) {
            //json对象  一级菜单
            JSONObject one = new JSONObject();
            one.put("name",oneMenuVo.getName());
            //json数组   二级菜单
            JSONArray subButton = new JSONArray();
            for (MenuVo twoMenuVo:oneMenuVo.getChildren()) {
                JSONObject view = new JSONObject();
                view.put("type", twoMenuVo.getType());
                if(twoMenuVo.getType().equals("view")) {
                    view.put("name", twoMenuVo.getName());
//                    view.put("url", "http://ggkt2.vipgz1.91tunnel.com/#"
//                    view.put("url", "http://classroom2.gz2vip.91tunnel.com/#" +twoMenuVo.getUrl());
                    // 跳转到8080端口，微信公众号前端页面
                    view.put("url", "http://classroom.free.idcfengye.com/#" +twoMenuVo.getUrl());
                    System.out.println(view.toJSONString());
                } else {
                    view.put("name", twoMenuVo.getName());
                    view.put("key", twoMenuVo.getMeunKey());
                }
                subButton.add(view);
            }
            one.put("sub_button",subButton);
            buttonList.add(one);
        }
        //封装最外层button部分
        JSONObject button = new JSONObject();
        button.put("button",buttonList);

        try {
            String menuId =
                    this.wxMpService.getMenuService().menuCreate(button.toJSONString());
            System.out.println("menuId"+menuId);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ClassroomException(20001,"公众号菜单同步失败");
        }
        /**
         *  菜单格式:
         *  {
         *      "button":[
         *      {
         *           "type":"click",
         *           "name":"今日歌曲",
         *           "key":"V1001_TODAY_MUSIC"
         *       },
         *       {
         *            "name":"菜单",
         *            "sub_button":[
         *            {
         *                "type":"view",
         *                "name":"搜索",
         *                "url":"http://www.soso.com/"
         *             },
         *             {
         *                  "type":"miniprogram",
         *                  "name":"wxa",
         *                  "url":"http://mp.weixin.qq.com",
         *                  "appid":"wx286b93c14bbf93aa",
         *                  "pagepath":"pages/lunar/index"
         *              },
         *             {
         *                "type":"click",
         *                "name":"赞一下我们",
         *                "key":"V1001_GOOD"
         *             }]
         *        }]
         *  }
         */
    }

    /**
     * 删除菜单
     */
//    @SneakyThrows
    @Override
    public void removeMenu() {
        try {
            this.wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ClassroomException(20001,"公众号菜单删除失败");
        }
    }
}
