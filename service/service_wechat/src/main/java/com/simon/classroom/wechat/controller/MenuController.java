package com.simon.classroom.wechat.controller;


import com.alibaba.fastjson.JSONObject;
import com.simon.classroom.exception.ClassroomException;
import com.simon.classroom.result.Result;
import com.simon.classroom.wechat.service.MenuService;
import com.simon.classroom.wechat.utils.ConstantPropertiesUtil;
import com.simon.classroom.wechat.utils.HttpClientUtils;
import com.simon.model.wechat.Menu;
import com.simon.vo.wechat.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 前端控制器
 * </p>
 *
 * @author simon
 * @since 2022-09-03
 */
@Api(tags = "微信公众号菜单管理")
@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 同步微信公众号菜单实现：1、调用微信接口，获取access_token，需要有公众号的appId和api秘钥
     * @return
     */
    @ApiOperation("获取access_token")
    @GetMapping("getAccessToken")
    public Result getAccessToken(){
        try {
            // 拼接请求地址
            StringBuffer buffer = new StringBuffer();
            buffer.append("https://api.weixin.qq.com/cgi-bin/token");
            buffer.append("?grant_type=client_credential");
            buffer.append("&appid=%s");
            buffer.append("&secret=%s");
            // 请求地址设置参数
            String url = String.format(buffer.toString(),
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            // 发送http请求
            String tokenString = HttpClientUtils.get(url);
            // 获取access_token
            JSONObject jsonObject = JSONObject.parseObject(tokenString);
            String access_token = jsonObject.getString("access_token");
            // 返回
            return Result.success(access_token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassroomException(20001,"获取access_token失败");
        }
    }

    /**
     *  2、拿着access_token调用微信接口，实现公众号菜单同步
     * @return
     */
    @ApiOperation("同步菜单")
    @GetMapping("syncMenu")
    public Result syncMenu(){
        menuService.createMenu();
        return Result.success(null);
    }

    @ApiOperation("删除菜单")
    @DeleteMapping("removeMenu")
    public Result removeMenu(){
        menuService.removeMenu();
        return Result.success(null);
    }

    @ApiOperation("获取所有菜单，按照一级和二级菜单封装")
    @GetMapping("findMenuInfo")
    public Result findMenuInfo(){
        List<MenuVo> menuVoList = menuService.findMenuInfo();
        return Result.success(menuVoList);
    }

    @ApiOperation("获得一级菜单")
    @GetMapping("findOneMenuInfo")
    public Result findOneMenuInfo(){
        List<Menu> menuList = menuService.findOneMenuInfo();
        return Result.success(menuList);
    }

    @ApiOperation("根据id获取菜单")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    @ApiOperation("新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody Menu menu){
        menuService.save(menu);
        return Result.success(null);
    }

    @ApiOperation("修改菜单")
    @PutMapping("update")
    public Result update(@RequestBody Menu menu){
        menuService.updateById(menu);
        return Result.success(null);
    }

    @ApiOperation("根据id删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        menuService.removeById(id);
        return Result.success(null);
    }

    @ApiOperation("删除菜单列表")
    @DeleteMapping("batchRemove")
    public Result batchRemove(List<Long> idList){
        menuService.removeByIds(idList);
        return Result.success(null);
    }

}

