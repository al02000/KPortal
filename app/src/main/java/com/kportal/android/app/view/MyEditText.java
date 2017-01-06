package com.kportal.android.app.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.kportal.android.app.R;
import com.kportal.android.app.common.Cutil;

/**
 * Created by KR8 on 2016-11-11.
 */

public class MyEditText extends EditText {

    private final String TAG = "MYEditText";
    private Context mContext;
    private ViewGroup.LayoutParams mParam;

//    public void onStyleChange(int style) {
//        this.setTypeface(null, style);
//    }
//
//    public int getTextStyle() {
//        return this.getTypeface().getStyle();
//    }
//
//    public void changeTextSize(int size) {
//        this.setTextSize(size);
//    }
//
//    public int currentTextSize() {
//        return (int) Cutil.convertToDp(mContext, this.getTextSize());
//    }
//
//    public void changeTextColor(int color) {
//        this.setTextColor(color);
//    }

    public int getTextColor() {
        return this.getCurrentTextColor();
    }

    public MyEditText(Context con) {
        super(con);
        this.mContext = con;
        init();
    }

    public MyEditText(Context con, AttributeSet attrs) {
        super(con, attrs);
        this.mContext = con;
        init();
    }

    private void init() {
        mParam = new ViewGroup.LayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//        Log.i(TAG, "Init");
        this.setLayoutParams(mParam);
        this.setTextColor(Color.BLACK);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
        this.setTextSize(12);
        this.setBackgroundResource(R.drawable.shape_pop_bg);
        this.setPadding((int)Cutil.convertToPx(mContext, 10), (int)Cutil.convertToPx(mContext, 10), (int)Cutil.convertToPx(mContext, 10), (int)Cutil.convertToPx(mContext, 10));
    }

}
