package com.kportal.android.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kportal.android.app.R;
import com.kportal.android.app.interfaces.TextChangedListener;

import java.util.ArrayList;

/**
 * Created by KR8 on 2016-11-07.
 */

public class TextStyleChangeDlg extends Dialog implements View.OnClickListener {

    private TextView mTvType1, mTvType2, mTvType3, mTvCancel;
    private TextChangedListener mListener;
    private int nType;
    private ArrayList<TextView> mArrTypes;

    public TextStyleChangeDlg(Context context, TextChangedListener listener, int type) {
        super(context);
        this.mListener = listener;
        this.nType = type;
        mArrTypes = new ArrayList<TextView>();
    }

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bottom_option_text_style_dlg);
        this.setCanceledOnTouchOutside(false);
        setLayout();
        setType(nType);
    }

    private void setLayout() {
        mTvType1 = (TextView)findViewById(R.id.tv_bottom_option_text_style_type1);
        mTvType2 = (TextView)findViewById(R.id.tv_bottom_option_text_style_type2);
        mTvType3 = (TextView)findViewById(R.id.tv_bottom_option_text_style_type3);

        mTvCancel = (TextView)findViewById(R.id.tv_bottom_option_text_style_cancel);

        mArrTypes.add(mTvType1);
        mArrTypes.add(mTvType2);
        mArrTypes.add(mTvType3);

        mTvType1.setOnClickListener(this);
        mTvType2.setOnClickListener(this);
        mTvType3.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bottom_option_text_style_type1:
                setType(0);
                mListener.onStyleChanged(nType);
                this.dismiss();
                break;

            case R.id.tv_bottom_option_text_style_type2:
                setType(1);
                mListener.onStyleChanged(nType);
                this.dismiss();
                break;

            case R.id.tv_bottom_option_text_style_type3:
                setType(2);
                mListener.onStyleChanged(nType);
                this.dismiss();
                break;

            case R.id.tv_bottom_option_text_style_cancel:
                mListener.onStyleChanged(nType);
                this.dismiss();
                break;
        }
    }

    private void setType(int pos) {
        for(int i=0; i<mArrTypes.size(); i++) {
            mArrTypes.get(i).setSelected(false);
        }
        mArrTypes.get(pos).setSelected(true);
        nType = pos;
    }
}
