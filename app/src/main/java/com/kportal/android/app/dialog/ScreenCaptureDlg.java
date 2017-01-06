package com.kportal.android.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kportal.android.app.R;
import com.kportal.android.app.interfaces.DlgClickListener;

/**
 * Created by KR8 on 2016-11-16.
 */

public class ScreenCaptureDlg extends Dialog implements View.OnClickListener{

    private TextView mTvOk, mTvCancel;
    private DlgClickListener mListener;

    public ScreenCaptureDlg(Context con, DlgClickListener listener) {
        super(con);
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.screen_capture_dlg);
        this.setCanceledOnTouchOutside(false);
        setLayout();
    }

    private void setLayout() {
        mTvOk = (TextView)findViewById(R.id.tv_screen_capture_dlg_ok);
        mTvCancel = (TextView)findViewById(R.id.tv_screen_capture_dlg_cancel);

        mTvOk.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        mListener.onDlgClick(0);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_screen_capture_dlg_ok:
                mListener.onDlgClick(1);
                this.dismiss();
                break;

            case R.id.tv_screen_capture_dlg_cancel:
                mListener.onDlgClick(0);
                this.dismiss();
                break;
        }
    }
}
