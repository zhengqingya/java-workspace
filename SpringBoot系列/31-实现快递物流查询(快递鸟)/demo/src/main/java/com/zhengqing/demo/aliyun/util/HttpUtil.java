package com.zhengqing.demo.aliyun.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * HttpClient 工具类
 * </p>
 *
 * @author zhengqingya
 * @description 实现 get、post、put、delete 以及上传、下载请求 (采用单利模式来初始化客户端，并用线程池来管理，同时支持http和https协议，项目启动之后，无需手动关闭httpClient客户端!)
 * 除了上传、下载请求之外，默认封装的请求参数格式都是application/json，其中`sendHttp`是一个支持GET、POST、PUT、DELETE请求的通用方法...
 * @date 2021/2/1 14:14
 */
@Slf4j
public class HttpUtil {

    private HttpUtil() {
    }

    // 多线程共享实例
    private static CloseableHttpClient httpClient;

    static {
        SSLContext sslContext = createSSLContext();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        // 注册http套接字工厂和https套接字工厂
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslsf).build();
        // 连接池管理器
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(300);// 连接池最大连接数
        connMgr.setDefaultMaxPerRoute(300);// 每个路由最大连接数，设置的过小，无法支持大并发
        connMgr.setValidateAfterInactivity(5 * 1000); // 在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证
        // 请求参数配置管理器
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(60000)
                .setConnectionRequestTimeout(60000).build();
        // 获取httpClient客户端
        httpClient = HttpClients.custom().setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * GET请求
     *
     * @param url
     * @return
     */
    public static String getUrl(String url) {
        return sendHttp(HttpMethod.GET, url, null, null);
    }

    /**
     * GET请求/带头部的信息
     *
     * @param url
     * @param header
     * @return
     */
    public static String getUrl(String url, Map<String, String> header) {
        return sendHttp(HttpMethod.GET, url, header, null);
    }

    /**
     * POST请求/无参数
     *
     * @param url
     * @return
     */
    public static String postJson(String url) {
        return postJson(url, null, null);
    }

    /**
     * POST请求/有参数
     *
     * @param url
     * @param param
     * @return
     */
    public static String postJson(String url, String param) {
        return postJson(url, null, param);
    }

    /**
     * POST请求/无参数带头部
     *
     * @param url
     * @param header
     * @return
     */
    public static String postJson(String url, Map<String, String> header) {
        return postJson(url, header, null);
    }

    /**
     * POST请求/有参数带头部
     *
     * @param url
     * @param header
     * @param params
     * @return
     */
    public static String postJson(String url, Map<String, String> header, String params) {
        return sendHttp(HttpMethod.POST, url, header, params);
    }

    /**
     * 上传文件流
     *
     * @param url
     * @param header
     * @param param
     * @param fileName
     * @param inputStream
     * @return
     */
    public static RequestResult postUploadFileStream(String url, Map<String, String> header, Map<String, String> param,
                                                     String fileName, InputStream inputStream) {
        byte[] stream = inputStream2byte(inputStream);
        return postUploadFileStream(url, header, param, fileName, stream);
    }

    /**
     * 上传文件
     *
     * @param url      上传地址
     * @param header   请求头部
     * @param param    请求表单
     * @param fileName 文件名称
     * @param stream   文件流
     * @return
     */
    public static RequestResult postUploadFileStream(String url, Map<String, String> header, Map<String, String> param,
                                                     String fileName, byte[] stream) {
        String infoMessage = new StringBuilder().append("request postUploadFileStream，url:").append(url)
                .append("，header:").append(header.toString()).append("，param:").append(JSONObject.toJSONString(param))
                .append("，fileName:").append(fileName).toString();
        log.info(infoMessage);
        RequestResult result = new RequestResult();
        if (StringUtils.isBlank(fileName)) {
            log.warn("上传文件名称为空");
            throw new RuntimeException("上传文件名称为空");
        }
        if (Objects.isNull(stream)) {
            log.warn("上传文件流为空");
            throw new RuntimeException("上传文件流为空");
        }
        try {
            ContentType contentType = ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8");
            HttpPost httpPost = new HttpPost(url);
            if (Objects.nonNull(header) && !header.isEmpty()) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                    if (log.isDebugEnabled()) {
                        log.debug(entry.getKey() + ":" + entry.getValue());
                    }
                }
            }
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("UTF-8"));// 使用UTF-8
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 设置浏览器兼容模式
            if (Objects.nonNull(param) && !param.isEmpty()) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    if (log.isDebugEnabled()) {
                        log.debug(entry.getKey() + ":" + entry.getValue());
                    }
                    builder.addPart(entry.getKey(), new StringBody(entry.getValue(), contentType));
                }
            }
            builder.addBinaryBody("file", stream, contentType, fileName);// 封装上传文件
            httpPost.setEntity(builder.build());
            // 请求执行，获取返回对象
            CloseableHttpResponse response = httpClient.execute(httpPost);
            log.info("postUploadFileStream response status:{}", response.getStatusLine());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_NO_CONTENT) {
                result.setSuccess(true);
            }
            HttpEntity httpEntity = response.getEntity();
            if (Objects.nonNull(httpEntity)) {
                String content = EntityUtils.toString(httpEntity, "UTF-8");
                log.info("postUploadFileStream response body:{}", content);
                result.setMsg(content);
            }
            HttpClientUtils.closeQuietly(response);
        } catch (Exception e) {
            log.error(infoMessage + " failure", e);
            result.setMsg("请求异常");
        }
        return result;
    }

    /**
     * 从下载地址获取文件流(如果链接出现双斜杠，请用OKHttp)
     *
     * @param url
     * @return
     */
    public static ByteArrayOutputStream getDownloadFileStream(String url) {
        String infoMessage = new StringBuilder().append("request getDownloadFileStream，url:").append(url).toString();
        log.info(infoMessage);
        ByteArrayOutputStream byteOutStream = null;
        try {
            CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
            log.info("getDownloadFileStream response status:{}", response.getStatusLine());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                // 请求成功
                HttpEntity entity = response.getEntity();
                if (entity != null && entity.getContent() != null) {
                    // 复制输入流
                    byteOutStream = cloneInputStream(entity.getContent());
                }
            }
            HttpClientUtils.closeQuietly(response);
        } catch (Exception e) {
            log.error(infoMessage + " failure", e);
        }
        return byteOutStream;
    }

    /**
     * 发送http请求(通用方法)
     *
     * @param httpMethod 请求方式（GET、POST、PUT、DELETE）
     * @param url        请求路径
     * @param header     请求头
     * @param params     请求body（json数据）
     * @return 响应文本
     */
    public static String sendHttp(HttpMethod httpMethod, String url, Map<String, String> header, String params) {
        String infoMessage =
                new StringBuilder().append("request sendHttp，url:").append(url).append("，method:").append(httpMethod.name())
                        .append("，header:").append(JSONObject.toJSONString(header)).append("，param:").append(params).toString();
        log.info(infoMessage);
        // 返回结果
        String result = null;
        long beginTime = System.currentTimeMillis();
        try {
            ContentType contentType = ContentType.APPLICATION_JSON.withCharset("UTF-8");
            HttpRequestBase request = buildHttpMethod(httpMethod, url);
            if (Objects.nonNull(header) && !header.isEmpty()) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    // 打印头部信息
                    if (log.isDebugEnabled()) {
                        log.debug(entry.getKey() + ":" + entry.getValue());
                    }
                    request.setHeader(entry.getKey(), entry.getValue());
                }
            }
            if (StringUtils.isNotEmpty(params)) {
                if (HttpMethod.POST.equals(httpMethod) || HttpMethod.PUT.equals(httpMethod)) {
                    ((HttpEntityEnclosingRequest) request).setEntity(new StringEntity(params, contentType));
                }
            }
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity httpEntity = response.getEntity();
            log.info("sendHttp response status:{}", response.getStatusLine());
            if (Objects.nonNull(httpEntity)) {
                result = EntityUtils.toString(httpEntity, "UTF-8");
                log.info("sendHttp response body:{}", result);
            }
            HttpClientUtils.closeQuietly(response);// 关闭返回对象
        } catch (Exception e) {
            log.error(infoMessage + " failure", e);
        }
        long endTime = System.currentTimeMillis();
        log.info("request sendHttp response time cost:" + (endTime - beginTime) + " ms");
        return result;
    }

    /**
     * 请求方法（全大些）
     */
    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    /**
     * 上传返回结果封装
     */
    public static class RequestResult {
        private boolean isSuccess;// 是否成功
        private String msg;// 消息

        public boolean isSuccess() {
            return isSuccess;
        }

        public RequestResult setSuccess(boolean success) {
            isSuccess = success;
            return this;
        }

        public String getMsg() {
            return msg;
        }

        public RequestResult setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public RequestResult() {
            this.isSuccess = false;
        }
    }

    /**
     * 构建请求方法
     *
     * @param method
     * @param url
     * @return
     */
    private static HttpRequestBase buildHttpMethod(HttpMethod method, String url) {
        if (HttpMethod.GET.equals(method)) {
            return new HttpGet(url);
        } else if (HttpMethod.POST.equals(method)) {
            return new HttpPost(url);
        } else if (HttpMethod.PUT.equals(method)) {
            return new HttpPut(url);
        } else if (HttpMethod.DELETE.equals(method)) {
            return new HttpDelete(url);
        } else {
            return null;
        }
    }

    /**
     * 配置证书
     *
     * @return
     */
    private static SSLContext createSSLContext() {
        try {
            // 信任所有,支持导入ssl证书
            TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            return sslContext;
        } catch (Exception e) {
            log.error("初始化ssl配置失败", e);
            throw new RuntimeException("初始化ssl配置失败");
        }
    }

    /**
     * 复制文件流
     *
     * @param input
     * @return
     */
    private static ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                byteOutStream.write(buffer, 0, len);
            }
            byteOutStream.flush();
            return byteOutStream;
        } catch (IOException e) {
            log.warn("copy InputStream error，{}", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * 输入流转字节流
     *
     * @param in
     * @return
     */
    private static byte[] inputStream2byte(InputStream in) {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            in.close();
            bos.close();
            byte[] buffer = bos.toByteArray();
            return buffer;
        } catch (IOException e) {
            log.warn("inputStream transfer byte error，{}", ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("clone inputStream error", e);
            }
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("clone outputStream error", e);
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://127.0.0.1:20040/web/api/demo/test";
        String result = HttpUtil.getUrl(url);
        System.out.println(result);
    }

}
