package com.cs496.cs496project2.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.helper.ServerRequest;
import com.cs496.cs496project2.model.Friend;
import com.cs496.cs496project2.model.Image;

import java.util.ArrayList;
import java.util.List;


public class VisitActivity extends AppCompatActivity {

    VisitActivity thisActivity = this;
    ImageView guestBookButton, galleryButton;
    private String profileImageURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        guestBookButton = (ImageView) findViewById(R.id.btn_guest_book);
        galleryButton = (ImageView) findViewById(R.id.btn_gallery);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String phoneNumber = intent.getStringExtra("phoneNumber");

        final Friend friend = new Friend(phoneNumber, name);

        (new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String url = (new ServerRequest(thisActivity)).getProfileImageUrl(friend.getPhoneNumber());

                return url;
            }
            @Override
            protected void onPostExecute(String url) {
                profileImageURL = url;
                Glide.with(thisActivity)
                    .load(url)
                    .placeholder(R.drawable.person)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(guestBookButton);
            }
        }).execute();


        (new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... voids) {
                List<String> urls = (new ServerRequest(thisActivity)).getGallery(friend.getPhoneNumber());

                if(urls.size() > 0) return urls.get(0);
                return "https://nomore.org/wp-content/uploads/2016/11/NO-MORE_INLINE_BADGE_RGB.png";
            }
            @Override
            protected void onPostExecute(String url) {
                if (!url.equals("no")) {
                    Log.i("Gallery", url);
                    Glide.with(thisActivity)
                        .load(url)
                        .placeholder(R.drawable.placeholder).centerCrop()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(galleryButton);
                }
            }
        }).execute();




        guestBookButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guestBookIntent = new Intent(thisActivity, GuestBookActivity.class);
                guestBookIntent.putExtra("name", name);
                guestBookIntent.putExtra("profileImageURL", profileImageURL);
                guestBookIntent.putExtra("phoneNumber", phoneNumber);
                guestBookIntent.putExtra("isMyGuestBook", false);
                startActivity(guestBookIntent);
            }
        });

        galleryButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(thisActivity, GalleryActivity.class);
                galleryIntent.putExtra("phoneNumber", phoneNumber);
                startActivity(galleryIntent);
            }
        });
    }
}
