package com.kportal.android.app.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kportal.android.app.R;

/**
 * Created by KR8 on 2016-12-07.
 */

public class PhotoMapExitDlg extends Activity {

    private TextView mTvOk, mTvCancel;

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photomap_exit_dlg);
        setLayout();
    }

    private void setLayout() {
        mTvOk = (TextView)findViewById(R.id.tv_photomap_exit_dlg_ok);
        mTvCancel = (TextView)findViewById(R.id.tv_photomap_exit_dlg_cancel);

        mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
