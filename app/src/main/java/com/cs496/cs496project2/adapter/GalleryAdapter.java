package com.cs496.cs496project2.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.model.Image;
import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;

import java.util.List;

public class GalleryAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Image, Image, Image> {
    private List<Image> images;
    private Context mContext;

    public class GalleryViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public GalleryViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public HeaderViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public GalleryAdapter(Context context, List<Image> images) {
        mContext = context;
        this.images = images;
    }

    @Override
    protected HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail, parent, false);
        return new HeaderViewHolder(headerView);
    }


    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        //TODO: image sources!
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        Glide.with(mContext)
                .load(R.drawable.placeholder)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(headerViewHolder.thumbnail);
    }

    @Override
    public GalleryViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail, parent, false);

        return new GalleryViewHolder(itemView);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        GalleryViewHolder galleryViewHolder = (GalleryViewHolder) holder;

        //TODO: image sources!
        Glide.with(mContext)
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTNDJerebYjU3S4HUgWYwDAN1MSq0R8ARqGIjP4NQyDfN885fXt")
                .placeholder(R.drawable.placeholder)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(galleryViewHolder.thumbnail);
    }


    @Override
    public int getItemCount() {
        return images.size() + 1;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
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
