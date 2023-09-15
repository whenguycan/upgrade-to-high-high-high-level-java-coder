package com.ruoyi.project.parking.utils;

import com.ruoyi.common.utils.JsonUtil;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 发送http请求的工具类
 *
 * @author LijieZhu
 * @date 2021/8/9 9:12
 */
public class HttpClientUtil {
    private static final Logger LOG = LogManager.getLogger(HttpClientUtil.class);

    public static String doGet(String url, Map<String, String> param) {
        // 创建Httpclient对象[根据是否 https 请求创建不同的类型客户端]
        CloseableHttpClient httpClient;
        if (url.startsWith("https")) {
            httpClient = createSSLClientDefault();
        } else {
            httpClient = HttpClients.createDefault();
        }
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return resultString;
    }

    public static String doGet(String url, Map<String, String> header, Map<String, String> param) {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            //请求头设置
            setHeader(httpGet, header);

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return resultString;
    }

    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error(e);


        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }
        }


        return resultString;
    }


    private static final class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public static CloseableHttpClient createSSLClientDefault(){

        try {
            //SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // 在JSSE中，证书信任管理器类就是实现了接口X509TrustManager的类。我们可以自己实现该接口，让它信任我们指定的证书。
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            //信任所有
            X509TrustManager x509mgr = new X509TrustManager() {

                //　　该方法检查客户端的证书，若不信任该证书则抛出异常
                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) {
                }
                // 　　该方法检查服务端的证书，若不信任该证书则抛出异常
                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string) {
                }
                // 　返回受信任的X509证书数组。
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { x509mgr }, null);
            ////创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            //  HttpsURLConnection对象就可以正常连接HTTPS了，无论其证书是否经权威机构的验证，只要实现了接口X509TrustManager的类MyX509TrustManager信任该证书。
            return HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    // 设置cookie策略，否则某些请求不支持
                    .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                    .build();


        } catch (KeyManagementException e) {

            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }

        // 创建默认的httpClient实例.
        return  HttpClients.createDefault();

    }



    /**
     * 可设置头参数，需要修改为content-type可自动选择
     *
     * @param url
     * @param header
     * @param param
     * @return
     */
    public static String doPost(String url, Map<String, String> header, Map<String, String> param) throws Exception {
        // 创建Httpclient对象(忽略验证)
        CloseableHttpClient httpClient = createSSLClientDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求头
            setHeader(httpPost, header);
            // 创建参数列表
            if (param != null) {
                Header contentHeader = httpPost.getFirstHeader(HTTP.CONTENT_TYPE);
                if (contentHeader != null && contentHeader.getValue().contains("application/json")) {
                    StringEntity entity = new StringEntity(JsonUtil.bean2Json(param), ContentType.APPLICATION_JSON);
                    httpPost.setEntity(entity);
                } else {
                    List<NameValuePair> paramList = new ArrayList<>();
                    for (Map.Entry<String, String> entry : param.entrySet()) {
                        paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                    // 模拟表单
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
                    httpPost.setEntity(entity);
                }
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }
        }

        return resultString;
    }

    public static String doPostOfMultipartFormData(String url, File file, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            if (file != null && file.exists() && file.isFile()) {
                multipartEntityBuilder.addBinaryBody("file", file);
            }
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    multipartEntityBuilder.addTextBody(entry.getKey(), entry.getValue());
                }
            }
            httpPost.setEntity(multipartEntityBuilder.build());
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error(e);


        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return resultString;
    }

    public static String doPostOfMultipartFormData(String url, File file, Map<String, String> header, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            setHeader(httpPost, header);
            // 创建请求内容
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            if (file != null && file.exists() && file.isFile()) {
                multipartEntityBuilder.addBinaryBody("file", file);
            }
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    multipartEntityBuilder.addTextBody(entry.getKey(), entry.getValue());
                }
            }
            httpPost.setEntity(multipartEntityBuilder.build());
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error(e);


        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return resultString;
    }

    public static String doPostOfJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            LOG.info("@@@@@发送Post请求：{}，参数：{}", url, json);
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            LOG.info("@@@@@请求：{}，结果返回：{}", url, resultString);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return resultString;
    }

    public static String doPutOfJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Put请求
            HttpPut httpPut = new HttpPut(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPut.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPut);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return resultString;
    }

    public static String doDelete(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = builder.build();
            // 创建Http Delete请求
            HttpDelete httpDelete = new HttpDelete(uri);
            // 执行http请求
            response = httpClient.execute(httpDelete);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return resultString;
    }

    public static String doDeleteOfJson(String url, String json) {
        /**
         * 没有现成的delete可以带json的，自己实现一个，参考HttpPost的实现
         */
        class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
            public static final String METHOD_NAME = "DELETE";

            @SuppressWarnings("unused")
            public HttpDeleteWithBody() {
            }

            @SuppressWarnings("unused")
            public HttpDeleteWithBody(URI uri) {
                setURI(uri);
            }

            public HttpDeleteWithBody(String uri) {
                setURI(URI.create(uri));
            }

            @Override
            public String getMethod() {
                return METHOD_NAME;
            }
        }

        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
            StringEntity params = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpDelete.setEntity(params);
            // 执行http请求
            response = httpClient.execute(httpDelete);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return resultString;
    }

    //region 内部函数

    /**
     * 设置请求头
     *
     * @param httpRequestBase
     * @param headerMap
     */
    private static void setHeader(HttpRequestBase httpRequestBase, Map<String, String> headerMap) {
        if (headerMap != null) {
            if (headerMap != null && !headerMap.isEmpty()) {
                Set<Map.Entry<String, String>> entries = headerMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    httpRequestBase.setHeader(name, value);
                }
            }
        }
    }
    //endregion
}
