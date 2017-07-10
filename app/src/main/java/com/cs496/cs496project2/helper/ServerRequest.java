package com.cs496.cs496project2.helper;

/**
 * Created by memorial on 2017. 7. 10..
 */

import com.cs496.cs496project2.MainActivity;
import com.cs496.cs496project2.model.Friend;

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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServerRequest {
    private final String url = "http://52.79.188.97/";
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String phoneNumber = MainActivity.myPhoneNumber;

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
            //TODO 여기 에러
            Response res = client.newCall(req).execute();
            return res.body().string();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String setContacts(List<Friend> friends) {
        OkHttpClient client = new OkHttpClient();

        JSONObject contacts = new JSONObject();
        try {
            JSONArray arrFriends = new JSONArray();

            int i;
            for(i=0; i<friends.size(); i++) {
                JSONObject body = new JSONObject();
                Friend iter = friends.get(i);
                body.put("name", iter.getName());
                body.put("phoneNumber", iter.getPhoneNumber());
                body.put("email", iter.getEmail());

                arrFriends.put(body);
            }

            contacts.put("contacts", arrFriends);

        } catch(Exception e) {
            e.printStackTrace();
        }

        RequestBody reqBody = RequestBody.create(JSON, contacts.toString());
        Request req = new Request.Builder().url(this.url + "api/" + this.phoneNumber + "/set/friends").put(reqBody).build();

        try {
            Response res = client.newCall(req).execute();
            return res.body().string();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Friend> getContacts() {
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder().url(this.url + "api/" + this.phoneNumber + "/get/friends").build();
        try {
            Response response = client.newCall(req).execute();
            JSONArray contacts = new JSONArray(response.body().string());

            List<Friend> friends = new ArrayList<Friend>();
            int i;

            for(i=0; i<contacts.length(); i++) {
                JSONObject iter = contacts.getJSONObject(i);
                Friend item = new Friend(iter.getString("phoneNumber"));
                item.setName(iter.getString("name"));
                item.setEmail(iter.getString("email"));
                item.setProfileImageUrl(iter.getString("profilePicture"));

                friends.add(item);
            }

            return friends;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getGallery() {
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder().url(this.url + "api/" + phoneNumber + "/get/gallery").build();
        try {
            Response response = client.newCall(req).execute();
            ArrayList<String> gallery = new ArrayList<>();
            int i;

            String[] urls = response.body().string().split(",");
            for(i=0; i<urls.length; i++) {
                gallery.add(url + urls[i]);
            }

            return gallery;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
