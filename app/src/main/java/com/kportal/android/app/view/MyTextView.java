package com.kportal.android.app.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kportal.android.app.R;
import com.kportal.android.app.common.Cutil;
import com.kportal.android.app.common.Cvalue;

/**
 * Created by KR8 on 2016-11-02.
 */

public class MyTextView extends TextView implements View.OnTouchListener {

    private final String TAG = "MyTextVIew";
    private int mXDelta, mYDelta;
    public boolean isTouch = false;
    private Context mContext;
    private ViewGroup.LayoutParams mParam;

    public void onStyleChange(int style) {
        this.setTypeface(null, style);
    }

    public void changeTextSize(int size) {
        this.setTextSize(size);
    }

//    public float currentTextSize() {
//        return Cutil.convertToDp(mContext, this.getTextSize());
//    }

    public void changeTextColor(int color) {
        this.setTextColor(color);
    }

    public MyTextView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mParam = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.setLayoutParams(mParam);
        this.setOnTouchListener(this);
        this.setBackgroundColor(Color.WHITE);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setBackgroundResource(R.drawable.textarea);
        this.setPadding((int)Cutil.convertToPx(mContext, 10), (int)Cutil.convertToPx(mContext, 10), (int)Cutil.convertToPx(mContext, 10), (int)Cutil.convertToPx(mContext, 10));
        this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(Cvalue.nTouch == 2) {
            if(isTouch == false) {
                return false;
            }
            final int X = (int)event.getRawX();
            final int Y = (int)event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mXDelta = (int)(X - this.getTranslationX());
                    mYDelta = (int)(Y - this.getTranslationY());
//                    Log.i(TAG, "In ? "+chkTouchInside(this, X, Y));
                    break;

                case MotionEvent.ACTION_MOVE:
                    this.setTranslationX(X-mXDelta);
                    this.setTranslationY(Y-mYDelta);
                    break;

                case MotionEvent.ACTION_UP:

                    break;
            }
            return true;
        } else {
            return false;
        }
    }
}
