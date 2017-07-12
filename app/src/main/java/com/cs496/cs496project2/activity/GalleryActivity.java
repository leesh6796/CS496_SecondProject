package com.cs496.cs496project2.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cs496.cs496project2.R;
import com.cs496.cs496project2.fragment.GalleryFragment;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneNumber");

        Fragment frag;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        frag = new GalleryFragment();

        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber", phoneNumber);
        frag.setArguments(bundle);

        ft.replace(R.id.gallery_fragment, frag).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
