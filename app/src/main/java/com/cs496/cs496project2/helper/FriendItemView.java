package com.cs496.cs496project2.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.R;

//좀 이상하다.
public class FriendItemView extends FrameLayout {

    TextView nameView, phoneNumberView;
    ImageView profileImageView;
    Context context;


    public FriendItemView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public FriendItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public FriendItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.friend_item, this, true);
        nameView = (TextView) findViewById(R.id.friend_name);
        phoneNumberView = (TextView) findViewById(R.id.friend_mobile);
        profileImageView = (ImageView) findViewById(R.id.friend_profile_image);

    }

    public void setName(String name) { nameView.setText(name); }

    public void setPhoneNumber(String mobile){ phoneNumberView.setText(mobile); }

    /*public void setBitImage(Bitmap bitmap){
        profileImageView.setImageBitmap(bitmap);
    }*/

    public void setImage(int id) {
        Glide.with(context)
                .load("") //id 기반 http 요청?
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(profileImageView);
    }
}
