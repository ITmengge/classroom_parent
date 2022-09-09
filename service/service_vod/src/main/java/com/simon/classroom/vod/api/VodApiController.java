package com.simon.classroom.vod.api;

import com.simon.classroom.result.Result;
import com.simon.classroom.vod.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "腾讯视频点播")
@RestController
@RequestMapping("/api/vod")
public class VodApiController {

    @Autowired
    private VodService vodService;

    @ApiOperation("点播视频播放接口")
    @GetMapping("getPlayAuth/{courseId}/{videoId}")
    public Result getPlayAuth(@PathVariable Long courseId,
                              @PathVariable Long videoId){
        Map<String,Object> map = vodService.getPlayAuth(courseId,videoId);
        return Result.success(map);
    }
}
