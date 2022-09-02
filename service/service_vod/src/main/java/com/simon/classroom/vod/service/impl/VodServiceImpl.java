package com.simon.classroom.vod.service.impl;

import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.simon.classroom.exception.ClassroomException;
import com.simon.classroom.vod.service.VodService;
import com.simon.classroom.vod.utils.ConstantPropertiesUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaResponse;
import org.springframework.stereotype.Service;

@Service
public class VodServiceImpl implements VodService {

    /**
     * 上传视频（服务器端），客户端上传在前端，然后子
     * @return
     */
    @Override
    public String uploadVideo() {
        // 指定当前腾讯云账号id和key
        VodUploadClient client = new VodUploadClient(ConstantPropertiesUtil.ACCESS_KEY_ID,
                                                     ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        // 上传请求对象
        VodUploadRequest request = new VodUploadRequest();
        // 设置上传的视频路径
//        request.setMediaFilePath("D:\\[WPF]JJDown\\Download\\test\\001.mp4");
        // 指定人物流
        request.setProcedure("LongVideoPreset");
        try {
            // 调用方法上传视频，指定地域
            VodUploadResponse response = client.upload("ap-guangzhou", request);
            // 获取上传之后的视频id
            String fileId = response.getFileId();
            // 将视频id存入video中的video_source_id
            return fileId;
        } catch (Exception e) {
            // 业务方进行异常处理
            throw new ClassroomException(20001,"上传视频失败");
        }
    }

    /**
     * 删除视频
     * 在线生成代码：https://console.cloud.tencent.com/api/explorer?Product=vod&Version=2018-07-17&Action=DescribeMediaInfos&SignVersion=
     * @param videoId
     */
    @Override
    public void removeVideo(String videoId) {
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(ConstantPropertiesUtil.ACCESS_KEY_ID,
                                             ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            VodClient client = new VodClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DeleteMediaRequest req = new DeleteMediaRequest();
            req.setFileId(videoId);
            // 返回的resp是一个DeleteMediaResponse的实例，与请求对象对应
            DeleteMediaResponse resp = client.DeleteMedia(req);
            // 输出json格式的字符串回包
            System.out.println(DeleteMediaResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            throw new ClassroomException(20001,"删除视频失败");
        }
    }
}
