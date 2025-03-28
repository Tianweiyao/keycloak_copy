package com.weixin.request.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author weiyao_tian 2025/3/14
 */
@Slf4j
public class HttpUtil {

    private static Logger logging = LoggerFactory.getLogger(HttpUtil.class);

    private static final String APPID = "wxa8de49759d00f0f9";
    private static final String SECRET = "b68dbef76b5701dc0c3d4bdbbd597a16";




    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * 发送 GET 请求
     */
    public static String doGet(String url) throws IOException {
        logging.info("请求 url:{}",url);
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return parseResponse(response);
        }
    }

    /**
     * 发送 POST 请求（JSON 格式）
     */
    public static String doPostJson(String url, String jsonBody) throws IOException {
        logging.info("请求 url:{}",url);
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        return doPost(httpPost);
    }

    /**
     * 通用 POST 请求处理
     */
    private static String doPost(HttpPost httpPost) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return parseResponse(response);
        }
    }

    /**
     * 解析响应内容
     */
    private static String parseResponse(CloseableHttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException("HTTP 请求失败，状态码: " + statusCode);
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        try {
            return EntityUtils.toString(entity);
        } catch (ParseException e) {
            throw new IOException("解析响应内容失败", e);
        } finally {
            EntityUtils.consume(entity); // 确保流被关闭
        }
    }

    /**
     * 关闭连接池（在应用退出时调用）
     */
    public static void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String decode(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static byte[] encode(String str, String charsetName) {
        return str.getBytes(Charset.forName(charsetName));
    }


}
