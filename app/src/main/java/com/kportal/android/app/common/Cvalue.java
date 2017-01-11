package com.kportal.android.app.common;

import android.os.Environment;

/**
 * Created by KR8 on 2016-10-19.
 */

public class Cvalue {

    public static final String MainDomain = "http://dev-m.krauction.co.kr/";
//    public static final String DAUM_KEY = "8f67b4136d163d7b9787c448a3614daf";
//    public static final String GOOGLE_MAP_KEY = "AIzaSyD0lHUcgXvwKFGlmbDQEjiYvzWF-MslgXk";

    public static final int Permission1 = 101;
    public static final int Permission2 = 102;
    public static final int Permission3 = 103;
    public static final int Permission4 = 104;
    public static final int Permission5 = 105;
    public static final int Permission6 = 106;

    public static int WidthPixel;
    public static int HeightPixel;

    public static int nTouch = 0;

    public static final int MainOption1 = 101;
    public static final int MainOption2 = 102;
    public static final int MainOption3 = 103;
    public static final int MainOption4 = 104;

    public static final int MiddleOption1 = 1041;
    public static final int MiddleOption2 = 1042;
    public static final int MiddleOption3 = 1043;
    public static final int MiddleOption4 = 1044;
    public static final int MiddleOption5 = 1045;

    public static final String USER_DATA = "USER_DATA";
    public static final String USER_NAME = "USER_NAME";
    public static final String FCM_TOKEN = "FCM_TOKEN";
    public static final String IS_FIRST = "IS_FIRST";
    public static final String STORAGE_PERMISSION = "STORAGE_PERMISSION";
    public static final String NOTIFICATION_SETTING = "NOTIFICATION_SETTING";
    public static final String ANOTHER_SETTING = "ANOTHER_SETTING";

    public static final String BACKUP_USER_DATA = "BACKUP_USER_DATA.txt";
    public static final String BASE_FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/KPortal";

}
