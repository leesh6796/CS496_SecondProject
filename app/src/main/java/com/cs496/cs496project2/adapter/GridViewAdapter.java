package com.cs496.cs496project2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.helper.GridViewImgItem;

import java.util.ArrayList;

/**
 * Created by memorial on 2017. 7. 11..
 */

public class GridViewAdapter extends BaseAdapter {
    private ArrayList<GridViewImgItem> gridViewItemList = new ArrayList<GridViewImgItem>();

    public GridViewAdapter() {

    }

    @Override
    public int getCount() {
        return gridViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_grid, parent, false);
        }

        ImageView img = (ImageView)convertView.findViewById(R.id.gridImg);
        GridViewImgItem item = gridViewItemList.get(pos);
        Glide.with(context).load(item.getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(img);

        return convertView;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public Object getItem(int pos) {
        return gridViewItemList.get(pos);
    }

    public void addItem(String url) {
        GridViewImgItem item = new GridViewImgItem();
        item.setUrl(url);

        gridViewItemList.add(item);
    }

    public void clearItem() {
        gridViewItemList.clear();
    }
}
