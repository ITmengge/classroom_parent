package com.simon.classroom.user.api;

import com.alibaba.fastjson.JSON;
import com.simon.classroom.user.service.UserInfoService;
import com.simon.classroom.utils.JwtHelper;
import com.simon.model.user.UserInfo;
import io.swagger.annotations.Api;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

/**
 * 获得微信授权信息并进行验证，验证成功再跳转
 */
@Controller // 这里要进行页面跳转，所以不用添加@RequestBody
@RequestMapping("/api/user/wechat")
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private UserInfoService userInfoService;

    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    /**
     * 授权跳转
     */
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl,
                            HttpServletRequest request){
//        System.out.println("returnUrl:" + returnUrl);
        String redirectURL = wxMpService.oauth2buildAuthorizationUrl(userInfoUrl,
                WxConsts.OAUTH2_SCOPE_USER_INFO,
                // 将路径中的simon转为#，注意不要跟内网穿透地址的路径一样了（如classroom），不然就会替换掉
                // 出现returnUrl:http://#.free.idcfengye.com/#/course/1
                URLEncoder.encode(returnUrl.replace("simon", "#")));
        return "redirect:" + redirectURL;
    }

    /**
     * 获得微信用户信息
     */
    @GetMapping("/userInfo")        // yml配置里的授权回调获取用户信息接口地址
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl){
//        System.out.println("走到userInfo这了");
        try {
            // 拿着code请求
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            // 获取openId
            String openId = wxMpOAuth2AccessToken.getOpenId();
            System.out.println("openId：" + openId);

            // 获取微信信息
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            System.out.println("wxMpUser:" + JSON.toJSONString(wxMpUser));

            // 获取微信信息添加到数据库
            UserInfo userInfo = userInfoService.getUserInfoByOpenid(openId);
            if (userInfo == null){
                userInfo = new UserInfo();
                userInfo.setOpenId(openId);
                userInfo.setNickName(wxMpUser.getNickname());
                userInfo.setUnionId(wxMpUser.getUnionId());
                userInfo.setAvatar(wxMpUser.getHeadImgUrl());
                userInfo.setProvince(wxMpUser.getProvince());
                userInfoService.save(userInfo);
            }
            System.out.println("returnUrl:" + returnUrl);
            // 授权完成之后，跳转具体功能页面
            // 生成token，按照一定规则生成字符串，可以包含用户信息（调用common下的service_utils下的生成token的工具类 ）
            String token = JwtHelper.createToken(userInfo.getId(), userInfo.getNickName());
            // 进行页面跳转时，判断路径是否还有其它参数，如localhost:8080/wexin?a=1&token=xxx
            if (returnUrl.indexOf("?") == -1){
                return "redirect:" + returnUrl + "?token=" + token;       // 没有其它参数
            } else {
                return "redirect:" + returnUrl + "&token=" + token;       // 有其它参数
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }
}
