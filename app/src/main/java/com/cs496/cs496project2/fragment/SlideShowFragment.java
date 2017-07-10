package com.cs496.cs496project2.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.model.Image;

import java.util.ArrayList;


public class SlideShowFragment extends DialogFragment {

    private ArrayList<Image> images;
    private ViewPager viewPager;
    private SlideShowViewPagerAdapter viewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    private boolean showMetaInfo = false;


    static SlideShowFragment newInstance() {
        return new SlideShowFragment();
    }

    public SlideShowFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide_show, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.slide_show_view_pager);
        lblCount = (TextView) view.findViewById(R.id.lbl_count);
        lblTitle = (TextView) view.findViewById(R.id.lbl_title);
        lblDate = (TextView) view.findViewById(R.id.lbl_date);

        images = (ArrayList<Image>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        viewPagerAdapter = new SlideShowViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        lblTitle.setVisibility(view.INVISIBLE);
        lblCount.setVisibility(view.INVISIBLE);
        lblDate.setVisibility(view.INVISIBLE);

        setCurrentItem(selectedPosition);

        return view;
    }


    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        Image image = images.get(position);

        lblCount.setText((position + 1) + " of " + images.size());
        lblTitle.setText(image.getTitle());
        lblDate.setText(image.getDate());
    }




    public class SlideShowViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public SlideShowViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen, container, false);

            ImageView fullImageView = (ImageView) view.findViewById(R.id.full_image_view);


            //TODO imagesource
            Glide.with(getActivity())
                    .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTNDJerebYjU3S4HUgWYwDAN1MSq0R8ARqGIjP4NQyDfN885fXt")
                    .placeholder(R.drawable.placeholder)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(fullImageView);

            container.addView(view);


/*
            fullImageView.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMetaInfo = !showMetaInfo;
                    if(!showMetaInfo){
                        lblTitle.setVisibility(view.INVISIBLE);
                        lblCount.setVisibility(view.INVISIBLE);
                        lblDate.setVisibility(view.INVISIBLE);
                    }
                    else{
                        lblTitle.setVisibility(view.VISIBLE);
                        lblCount.setVisibility(view.VISIBLE);
                        lblDate.setVisibility(view.VISIBLE);
                    }
                }

            });*/

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
