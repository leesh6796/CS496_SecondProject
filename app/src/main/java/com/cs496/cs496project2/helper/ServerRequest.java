package com.cs496.cs496project2.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.cs496.cs496project2.model.Friend;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerRequest {
    public static final String url = "http://52.79.188.97/";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String myPhoneNumber;

    public ServerRequest(Context context) {
        SharedPreferences pref = context.getSharedPreferences("registration", 0);
        myPhoneNumber = pref.getString("phone_number", "");
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
        Request req = new Request.Builder().url(url + "api/account/add").put(reqBody).build();

        try {
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

                arrFriends.put(body);
            }

            contacts.put("contacts", arrFriends);

        } catch(Exception e) {
            e.printStackTrace();
        }

        RequestBody reqBody = RequestBody.create(JSON, contacts.toString());
        Request req = new Request.Builder().url(url + "api/" + myPhoneNumber + "/set/friends").put(reqBody).build();

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

        Request req = new Request.Builder().url(url + "api/" + myPhoneNumber + "/get/friends").build();
        try {
            Response response = client.newCall(req).execute();
            JSONArray contacts = new JSONArray(response.body().string());

            List<Friend> friends = new ArrayList<>();
            int i;

            for(i=0; i<contacts.length(); i++) {
                JSONObject iter = contacts.getJSONObject(i);
                Friend item = new Friend(iter.getString("phoneNumber"));
                item.setName(iter.getString("name"));

                Log.i("name", iter.getString("name"));

                friends.add(item);
            }

            return friends;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getGallery(String phoneNumber) {
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder().url(url + "api/" + phoneNumber + "/get/gallery").build();
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

    public void uploadFile(String filename, File file) {
        HttpClient client = new DefaultHttpClient();

        HttpPost post = new HttpPost(url + "api/" + myPhoneNumber +"/upload/picture/" + filename);

        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("attachment", new FileBody(file));
        post.setEntity(reqEntity);

        try {
            HttpResponse response = client.execute(post);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getProfileImageUrl(String phoneNumber) {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(url + "api/" + phoneNumber + "/get/account").build();

        try {
            Response response = client.newCall(req).execute();
            JSONObject account = new JSONObject(response.body().string());
            return account.getString("profilePictureURL");
        } catch(JSONException e) {
            //e.printStackTrace();
            Log.e(phoneNumber, "is not registered");
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<GuestBookItem> getGuestBook(String phoneNumber) {
        OkHttpClient client = new OkHttpClient();

        String apiURL = url + "api/" + phoneNumber + "/get/guestbook";
        Request req = new Request.Builder().url(apiURL).build();

        try {
            Response response = client.newCall(req).execute();

            JSONArray items = new JSONArray(response.body().string());
            List<GuestBookItem> returnValue = new ArrayList<>();

            int i;
            for(i=0; i<items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                GuestBookItem ele = new GuestBookItem();

                ele.setName(item.getString("name"));
                ele.setContent(item.getString("content"));
                ele.setProfilePictureURL(item.getString("profilePictureURL"));
                ele.setSecret(item.getBoolean("secret"));

                returnValue.add(ele);
            }

            return returnValue;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String addGuestBook(String phoneNumber, GuestBookItem item) {
        OkHttpClient client = new OkHttpClient();
        String api = this.url + "api/" + phoneNumber + "/add/guestbook";

        JSONObject json = new JSONObject();
        try {
            json.put("visitor", item.getName());
            json.put("content", item.getContent());
            json.put("profilePictureURL", item.getProfilePictureURL());
            json.put("secret", item.isSecret());
        } catch(Exception e) {
            e.printStackTrace();
        }

        RequestBody reqBody = RequestBody.create(JSON, json.toString());
        Request req = new Request.Builder().url(api).post(reqBody).build();

        try {
            Response res = client.newCall(req).execute();
            String body = res.body().string();
            return body;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public boolean isRegister(String phoneNumber) {
        OkHttpClient client = new OkHttpClient();
        String api = this.url + "api/" + phoneNumber + "/isregister";

        Request req = new Request.Builder().url(api).build();
        try {
            Response response = client.newCall(req).execute();
            String body = response.body().string();

            if(body == "true") return true;
            else if(body == "false") return false;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}
