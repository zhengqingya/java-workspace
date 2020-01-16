package com.zhengqing.demo.modules.weixin.service.impl;

import com.zhengqing.demo.modules.weixin.enumeration.EnumEventMessageType;
import com.zhengqing.demo.modules.weixin.enumeration.EnumRequestMessageType;
import com.zhengqing.demo.modules.weixin.enumeration.EnumResponseMessageType;
import com.zhengqing.demo.modules.weixin.model.vo.*;
import com.zhengqing.demo.modules.weixin.service.IMsgService;
import com.zhengqing.demo.utils.BeanUtil;
import com.zhengqing.demo.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p> 微信 - 消息处理 - 服务实现类$ </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/14$ 17:23$
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MsgServiceImpl implements IMsgService {

    @Override
    public String processRequest(HttpServletRequest request) {
        String respMessage = null;
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            // 回复文本消息
            TextMessageVO textMessage = new TextMessageVO();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(System.currentTimeMillis());
            textMessage.setMsgType(EnumResponseMessageType.TEXT.getType());

            // 默认返回的文本消息内容
            String respContent = "请求处理异常，请稍候重试！";

            // 事件推送
            if (EnumRequestMessageType.getEnum(msgType) == EnumRequestMessageType.EVENT) {
                // 事件类型
                String eventType = requestMap.get("Event");
                switch (EnumEventMessageType.getEnum(eventType)) {
                    // 订阅
                    case SUBSCRIBE:
                        respContent = "亲，感谢您的关注！";
                        break;
                    // 取消订阅
                    case UNSUBSCRIBE:
                        // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                        break;
                    // 自定义菜单点击事件
                    case CLICK:
                        // TODO 自定义菜单权没有开放，暂不处理该类消息
                        break;
                    default:
                        break;
                }
            } else {
                respContent = EnumRequestMessageType.getEnum(msgType).getTypeValue();
            }
            textMessage.setContent(respContent);
            respMessage = MessageUtil.textMessageToXml(textMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }

    @Override
    public String processRequestReturnSameMsg(HttpServletRequest request) {
        String respMessage = null;
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");

            // 回复消息
            BaseMessageVO baseMessageVO = new BaseMessageVO();
            baseMessageVO.setToUserName(fromUserName);
            baseMessageVO.setFromUserName(toUserName);
            baseMessageVO.setCreateTime(System.currentTimeMillis());

            switch (EnumRequestMessageType.getEnum(msgType)) {
                // 文本消息
                case TEXT:
                    TextMessageVO textMessageVO = BeanUtil.copyProperties(baseMessageVO, TextMessageVO.class);
                    textMessageVO.setContent(requestMap.get("Content"));
                    respMessage = MessageUtil.textMessageToXml(textMessageVO);
                    break;
                // 图片消息
                case IMAGE:
                    ImageMessageVO imageMessageVO = BeanUtil.copyProperties(baseMessageVO, ImageMessageVO.class);
                    ImageMessageVO.Image image = new ImageMessageVO.Image(requestMap.get("MediaId"));
                    imageMessageVO.setImage(image);
                    respMessage = MessageUtil.imageMessageToXml(imageMessageVO);
                    break;
                // 语音消息
                case VOICE:
                    VoiceMessageVO voiceMessageVO = BeanUtil.copyProperties(baseMessageVO, VoiceMessageVO.class);
                    VoiceMessageVO.Voice voice = new VoiceMessageVO.Voice(requestMap.get("MediaId"));
                    voiceMessageVO.setVoice(voice);
                    respMessage = MessageUtil.voiceMessageToXml(voiceMessageVO);
                    break;
                // TODO 视频消息 - 失败，原因未知！
                case VIDEO:
                    VideoMessageVO videoMessageVO = BeanUtil.copyProperties(baseMessageVO, VideoMessageVO.class);
                    VideoMessageVO.Video video = new VideoMessageVO.Video(requestMap.get("MediaId"), "视频消息", "这是一条视频消息");
                    videoMessageVO.setVideo(video);
                    respMessage = MessageUtil.videoMessageToXml(videoMessageVO);
                    break;
                // 事件推送
                case EVENT:
                    // 事件类型
                    String eventType = requestMap.get("Event");
                    switch (EnumEventMessageType.getEnum(eventType)) {
                        // 订阅
                        case SUBSCRIBE:
                            NewsMessageVO newsMessage = BeanUtil.copyProperties(baseMessageVO, NewsMessageVO.class);
                            NewsMessageVO.Article article = new NewsMessageVO.Article();
                            article.setTitle("感谢您的关注，这是一条图文消息！");
                            article.setDescription("mysql转oracle笔记");
                            article.setPicUrl("https://mmbiz.qpic.cn/mmbiz_png/iaUVVC0premhqE0TrtLzM6ABMIKKjnu81hraZvXia52byYMqADCyqXKwbs2wJ6jiadWc7MypLKL4EC5mUzXZKH2Rg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1");
                            article.setUrl("https://mp.weixin.qq.com/s?__biz=Mzg2NzEwMjc3Ng==&mid=2247483813&idx=1&sn=7a18081426d014ddccd203d33011f526&chksm=ce41f8f2f93671e4fc5d93292360fd24da4cf1434415befc37f25be2d40780b4a485653861d8&mpshare=1&scene=1&srcid=&sharer_sharetime=1579140222251&sharer_shareid=936076bf8d5bee83e89fd7e769b5c6db&key=c62bc26d01d4cb91d588b8abdeaca0fbba6d713fbc52e3b4c4a9f0377e231e0fe6b4ce07f287f509e37cefa17a0346475f12d85e21bcdbb8e953d0685018a874fbd80005417e94836ad9b0ff7559b334&ascene=1&uin=MTg4MzA0MzMxNA%3D%3D&devicetype=Windows+7&version=62070158&lang=zh_CN&exportkey=AYq%2FJJZv5hRr7YLluyVInZk%3D&pass_ticket=vCPgwidZSOs1xBfcd5SrzkCdVlApSWF7Xc%2BOzjYf8GlJ9%2BLQco9XYzTHe9yWHqc1");
                            newsMessage.addArticle(article);
                            respMessage = MessageUtil.newsMessageToXml(newsMessage);
                            break;
                        // 取消订阅
                        case UNSUBSCRIBE:
                            // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                            break;
                        // 自定义菜单点击事件
                        case CLICK:
                            // TODO 自定义菜单权没有开放，暂不处理该类消息
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }

}
