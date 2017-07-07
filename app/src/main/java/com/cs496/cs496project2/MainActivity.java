package com.cs496.cs496project2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import okhttp3.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("작업", "로드");

        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground(Void... args) {
                Log.i("작업", "시작");

                String dirPath = getFilesDir().getAbsolutePath();
                File file = new File(dirPath);

                // 일치하는 폴더가 없으면 생성
                if(!file.exists()) {
                    file.mkdirs();
                    Log.i("폴더 생성", "Success");
                }

                // txt 파일 생성
                String testStr = "ABCDE";
                File saveFile = new File(dirPath + "/test.txt");
                try {
                    if(!saveFile.exists()) {
                        FileOutputStream fos = new FileOutputStream(saveFile);
                        fos.write(testStr.getBytes());
                        fos.close();
                        Log.i("파일 생성", "Success");
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://13.124.149.1/api/upload/profile/01082169122");

                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("attachment", new FileBody(new File(dirPath + "/test.txt")));
                post.setEntity(reqEntity);

                try {
                    HttpResponse response = client.execute(post);
                } catch(Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public void onPostExecute(final Void result) {
            }
        }.execute();
    }
}
