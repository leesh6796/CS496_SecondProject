package com.cs496.cs496project2.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.helper.FriendItemView;
import com.cs496.cs496project2.helper.ServerRequest;
import com.cs496.cs496project2.helper.SoundSearcher;
import com.cs496.cs496project2.model.Friend;

import java.io.File;
import java.util.ArrayList;


public class FriendAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<Friend> items;
    SearchFilter filter;
    ArrayList<Friend> filterList;

    public FriendAdapter(Context context, ArrayList<Friend> items) {
        this.context = context;
        this.items = items;
        filterList = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(Friend item) {
        items.add(item);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//
//        if (convertView != null) {
//            return (FriendItemView) convertView;
//        }

        final FriendItemView view =  new FriendItemView(context);
        final Friend item = items.get(position);
        view.setName(item.getName());
        view.setPhoneNumber(item.getPhoneNumber());

        (new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return (new ServerRequest(context)).getProfileImageUrl(item.getPhoneNumber());
            }
            @Override
            protected void onPostExecute(String temp) {
                if(temp != null)
                    view.setImage(temp);
            }
        }).execute();

        return view;
    }

    @Override
    public Filter getFilter(){
        if(filter == null){
            filter = new SearchFilter();
        }
        return filter;
    }

    private class SearchFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length() >0) {
                constraint= constraint.toString().toUpperCase();

                ArrayList<Friend> filters = new ArrayList<Friend>();

                for(int i =0; i<filterList.size();i++) {
                    if(filterList.get(i).getName().toUpperCase().contains(constraint) || filterList.get(i).getPhoneNumber().contains(constraint) || SoundSearcher.matchString(filterList.get(i).getName(),constraint.toString())){
                        Friend p = new Friend(filterList.get(i).getPhoneNumber(), filterList.get(i).getName());
                        filters.add(p);
                    }
                }

                results.count=filters.size();
                results.values=filters;


            } else {
                results.count=filterList.size();
                results.values=filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            items = (ArrayList<Friend>) results.values;
            notifyDataSetChanged();

        }
    }

}
