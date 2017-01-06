package com.kportal.android.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kportal.android.app.R;
import com.kportal.android.app.interfaces.LineTypeChangeListener;

import java.util.ArrayList;

/**
 * Created by KR8 on 2016-11-04.
 */

public class LineTypeChangeDlg extends Dialog implements View.OnClickListener{

    private ImageView mIvType1, mIvType2, mIvType3, mIvType4;
    private TextView mTvCancel;
    private LineTypeChangeListener mListener;
    private int nType;
    private ArrayList<ImageView> mArrTypes;

    public LineTypeChangeDlg(Context context, LineTypeChangeListener listener, int type) {
        super(context);
        this.mListener = listener;
        this.nType = type;
        mArrTypes = new ArrayList<ImageView>();
    }

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bottom_option_line_dlg);
        this.setCanceledOnTouchOutside(false);
        setLayout();
        setType(nType);
    }

    private void setLayout() {
        mIvType1 = (ImageView)findViewById(R.id.iv_bottom_option_line_dlg_type1);
        mIvType2 = (ImageView)findViewById(R.id.iv_bottom_option_line_dlg_type2);
        mIvType3 = (ImageView)findViewById(R.id.iv_bottom_option_line_dlg_type3);
        mIvType4 = (ImageView)findViewById(R.id.iv_bottom_option_line_dlg_type4);
        mTvCancel = (TextView)findViewById(R.id.tv_bottom_option_line_dlg_cancel);

        mArrTypes.add(mIvType1);
        mArrTypes.add(mIvType2);
        mArrTypes.add(mIvType3);
        mArrTypes.add(mIvType4);

        mIvType1.setOnClickListener(this);
        mIvType2.setOnClickListener(this);
        mIvType3.setOnClickListener(this);
        mIvType4.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_bottom_option_line_dlg_type1:
                setType(0);
                mListener.onLineTypeChanged(nType);
                this.dismiss();
                break;

            case R.id.iv_bottom_option_line_dlg_type2:
                setType(1);
                mListener.onLineTypeChanged(nType);
                this.dismiss();
                break;

            case R.id.iv_bottom_option_line_dlg_type3:
                setType(2);
                mListener.onLineTypeChanged(nType);
                this.dismiss();
                break;

            case R.id.iv_bottom_option_line_dlg_type4:
                setType(3);
                mListener.onLineTypeChanged(nType);
                this.dismiss();
                break;

            case R.id.tv_bottom_option_line_dlg_cancel:
                setType(nType);
                mListener.onLineTypeChanged(nType);
                this.dismiss();
                break;
        }
    }

    public void setType(int pos) {
        for(int i=0; i<mArrTypes.size(); i++){
            mArrTypes.get(i).setSelected(false);
        }
        mArrTypes.get(pos).setSelected(true);
        nType = pos;
    }
}
