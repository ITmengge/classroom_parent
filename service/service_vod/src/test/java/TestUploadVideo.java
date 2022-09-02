import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.simon.classroom.exception.ClassroomException;
import com.simon.classroom.vod.utils.ConstantPropertiesUtil;

public class TestUploadVideo {
    public static void main(String[] args) {
        // 指定当前腾讯云账号id和key
        VodUploadClient client = new VodUploadClient(ConstantPropertiesUtil.ACCESS_KEY_ID,
                                                     ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        // 上传请求对象
        VodUploadRequest request = new VodUploadRequest();
        // 设置上传的视频路径
        request.setMediaFilePath("D:\\[WPF]JJDown\\Download\\test\\001.mp4");
        // 指定人物流
        request.setProcedure("LongVideoPreset");
        try {
            // 调用方法上传视频，指定地域
            VodUploadResponse response = client.upload("ap-guangzhou", request);
            // 获取上传之后的视频id
            String fileId = response.getFileId();
            System.out.println("fileId：" + fileId);
        } catch (Exception e) {
            // 业务方进行异常处理
            throw new ClassroomException(20001,"上传视频失败");
        }
    }
}
