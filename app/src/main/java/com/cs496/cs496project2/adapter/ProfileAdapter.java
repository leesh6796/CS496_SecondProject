package com.cs496.cs496project2.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.MainActivity;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.helper.ServerRequest;
import com.cs496.cs496project2.model.Image;
import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Image, Image, Image> {

    private Context context;
    private String phoneNumber;
    ArrayList<String> urls = new ArrayList<>();

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImageView;
        public TextView nameView, phoneNumberView;
        public HeaderViewHolder(View view) {
            super(view);
            /*profileImageView = (ImageView) view.findViewById(R.id.profile_image);
            nameView = (TextView) view.findViewById(R.id.profile_name);
            phoneNumberView = (TextView) view.findViewById(R.id.profile_phoneNumber);*/
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public ImageViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public ProfileAdapter(final Context context, final String phoneNumber) {
        this.context = context;
        this.phoneNumber = phoneNumber;
    }

    public void galleryUpdate() {
        urls.clear();
        (new AsyncTask<Void,Void,List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                return (new ServerRequest(context)).getGallery(phoneNumber);
            }
            @Override
            protected void onPostExecute(List<String> temp) {
                for(String url : temp) urls.add(url);
            }
        }).execute();
    }


    //TODO: getHeader(), getItems()로 항목 접근


    @Override
    protected HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_header, parent, false);
        return new HeaderViewHolder(headerView);
    }


    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        //TODO:이거 왜인지 작동 안함 ???
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        Log.e("               ", getHeader().getImageUrl());
        Glide.with(context)
                //.load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTNDJerebYjU3S4HUgWYwDAN1MSq0R8ARqGIjP4NQyDfN885fXt")
                .load(getHeader().getImageUrl())
                .placeholder(R.drawable.placeholder)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(headerViewHolder.profileImageView);
    }

    @Override
    public ImageViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail, parent, false);

        return new ImageViewHolder(itemView);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        Log.i("Position", String.valueOf(position));

        //TODO: image sources! -> 으악
        Glide.with(context)
                .load(urls.get(position))
                .placeholder(R.drawable.placeholder)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewHolder.thumbnail);
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ProfileAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ProfileAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
