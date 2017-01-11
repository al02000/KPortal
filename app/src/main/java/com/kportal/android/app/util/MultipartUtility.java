package com.kportal.android.app.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by KR8 on 2017-01-06.
 */

public class MultipartUtility {

//    private final String mBoundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection mHttpCon;
    private String mCharset;
    private OutputStream mOutputStream;
    private PrintWriter mWriter;

    public MultipartUtility(String requesURL, String mCharset) throws IOException {
        URL mUrl = new URL(requesURL);
        mHttpCon = (HttpURLConnection)mUrl.openConnection();

    }
}
