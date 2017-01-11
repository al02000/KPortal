package com.kportal.android.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kportal.android.app.R;
import com.kportal.android.app.interfaces.OnColorChangedListener;

import java.util.ArrayList;

/**
 * Created by KR8 on 2016-10-25.
 */

public class ColorPickerDialog extends Dialog implements View.OnClickListener{

    public static OnColorChangedListener mListener;
    private int mInitialColor;
//    private final String TAG = "ColorPickerDialog";
    private ImageView mIvType1, mIvType2, mIvType3, mIvType4, mIvType5, mIvType6, mIvType7, mIvType8, mIvType9, mIvType10, mIvType11, mIvType12, mIvType13, mIvType14, mIvType15, mIvType16, mIvType17, mIvType18,
            mIvType19, mIvType20, mIvPreColor, mIvSelectColor;
    private TextView mTvOk, mTvCancel;
    private ArrayList<Integer> mArrColors;
    private int nSelect;
    private Context mContext;

    public ColorPickerDialog(Context context, OnColorChangedListener listener, int initialColor) {
        super(context);
        mListener = listener;
        mInitialColor = initialColor;
        mArrColors = new ArrayList<Integer>();
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle saved) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(saved);
        setContentView(R.layout.color_pic_dialog);
        this.setCanceledOnTouchOutside(false);
        setLayout();
    }

    private void setLayout() {
        mTvOk = (TextView)findViewById(R.id.tv_color_pic_dialog_ok);
        mTvCancel = (TextView)findViewById(R.id.tv_color_pic_dialog_cancel);
        mIvType1 = (ImageView)findViewById(R.id.iv_color_dlg_1);
        mIvType2 = (ImageView)findViewById(R.id.iv_color_dlg_2);
        mIvType3 = (ImageView)findViewById(R.id.iv_color_dlg_3);
        mIvType4 = (ImageView)findViewById(R.id.iv_color_dlg_4);
        mIvType5 = (ImageView)findViewById(R.id.iv_color_dlg_5);
        mIvType6 = (ImageView)findViewById(R.id.iv_color_dlg_6);
        mIvType7 = (ImageView)findViewById(R.id.iv_color_dlg_7);
        mIvType8 = (ImageView)findViewById(R.id.iv_color_dlg_8);
        mIvType9 = (ImageView)findViewById(R.id.iv_color_dlg_9);
        mIvType10 = (ImageView)findViewById(R.id.iv_color_dlg_10);
        mIvType11 = (ImageView)findViewById(R.id.iv_color_dlg_11);
        mIvType12 = (ImageView)findViewById(R.id.iv_color_dlg_12);
        mIvType13 = (ImageView)findViewById(R.id.iv_color_dlg_13);
        mIvType14 = (ImageView)findViewById(R.id.iv_color_dlg_14);
        mIvType15 = (ImageView)findViewById(R.id.iv_color_dlg_15);
        mIvType16 = (ImageView)findViewById(R.id.iv_color_dlg_16);
        mIvType17 = (ImageView)findViewById(R.id.iv_color_dlg_17);
        mIvType18 = (ImageView)findViewById(R.id.iv_color_dlg_18);
        mIvType19 = (ImageView)findViewById(R.id.iv_color_dlg_19);
        mIvType20 = (ImageView)findViewById(R.id.iv_color_dlg_20);
        mIvPreColor = (ImageView)findViewById(R.id.iv_color_dlg_pre_color);
        mIvSelectColor = (ImageView)findViewById(R.id.iv_color_dlg_select_color);

        mIvPreColor.setBackgroundColor(mInitialColor);

        mIvType1.setOnClickListener(this);
        mIvType2.setOnClickListener(this);
        mIvType3.setOnClickListener(this);
        mIvType4.setOnClickListener(this);
        mIvType5.setOnClickListener(this);
        mIvType6.setOnClickListener(this);
        mIvType7.setOnClickListener(this);
        mIvType8.setOnClickListener(this);
        mIvType9.setOnClickListener(this);
        mIvType10.setOnClickListener(this);
        mIvType11.setOnClickListener(this);
        mIvType12.setOnClickListener(this);
        mIvType13.setOnClickListener(this);
        mIvType14.setOnClickListener(this);
        mIvType15.setOnClickListener(this);
        mIvType16.setOnClickListener(this);
        mIvType17.setOnClickListener(this);
        mIvType18.setOnClickListener(this);
        mIvType19.setOnClickListener(this);
        mIvType20.setOnClickListener(this);
        mTvOk.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);

        mArrColors.add(ContextCompat.getColor(mContext, R.color.app_black));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_2));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_3));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_4));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_5));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.app_white));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_7));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_8));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_9));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_10));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_11));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_12));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_13));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_14));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_15));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_16));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_17));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_18));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_19));
        mArrColors.add(ContextCompat.getColor(mContext, R.color.color_dlg_type_20));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_color_pic_dialog_ok:
                mListener.colorChanged(getSelectColor(nSelect));
                this.dismiss();
                break;

            case R.id.tv_color_pic_dialog_cancel:
                mListener.colorChanged(mInitialColor);
                this.dismiss();
                break;

            case R.id.iv_color_dlg_1:
                nSelect = 0;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_2:
                nSelect = 1;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_3:
                nSelect = 2;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_4:
                nSelect = 3;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_5:
                nSelect = 4;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_6:
                nSelect = 5;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_7:
                nSelect = 6;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_8:
                nSelect = 7;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_9:
                nSelect = 8;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_10:
                nSelect = 9;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_11:
                nSelect = 10;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_12:
                nSelect = 11;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_13:
                nSelect = 12;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_14:
                nSelect = 13;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_15:
                nSelect = 14;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_16:
                nSelect = 15;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_17:
                nSelect = 16;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_18:
                nSelect = 17;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_19:
                nSelect = 18;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;

            case R.id.iv_color_dlg_20:
                nSelect = 19;
                mListener.colorChanged(getSelectColor(nSelect));
                mIvSelectColor.setBackgroundColor(getSelectColor(nSelect));
                break;
        }
    }

    private int getSelectColor(int pos) {
        return mArrColors.get(pos);
    }
}
