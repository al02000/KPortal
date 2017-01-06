package com.kportal.android.app.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kportal.android.app.R;

/**
 * Created by KR8 on 2016-12-08.
 */

public class ShareViewChoiceDialog extends Activity implements View.OnClickListener{

    private TextView mTvCancel;
    private ImageView mIvCurrentView, mIvGallery;
    private Intent mShareIntent;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shareview_choice_dialog);
        setLayout();
    }

    private void setLayout() {
        mTvCancel = (TextView)findViewById(R.id.tv_shareView_choice_dialog_cancel);
        mIvCurrentView = (ImageView)findViewById(R.id.iv_shareView_choice_dialog_current);
        mIvGallery = (ImageView)findViewById(R.id.iv_shareView_choice_dialog_gallery);
        mShareIntent = new Intent();

        mTvCancel.setOnClickListener(this);
        mIvCurrentView.setOnClickListener(this);
        mIvGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_shareView_choice_dialog_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.iv_shareView_choice_dialog_current:
                mShareIntent.putExtra("share", 0);
                setResult(RESULT_OK, mShareIntent);
                finish();
                break;

            case R.id.iv_shareView_choice_dialog_gallery:
                mShareIntent.putExtra("share", 1);
                setResult(RESULT_OK, mShareIntent);
                finish();
                break;
        }
    }
}
