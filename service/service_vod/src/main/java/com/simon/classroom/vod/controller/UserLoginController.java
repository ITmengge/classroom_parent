package com.simon.classroom.vod.controller;

import com.simon.classroom.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录功能，根据前端框架，按F12查看登录访问了哪些接口，然后在本地创建相同接口，返回相同数据
 *
 *     跨域本质：浏览器对ajax请求的一种限制，三个部分（访问协议http/https、访问ip、端口号）都需要一致才行
 *     解决方案：
 *              1）在后端接口controller添加 @CrossOrigin 注解
 *              2）使用httpclient
 *              3）通过gateway网关
 */
@Api(tags = "用户登录接口")
@RestController
@RequestMapping("/admin/vod/user")
//@CrossOrigin    // 添加这个注解就能解决跨域问题
public class UserLoginController {

    /**
     * 登录功能
     * @return
     */
    @PostMapping("login")
    public Result login(){
        // {"code":20000,"data":{"token":"admin-token"}}
        Map<String, Object> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.success(map);
    }

    /**
     * 获得身份信息
     * @return
     */
    @GetMapping("info")
    public Result info(){
        // {"code":20000,
        //  "data":{
        //      "roles":["admin"],
        //      "introduction":"I am a super administrator",
        //      "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
        //      "name":"Super Admin"
        //  }
        // }
        Map<String, Object> map = new HashMap<>();
        map.put("roles","admin");
        map.put("introduction","I am a super administrator");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Super Admin");
        return Result.success(map);
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.success(null);
    }
}
