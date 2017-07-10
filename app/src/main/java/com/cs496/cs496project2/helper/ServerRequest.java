package com.cs496.cs496project2.helper;

/**
 * Created by memorial on 2017. 7. 10..
 */

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class ServerRequest {
    private final String url = "http://52.79.188.97/";
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public String getUrl() {
        return this.url;
    }

    public ServerRequest() {

    }

    // 서버에 새로운 계정을 추가한다. 프로그램에서는 최초 등록 시 한 번만 실행할 듯.
    // Response Body를 return 한다.
    public String addAccount(String name, String phoneNumber, String email, String profilePictureURL) {
        OkHttpClient client = new OkHttpClient();

        JSONObject account = new JSONObject();
        try {
            account.put("name", name);
            account.put("phoneNumber", phoneNumber);
            account.put("email", email);
            account.put("profilePictureURL", profilePictureURL);
        } catch(Exception e) {
            e.printStackTrace();
        }

        RequestBody reqBody = RequestBody.create(JSON, account.toString());
        Request req = new Request.Builder().url(this.url + "api/account/add").put(reqBody).build();

        try {
            Response res = client.newCall(req).execute();
            return res.body().string();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String setContacts() {
        return "";
    }
}
