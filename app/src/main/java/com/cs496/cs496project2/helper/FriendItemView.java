package com.cs496.cs496project2.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs496.cs496project2.R;

//좀 이상하다.
public class FriendItemView extends FrameLayout {

    TextView nameView, mobileView;
    ImageView profileImageView;


    public FriendItemView(Context context) {
        super(context);
        init(context);
    }

    public FriendItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FriendItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.friend_item, this, true);
        nameView = (TextView) findViewById(R.id.friend_name);
        mobileView = (TextView) findViewById(R.id.friend_mobile);
        profileImageView = (ImageView) findViewById(R.id.friend_profile_image);
    }

    public void setName(String name) { nameView.setText(name); }

    public void setMobile(String mobile){ mobileView.setText(mobile); }

    public void setImage(int resId){
        profileImageView.setImageResource(resId);
    }

    public void setBitImage(Bitmap bitmap){
        profileImageView.setImageBitmap(bitmap);
    }
}
