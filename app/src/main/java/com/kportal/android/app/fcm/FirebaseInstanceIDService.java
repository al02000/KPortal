package com.kportal.android.app.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kportal.android.app.dto.UserData;

/**
 * Created by KR8 on 2016-11-29.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

//    private final String TAG = "IDService";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
//        Log.i(TAG, "Token > "+token);
        UserData mUserData = new UserData(getApplicationContext());
        mUserData.setToken(token);
        mUserData.setIsFirst(false);
    }
}
