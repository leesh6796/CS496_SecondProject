package com.cs496.cs496project2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.helper.GridViewImgItem;
import com.cs496.cs496project2.helper.GuestBookItem;

import java.util.ArrayList;

/**
 * Created by memorial on 2017. 7. 12..
 */

public class GuestBookAdapter extends BaseAdapter {

    private ArrayList<GuestBookItem> items = new ArrayList<>();

    public GuestBookAdapter() {

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_guestbook, parent, false);
        }

        Log.i("진입", String.valueOf(position));

        ImageView img = (ImageView)convertView.findViewById(R.id.profilePicture);
        TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView content = (TextView)convertView.findViewById(R.id.content);

        GuestBookItem item = items.get(pos);

        Glide.with(context).load(item.getProfilePictureURL()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(img);
        name.setText(item.getName());
        content.setText(item.getContent());

        return convertView;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public GuestBookItem getItem(int pos) {
        return items.get(pos);
    }

    public void addItem(String name, String content, String profilePictureURL) {
        GuestBookItem item = new GuestBookItem();
        item.setName(name);
        item.setContent(content);
        item.setProfilePictureURL(profilePictureURL);

        items.add(item);
    }

    public void addItem(GuestBookItem item) {
        items.add(item);
    }

    public void clearItem() {
        items.clear();
    }
}
