package com.simon.classroom.vod.controller;

import com.simon.classroom.exception.ClassroomException;
import com.simon.classroom.result.Result;
import com.simon.classroom.vod.service.VodService;
import com.simon.classroom.vod.utils.ConstantPropertiesUtil;
import com.simon.classroom.vod.utils.Signature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

@Api(tags = "腾讯云点播接口")
@RestController
@RequestMapping("/admin/vod")
//@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    @ApiOperation("返回客户端上传视频签名")
    @GetMapping("sign")
    public Result sign(){
        // 代码在https://cloud.tencent.com/document/product/266/10638官网文档都有的
        Signature sign = new Signature();
        // 设置 App 的云 API 密钥
        sign.setSecretId(ConstantPropertiesUtil.ACCESS_KEY_ID);
        sign.setSecretKey(ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 24 * 2); // 签名有效期：2天
        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
            // 返回前面
            return Result.success(signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
            throw new ClassroomException(20001,"返回视频签名失败");
        }
    }

    @ApiOperation("上传视频接口（服务器端）")
    @PostMapping("upload")
    //@ApiParam(name = "file",value = "文件",required = true) MultipartFile file
    public Result upload(){
        String videoId = vodService.uploadVideo();
        return Result.success(videoId);
    }

    @ApiOperation("删除视频接口")
    @DeleteMapping("remove/{videoId}")
    public Result remove(@PathVariable String videoId){
        vodService.removeVideo(videoId);
        return Result.success(null);
    }
}
