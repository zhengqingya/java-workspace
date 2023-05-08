package com.zhengqing.demo.kdniao.util;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.zhengqing.demo.kdniao.model.dto.KdniaoApiDTO;
import com.zhengqing.demo.kdniao.model.vo.KdniaoApiVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;

/**
 * <p> 快递鸟工具类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/25 17:24
 */
@Slf4j
public class KdniaoUtil {

    /**
     * 快递查询接口
     *
     * @param queryDTO 请求参数
     * @return 物流信息
     * @author zhengqingya
     * @date 2021/10/25 17:39
     */
    public static KdniaoApiVO getLogisticInfo(KdniaoApiDTO queryDTO) {
        KdniaoApiVO kdniaoApiVO = new KdniaoUtil().getLogisticBase(queryDTO);
        Assert.isTrue("true".equals(kdniaoApiVO.getSuccess()), kdniaoApiVO.getReason());
        kdniaoApiVO.handleData();
        return kdniaoApiVO;
    }

    /**
     * 快递查询接口
     *
     * @param queryDTO 请求参数
     * @return 物流信息
     * @author zhengqingya
     * @date 2021/10/25 17:39
     */
    @SneakyThrows(Exception.class)
    private KdniaoApiVO getLogisticBase(KdniaoApiDTO queryDTO) {
        String EBusinessID = queryDTO.getEBusinessID();
        String ApiKey = queryDTO.getApiKey();
        String ReqURL = queryDTO.getReqURL();
        String shipperCode = queryDTO.getShipperCode();
        String logisticCode = queryDTO.getLogisticCode();

        // 组装应用级参数
        Map<String, String> requestParamMap = Maps.newHashMap();
        requestParamMap.put("shipperCode", shipperCode);
        requestParamMap.put("LogisticCode", logisticCode);
        String RequestData = JSON.toJSONString(requestParamMap);
        // 组装系统级参数
        Map<String, String> params = Maps.newHashMap();
        params.put("RequestData", this.urlEncoder(RequestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "8001");
        String dataSign = this.encrypt(RequestData, ApiKey, "UTF-8");
        params.put("DataSign", this.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        // 以form表单形式提交post请求，post请求体中包含了应用级参数和系统级参数
        String resultJson = this.sendPost(ReqURL, params);
        return JSON.parseObject(resultJson, KdniaoApiVO.class);
    }

    /**
     * MD5加密
     * str 内容
     * charset 编码方式
     *
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private String MD5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < result.length; i++) {
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    /**
     * base64编码
     * str 内容
     * charset 编码方式
     *
     * @throws UnsupportedEncodingException
     */
    private String base64(String str, String charset) throws UnsupportedEncodingException {
        String encoded = Base64.encode(str.getBytes(charset));
        return encoded;
    }

    @SuppressWarnings("unused")
    private String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
        String result = URLEncoder.encode(str, charset);
        return result;
    }

    /**
     * 电商Sign签名生成
     * content 内容
     * keyValue ApiKey
     * charset 编码方式
     *
     * @return DataSign签名
     * @throws UnsupportedEncodingException ,Exception
     */
    @SuppressWarnings("unused")
    private String encrypt(String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception {
        if (keyValue != null) {
            return base64(MD5(content + keyValue, charset), charset);
        }
        return base64(MD5(content, charset), charset);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * url 发送请求的 URL
     * params 请求的参数集合
     *
     * @return 远程资源的响应结果
     */
    @SuppressWarnings("unused")
    private String sendPost(String url, Map<String, String> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (param.length() > 0) {
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                }
                log.info("[快递鸟] 请求参数: [{}]", param);
                out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

}
