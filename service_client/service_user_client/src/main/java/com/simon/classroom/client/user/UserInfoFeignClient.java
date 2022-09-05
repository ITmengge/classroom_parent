package com.simon.classroom.client.user;

import com.simon.model.user.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 此接口为 service_activity 在 service_user 获取用户信息而创建的
 * springcloud 里的 feign
 */
@FeignClient(value = "service-user")        // 指定要调用的接口所在的服务名
public interface UserInfoFeignClient {

    @GetMapping("/admin/user/userInfo/inner/getById/{id}")
    UserInfo getById(@PathVariable Long id);
}
