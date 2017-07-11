package com.cs496.cs496project2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.R;

import uk.co.senab.photoview.PhotoView;

public class ImageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        /*ImageView img = (ImageView)findViewById(R.id.targetImg);
        Glide.with(this).load(url).centerCrop().into(img);*/

        PhotoView photoView = (PhotoView)findViewById(R.id.targetImg);
        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(photoView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
