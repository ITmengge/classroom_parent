package com.simon.classroom.vod.service;

import java.util.Map;

public interface VodService {
    /**
     * 上传视频（服务器端），客户端上传在前端，然后传一个courseId给后端即可
     * @return
     */
    String uploadVideo();

    /**
     * 删除视频
     * @param videoId
     */
    void removeVideo(String videoId);

    /**
     * 点播视频播放接口
     * @param courseId
     * @param videoId
     * @return
     */
    Map<String,Object> getPlayAuth(Long courseId, Long videoId);
}
