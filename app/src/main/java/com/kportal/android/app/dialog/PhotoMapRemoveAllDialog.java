package com.kportal.android.app.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kportal.android.app.R;

/**
 * Created by KR8 on 2016-12-09.
 */

public class PhotoMapRemoveAllDialog extends Activity implements View.OnClickListener{

    private TextView mTvOk, mTvCancel;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photomap_remove_all_dialog);
        setLayout();
    }

    private void setLayout() {
        mTvOk = (TextView)findViewById(R.id.tv_photomap_remove_all_dialog_ok);
        mTvCancel = (TextView)findViewById(R.id.tv_photomap_remove_all_dialog_cancel);

        mTvOk.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_photomap_remove_all_dialog_ok:
                setResult(RESULT_OK);
                finish();
                break;

            case R.id.tv_photomap_remove_all_dialog_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}
