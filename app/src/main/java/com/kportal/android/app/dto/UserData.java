package com.kportal.android.app.dto;

import android.content.Context;
import android.content.SharedPreferences;

import com.kportal.android.app.common.Cvalue;

/**
 * Created by KR8 on 2016-12-01.
 */

public class UserData {

    private Context mContext;
    private SharedPreferences mShared;
    private SharedPreferences.Editor mEditor;

    public UserData(Context context) {
        this.mContext = context;
        mShared = mContext.getSharedPreferences(Cvalue.USER_DATA, Context.MODE_PRIVATE);
        mEditor = mShared.edit();
    }

    public void setUserName(String name) {
        mEditor.putString(Cvalue.USER_NAME, name);
        mEditor.commit();
    }

    public String getUserName() {
        String name = "";
        name = mShared.getString(Cvalue.USER_NAME, "");
        return name;
    }

    public void setToken(String token) {
        mEditor.putString(Cvalue.FCM_TOKEN, token);
        mEditor.commit();
    }

    public String getToken() {
        String token = "";
        token = mShared.getString(Cvalue.FCM_TOKEN, "");
        return token;
    }

    public void setIsFirst(boolean isFirst) {
        mEditor.putBoolean(Cvalue.IS_FIRST, isFirst);
        mEditor.commit();
    }

    public boolean getIsFirst() {
        boolean isFirst = true;
        isFirst = mShared.getBoolean(Cvalue.IS_FIRST, true);
        return isFirst;
    }

    public void setStoragePermission(boolean isStorage) {
        mEditor.putBoolean(Cvalue.STORAGE_PERMISSION, isStorage);
        mEditor.commit();
    }

    public boolean getStoragePermission() {
        boolean isStorage = false;
        isStorage = mShared.getBoolean(Cvalue.STORAGE_PERMISSION, isStorage);
        return isStorage;
    }

    public void setNotificationSetting(boolean isNotification){
        mEditor.putBoolean(Cvalue.NOTIFICATION_SETTING, isNotification);
        mEditor.commit();
    }

    public boolean getNotificationSetting(){
        boolean isNotification = false;
        isNotification = mShared.getBoolean(Cvalue.NOTIFICATION_SETTING, isNotification);
        return isNotification;
    }

    public void setAnotherSetting(boolean isAnother){
        mEditor.putBoolean(Cvalue.ANOTHER_SETTING, isAnother);
        mEditor.commit();
    }

    public boolean getAnotherSetting(){
        boolean isAnother = false;
        isAnother = mShared.getBoolean(Cvalue.ANOTHER_SETTING, isAnother);
        return isAnother;
    }
}
