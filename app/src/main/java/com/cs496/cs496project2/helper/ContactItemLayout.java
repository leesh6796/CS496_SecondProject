package com.cs496.cs496project2.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cs496.cs496project2.R;

public class ContactItemLayout extends LinearLayout {

    public ContactItemLayout(Context context) {
        super(context);
        init(context);
    }

    public ContactItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.contact_item, this, true);
    }
}
