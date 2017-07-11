package com.cs496.cs496project2.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.model.Friend;


public class VisitActivity extends AppCompatActivity {

    VisitActivity thisActivity = this;
    ImageView guestBookButton, galleryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        guestBookButton = (ImageView) findViewById(R.id.btn_guest_book);
        galleryButton = (ImageView) findViewById(R.id.btn_gallery);

        final Friend friend = (Friend) getIntent().getExtras().getSerializable("friend");

        Glide.with(this)
                .load(friend.getProfileImageUrl())
                .placeholder(R.drawable.person)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(guestBookButton);

        Glide.with(this)
                .load(friend.fetchImages(this).get(0))
                .placeholder(R.drawable.person)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(guestBookButton);


        guestBookButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guestBookIntent = new Intent(thisActivity, GuestBookActivity.class);
                guestBookIntent.putExtra("friend", friend);
                startActivity(guestBookIntent);
            }
        });

        galleryButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(thisActivity, GalleryActivity);
                galleryIntent.putExtra("friend", friend);
                startActivity(galleryIntent);
            }
        });
    }
}
