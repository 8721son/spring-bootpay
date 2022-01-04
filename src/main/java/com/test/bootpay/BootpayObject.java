package com.test.bootpay;

import com.google.gson.Gson;
import com.test.bootpay.domain.dto.request.Token;
import com.test.bootpay.domain.dto.response.ResToken;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URI;


public class BootpayObject {
    public String token;
    public String application_id;
    public String private_key;
    public String baseUrl;

    public static final String PRODUCTION = "https://api.bootpay.co.kr/";

    public BootpayObject() {}
    public BootpayObject(String rest_application_id, String private_key) {
        this.application_id = rest_application_id;
        this.private_key = private_key;
        this.baseUrl = PRODUCTION;
    }

    public BootpayObject(String rest_application_id, String private_key, String devMode) {
        this.application_id = rest_application_id;
        this.private_key = private_key;
        this.baseUrl = devMode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HttpGet httpGet(String url) throws Exception {
        HttpGet get = new HttpGet(this.baseUrl + url);
        URI uri = new URIBuilder(get.getURI()).build();
        get.setURI(uri);
        return get;
    }

    public HttpPost httpPost(String url, StringEntity entity) {
        HttpPost post = new HttpPost(this.baseUrl + url);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept-Charset", "utf-8");
        post.setEntity(entity);
        return post;
    }

    public HttpResponse getAccessToken() throws Exception {
        if(this.application_id == null || this.application_id.isEmpty()) throw new Exception("application_id 값이 비어있습니다.");
        if(this.private_key == null || this.private_key.isEmpty()) throw new Exception("private_key 값이 비어있습니다.");

        Token token = new Token();
        token.application_id = this.application_id;
        token.private_key = this.private_key;

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = httpPost("request/token.json", new StringEntity(new Gson().toJson(token), "UTF-8"));

        HttpResponse res = client.execute(post);
        String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
        ResToken resToken = new Gson().fromJson(str, ResToken.class);
        if(resToken.status == 200)
            this.token = resToken.data.token;

        return res;
    }
}