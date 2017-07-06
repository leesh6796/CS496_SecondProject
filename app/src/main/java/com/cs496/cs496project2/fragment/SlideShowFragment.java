package com.cs496.cs496project2.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs496.cs496project2.R;
import com.cs496.cs496project2.adapter.SlideShowViewPagerAdapter;
import com.cs496.cs496project2.model.Image;

import java.util.ArrayList;


public class SlideShowFragment extends Fragment {

    private ArrayList<Image> images;
    private ViewPager viewPager;
    private SlideShowViewPagerAdapter viewPagerAdapter;
    private TextView lblCount, lblTitle;
    private int selectedPosition = 0;
    private boolean showData = false;


    static SlideShowFragment newInstance() {
        return new SlideShowFragment();
    }

    public SlideShowFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide_show, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.);
        lblCount = (TextView) view.findViewById(R.id.lbl_count);
        lblTitle = (TextView) view.findViewById(R.id.title);

        images = (ArrayList<Image>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        viewPagerAdapter = SlideShowFragment();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        lblTitle.setVisibility(view.INVISIBLE);
        lblCount.setVisibility(view.INVISIBLE);

        setCurrentItem(selectedPosition);

        return view;
    }

}
