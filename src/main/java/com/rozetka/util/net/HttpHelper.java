package com.rozetka.util.net;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public final class HttpHelper {
    private final static org.slf4j.Logger syslog = org.slf4j.LoggerFactory.getLogger(HttpHelper.class);

    // HTTP GET request
    public static String sendGet(String url, List<NameValuePair> params) {
        URI uri = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            builder.setParameters(params);
            uri = builder.build();
        } catch (URISyntaxException e) {
            syslog.error("", e);
            return "";
        }

        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String content = "";

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            syslog.debug("----------------------------------------");
            syslog.debug("Executing request: " + httpGet.getRequestLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                StringWriter writer = new StringWriter();
                IOUtils.copy(entity.getContent(), writer, "UTF-8");
                content = writer.toString();
                writer.close();
            }
            EntityUtils.consume(entity);
        } catch (ClientProtocolException e) {
            syslog.error("", e);
        } catch (IOException e) {
            syslog.error("", e);
        }
        syslog.debug("Response: " + content);
        syslog.debug("----------------------------------------");
        return content;
    }

    //HTTP POST MultipartForm
    public static void sendMultipartFormPost(String url, HttpEntity httpEntity) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(httpEntity);
            syslog.debug("----------------------------------------");
            syslog.debug("Executing request: " + httpPost.getRequestLine());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                syslog.debug(response.getStatusLine().toString());
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    syslog.debug("Response content length: " + responseEntity.getContentLength());
                }
                EntityUtils.consume(responseEntity);
            } finally {
                syslog.debug("----------------------------------------");
                response.close();
            }
        } catch (ClientProtocolException e) {
            syslog.error("", e);
        } catch (IOException e) {
            syslog.error("", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                syslog.error("", e);
            }
        }
    }
}