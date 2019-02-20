package com.lunch.support.http;

import com.alibaba.fastjson.JSON;
import com.lunch.support.tool.LogNewUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    private static final String USER_AGENT = "Mozilla/5.0";

    public enum Method {
        POST,
        GET
    }

    @SuppressWarnings("unchecked")
    public static <T> T syncRequestJson(Method method, String url, Map<String, String> params, Class<T> t) {
        String result = syncRequest(method, url, params);
        return JSON.parseObject(result, t);
    }

    public static String syncRequest(Method method, String url, Map<String, String> params) {
        try {
            if (method == Method.GET) {
                return syncGet(parseGet(url, params));
            }
            return syncPost(url, params);
        } catch (IOException e) {
            e.printStackTrace();
            LogNewUtils.printException("syncRequest error ,url: " + url + " params: " + params, e);
            return null;
        }
    }

    private static String syncGet(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        LogNewUtils.info("\nSending 'GET' request to URL : " + url);
        LogNewUtils.info("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        LogNewUtils.info(response.toString());
        return response.toString();
    }

    private static String syncPost(String url, Map<String, String> params) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", USER_AGENT);

        post.setEntity(new UrlEncodedFormEntity(parsePost(params)));

        HttpResponse response = client.execute(post);
        LogNewUtils.info("\nSending 'POST' request to URL : " + url);
        LogNewUtils.info("Post parameters : " + post.getEntity());
        LogNewUtils.info("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        LogNewUtils.info(result.toString());
        return result.toString();
    }

    private static String parseGet(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }
        StringBuffer sb = new StringBuffer(url);
        sb.append("?");
        for (String key : params.keySet()) {
            sb.append(key).append("=").append(params.get(key)).append("&");
        }

        return sb.toString().substring(0, sb.length() - 2);
    }

    private static List<NameValuePair> parsePost(Map<String, String> params) {
        List<NameValuePair> pairs = new ArrayList<>();
        if (params == null) {
            return pairs;
        }
        for (String key : params.keySet()) {
            pairs.add(new BasicNameValuePair(key, params.get(key)));
        }
        LogNewUtils.info("post params:" + params);
        return pairs;
    }
}
