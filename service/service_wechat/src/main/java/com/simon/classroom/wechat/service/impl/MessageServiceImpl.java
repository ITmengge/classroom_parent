package com.simon.classroom.wechat.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.simon.classroom.client.course.CourseFeignClient;
import com.simon.classroom.exception.ClassroomException;
import com.simon.classroom.wechat.service.MessageService;
import com.simon.model.vod.Course;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class MessageServiceImpl implements MessageService {

    // 注入远程调用的接口
    @Autowired
    private CourseFeignClient courseFeignClient;

    @Autowired
    private WxMpService wxMpService;

    /**
     * 接收消息
     * @param param
     * @return
     */
    @Override
    public String receiveMessage(Map<String, String> param) {
        String content = "";        // 要返回的最终消息
        try {
            String msgType = param.get("MsgType");      // 从参数中获得消息类型
            switch (msgType) {
                case "text":            // 普通文本类型，如输入关键字java，进行课程关键字查询
                content = this.search(param);
                    break;
                case "event":           // 关注/取消关注公众号 或 点击关于我们
                    String event = param.get("Event");
                    String eventKey = param.get("EventKey");
                    if ("subscribe".equals(event)) {             // 关注公众号
                        content = this.subscribe(param);
                    } else if ("unsubscribe".equals(event)) {    // 取消关注公众号
                        content = this.unsubscribe(param);
                    } else if ("CLICK".equals(event) && "aboutUs".equals(eventKey)) {    // 关于我们
                        // aboutUs是前端菜单key，也是数据库中的menu_key
                        content = this.aboutUs(param);
                    } else {
                        content = "success";
                    }
                    break;
                default:
                    content = "success";
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new ClassroomException(20001,"处理公众号消息失败");
        }
        return content;
    }

    /**
     * 模板消息，如订单支付成功的通知，这里先写死数据
     * @param orderId
     */
    @Override
    public void pushPayMessage(Long orderId) {
        // 微信openid
        String openid = "oWr9j6L8vpQtW1OlZA4M41UldeTs";
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openid)//要推送的用户openid
                .templateId("bnS-ecrP8UxzieyUiS6FNg2N0GYBbn9ytMl9wBOHNPI")//模板id
                .url("http://classroom2.gz2vip.91tunnel.com/#/pay/"+orderId)//点击模板消息要访问的网址
                .build();
        //3,如果是正式版发送消息，，这里需要配置你的信息
        templateMessage.addData(new WxMpTemplateData("first", "亲爱的用户：您有一笔订单支付成功。", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", "1314520", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword2", "java基础课程", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword3", "2022-01-11", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword4", "100", "#272727"));
        templateMessage.addData(new WxMpTemplateData("remark", "感谢你购买课程，如有疑问，随时咨询！", "#272727"));
        String msg = null;
        try {
            msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        System.out.println(msg);
    }

    /**
     * 关于我们
     * @param param
     * @return
     */
    private String aboutUs(Map<String, String> param) {
        return this.text(param, "硅谷课堂现开设Java、HTML5前端+全栈、大数据、全链路UI/UE设计、人工智能、大数据运维+Python自动化、Android+HTML5混合开发等多门课程；同时，通过视频分享、谷粒学苑在线课堂、大厂学苑直播课堂等多种方式，满足了全国编程爱好者对多样化学习场景的需求，已经为行业输送了大量IT技术人才。").toString();
    }

    /**
     * 处理关注事件
     * @param param
     * @return
     */
    private String subscribe(Map<String, String> param) {
        //处理业务
        return this.text(param, "感谢你关注“硅谷课堂”，可以根据关键字搜索您想看的视频教程，如：JAVA基础、Spring boot、大数据等").toString();
    }

    /**
     * 处理取消关注事件
     * @param param
     * @return
     */
    private String unsubscribe(Map<String, String> param) {
        //处理业务
        return "success";
    }

    /**
     * 返回消息的格式：
     * <xml>
     *     <ToUserName><![CDATA[toUser]]></ToUserName>
     *     <FromUserName><![CDATA[fromUser]]></FromUserName>
     *     <CreateTime>1348831860</CreateTime>
     *     <MsgType><![CDATA[text]]></MsgType>
     *     <Content><![CDATA[this is a test]]></Content>
     *     <MsgId>1234567890123456</MsgId>
     * </xml>
     *
     * | 参数          | 描述                      |
     * | :----------- | :----------------------- |
     * | ToUserName   | 开发者微信号               |
     * | FromUserName | 发送方帐号（一个OpenID）    |
     * | CreateTime   | 消息创建时间 （整型）       |
     * | MsgType      | 消息类型，文本为text       |
     * | Content      | 文本消息内容              |
     * | MsgId        | 消息id，64位整型          |
     */
    /**
     * 处理关键字搜索事件
     * 图文消息个数；当用户发送文本、图片、语音、视频、图文、地理位置这六种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     * @param param
     * @return
     */
    private String search(Map<String, String> param) {
        String fromusername = param.get("FromUserName");
        String tousername = param.get("ToUserName");
        String content = param.get("Content");
        //单位为秒，不是毫秒
        Long createTime = new Date().getTime() / 1000;
        StringBuffer text = new StringBuffer();
        // 远程调用，根据课程关键字查询课程
        List<Course> courseList = courseFeignClient.findCourseByKeyWord(content);
        if(CollectionUtils.isEmpty(courseList)) {
            text = this.text(param, "请重新输入关键字，没有匹配到相关视频课程");
        } else {
            //一次只能返回一个
            Random random = new Random();
            int num = random.nextInt(courseList.size());
            Course course = courseList.get(num);
            StringBuffer articles = new StringBuffer();
            articles.append("<item>");
            articles.append("<Title><![CDATA["+course.getTitle()+"]]></Title>");
            articles.append("<Description><![CDATA["+course.getTitle()+"]]></Description>");
            articles.append("<PicUrl><![CDATA["+course.getCover()+"]]></PicUrl>");
            articles.append("<Url><![CDATA[http://classroom2.gz2vip.91tunnel.com/#/liveInfo/"+course.getId()+"]]></Url>");
            articles.append("</item>");

            text.append("<xml>");
            text.append("<ToUserName><![CDATA["+fromusername+"]]></ToUserName>");
            text.append("<FromUserName><![CDATA["+tousername+"]]></FromUserName>");
            text.append("<CreateTime><![CDATA["+createTime+"]]></CreateTime>");
            text.append("<MsgType><![CDATA[news]]></MsgType>");
            text.append("<ArticleCount><![CDATA[1]]></ArticleCount>");
            text.append("<Articles>");
            text.append(articles);
            text.append("</Articles>");
            text.append("</xml>");
        }
        return text.toString();
    }

    /**
     * 回复文本
     * @param param
     * @param content
     * @return
     */
    private StringBuffer text(Map<String, String> param, String content) {
        String fromusername = param.get("FromUserName");
        String tousername = param.get("ToUserName");
        //单位为秒，不是毫秒
        Long createTime = new Date().getTime() / 1000;
        StringBuffer text = new StringBuffer();
        text.append("<xml>");
        text.append("<ToUserName><![CDATA["+fromusername+"]]></ToUserName>");
        text.append("<FromUserName><![CDATA["+tousername+"]]></FromUserName>");
        text.append("<CreateTime><![CDATA["+createTime+"]]></CreateTime>");
        text.append("<MsgType><![CDATA[text]]></MsgType>");
        text.append("<Content><![CDATA["+content+"]]></Content>");
        text.append("</xml>");
        return text;
    }

}
