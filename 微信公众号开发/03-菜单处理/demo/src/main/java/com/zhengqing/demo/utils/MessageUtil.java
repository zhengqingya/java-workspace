package com.zhengqing.demo.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.zhengqing.demo.modules.weixin.model.message.vo.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 消息工具类：对请求消息、响应消息建立与之对应的Java类、对xml消息进行解析、将响应消息的Java对象转换成xml </p>
 *
 * @author : zhengqing
 * @description : 所需引入相关依赖：
 *                          dom4j、jaxen：解析微信发来的请求（XML）
 *                          xstream：将响应消息转换成xml返回
 * @date : 2020/1/14 17:05
 */
public class MessageUtil {

    /**
     * 解析微信发来的请求（XML）
     *
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<>(10);

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }

        // 释放资源
        inputStream.close();
        inputStream = null;
        return map;
    }

    /**
     * 文本消息对象转换成xml
     *
     * @param textMessageVO 文本消息对象   XStream是一个Java对象和XML相互转换的工具
     * @return xml
     */
    public static String textMessageToXml(TextMessageVO textMessageVO) {
        xstream.alias("xml", textMessageVO.getClass());
        return xstream.toXML(textMessageVO);
    }

    /**
     * 图片消息对象转换成xml
     *
     * @param imageMessageVO 图片消息对象
     * @return xml
     */
    public static String imageMessageToXml(ImageMessageVO imageMessageVO) {
        xstream.alias("xml", imageMessageVO.getClass());
        return xstream.toXML(imageMessageVO);
    }

    /**
     * 语音消息对象转换成xml
     *
     * @param voiceMessageVO 语音消息对象
     * @return xml
     */
    public static String voiceMessageToXml(VoiceMessageVO voiceMessageVO) {
        xstream.alias("xml", voiceMessageVO.getClass());
        return xstream.toXML(voiceMessageVO);
    }

    /**
     * 视频消息对象转换成xml
     *
     * @param videoMessageVO 视频消息对象
     * @return xml
     */
    public static String videoMessageToXml(VideoMessageVO videoMessageVO) {
        xstream.alias("xml", videoMessageVO.getClass());
        return xstream.toXML(videoMessageVO);
    }

    /**
     * 音乐消息对象转换成xml
     *
     * @param musicMessage 音乐消息对象
     * @return xml
     */
    public static String musicMessageToXml(MusicMessageVO musicMessage) {
        xstream.alias("xml", musicMessage.getClass());
        return xstream.toXML(musicMessage);
    }

    /**
     * 图文消息对象转换成xml
     *
     * @param newsMessage 图文消息对象
     * @return xml
     */
    public static String newsMessageToXml(NewsMessageVO newsMessage) {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", NewsMessageVO.Article.class);
        return xstream.toXML(newsMessage);
    }

    /**
     * 扩展xstream，使其支持CDATA块
     */
    private static XStream xstream = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @Override
                @SuppressWarnings("unchecked")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

}
