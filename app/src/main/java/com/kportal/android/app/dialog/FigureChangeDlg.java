package com.kportal.android.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kportal.android.app.R;
import com.kportal.android.app.interfaces.FigureChangeListener;

import java.util.ArrayList;

/**
 * Created by KR8 on 2016-11-07.
 */

public class FigureChangeDlg extends Dialog implements View.OnClickListener{

    private ImageView mIvType1, mIvType2, mIvType3;
    private TextView mTvCancel;
    private FigureChangeListener mListener;
    private int nType;
    private ArrayList<ImageView> mArrTypes;

    public FigureChangeDlg(Context context, FigureChangeListener listener, int type) {
        super(context);
        this.mListener = listener;
        this.nType = type;
        mArrTypes = new ArrayList<ImageView>();
    }

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bottom_option_figure_dlg);
        this.setCanceledOnTouchOutside(false);
        setLayout();
    }

    private void setLayout() {
        mIvType1 = (ImageView)findViewById(R.id.iv_bottom_option_figure_type1);
        mIvType2 = (ImageView)findViewById(R.id.iv_bottom_option_figure_type2);
        mIvType3 = (ImageView)findViewById(R.id.iv_bottom_option_figure_type3);
        mTvCancel = (TextView)findViewById(R.id.tv_bottom_option_figure_cancel);

        mArrTypes.add(mIvType1);
        mArrTypes.add(mIvType2);
        mArrTypes.add(mIvType3);

        mIvType1.setOnClickListener(this);
        mIvType2.setOnClickListener(this);
        mIvType3.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_bottom_option_figure_type1:
                setType(0);
                mListener.onFigureChanged(1);
                this.dismiss();
                break;

            case R.id.iv_bottom_option_figure_type2:
                setType(1);
                mListener.onFigureChanged(2);
                this.dismiss();
                break;

            case R.id.iv_bottom_option_figure_type3:
                setType(2);
                mListener.onFigureChanged(3);
                this.dismiss();
                break;

            case R.id.tv_bottom_option_figure_cancel:
                mListener.onFigureChanged(0);
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
