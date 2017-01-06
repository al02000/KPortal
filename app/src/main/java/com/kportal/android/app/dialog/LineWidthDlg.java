package com.kportal.android.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kportal.android.app.R;
import com.kportal.android.app.interfaces.WidthChangeListener;

/**
 * Created by KR8 on 2016-11-01.
 */

public class LineWidthDlg extends Dialog implements View.OnClickListener
{
    private TextView mTvCount, mTvCancel, mTvOk, mTvTitle;
    private SeekBar mSbWidth;
    private WidthChangeListener mListener;
    private int defaultWidth;
    private String mTitle;


    public LineWidthDlg(Context context, WidthChangeListener listener, int dWidth, String title) {
        super(context);
        this.mListener = listener;
        defaultWidth = dWidth;
        this.mTitle = title;
    }

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.line_width_dlg);
        this.setCanceledOnTouchOutside(false);
        setLayout();


        mSbWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvCount.setText(String.valueOf(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.i("TEST", "Stop Tracking");
//                seekBar.setSelected(false);
            }
        });
    }

    public void setLayout() {
        mSbWidth = (SeekBar)findViewById(R.id.sb_line_width_dlg);
        mTvCancel = (TextView)findViewById(R.id.tv_line_width_dlg_cancel);
        mTvCount = (TextView)findViewById(R.id.tv_line_width_dlg);
        mTvOk = (TextView)findViewById(R.id.tv_line_width_dlg_ok);
        mTvTitle = (TextView)findViewById(R.id.tv_line_width_dlg_title);

        mTvTitle.setText(mTitle);
        mSbWidth.setProgress(defaultWidth);
        mTvCount.setText(String.valueOf(defaultWidth));

        mTvCancel.setOnClickListener(this);
        mTvOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_line_width_dlg_ok:
                mListener.widthChanged(Integer.parseInt(mTvCount.getText().toString()));
                this.dismiss();
                break;

            case R.id.tv_line_width_dlg_cancel:
                mListener.widthChanged(0);
                this.dismiss();
                break;
        }
    }
}
