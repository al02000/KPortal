package com.kportal.android.app.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kportal.android.app.common.Cutil;
import com.kportal.android.app.common.Cvalue;
import com.kportal.android.app.dto.UserData;
import com.kportal.android.app.R;
import com.kportal.android.app.service.OnClearFromRecentService;
import com.kportal.android.app.util.MyProgress;


/**
 * Created by KR4 on 2016-10-17.
 * 메인화면 액티비티 클래스
 */

public class HomeActivity extends AppCompatActivity {

    private Context mContext;
    private LinearLayout mLlSplashFrame, mLlMainFrame, mLlProgressFrame, mLlSetUpFrame;
    private Animation mSplashTranslateLeft, mSetupTranslateLeft, mSetupTranslateRight;
    private long backPressedTime = 0;
    private ImageView mIvNotificationStatus, mIvAnotherState;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private LocationManager mLocationManager;
    private GPSListener mGpsListener;
    private double mGeoLat, mGeoLng;
    private boolean isGeoState = false, isSetupState = false, isFirst = true;
    private final String TAG = "ActLoading";
    private MyProgress mProgress;

    // UserData 저장용 SharedPreference
    private UserData mUserData;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.activity_main);
        setLayout();
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
    }

    private void setLayout() {
        mContext = getApplicationContext();
        mUserData = new UserData(mContext);
        mLlSplashFrame = (LinearLayout) findViewById(R.id.splash_frame);
        mSplashTranslateLeft = AnimationUtils.loadAnimation(mContext, R.anim.splash_translate_left);
        mLlMainFrame = (LinearLayout) findViewById(R.id.main_frame);
        mLlSetUpFrame = (LinearLayout) findViewById(R.id.setup_frame);
        mSetupTranslateLeft = AnimationUtils.loadAnimation(mContext, R.anim.setup_translate_left);
        mSetupTranslateRight = AnimationUtils.loadAnimation(mContext, R.anim.setup_translate_right);

        mWebView = (WebView) findViewById(R.id.main_web_view);
        mLlProgressFrame = (LinearLayout) findViewById(R.id.progressbar_frame);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mProgress = new MyProgress(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mGpsListener = new GPSListener();

        int size[] = Cutil.getViewSize(this);
        Cvalue.WidthPixel = size[0];
        Cvalue.HeightPixel = size[1];

        mIvNotificationStatus = (ImageView) findViewById(R.id.iv_main_setup_notification_toggle);
        mIvAnotherState = (ImageView)findViewById(R.id.iv_main_setup_another_toggle);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLlSplashFrame.startAnimation(mSplashTranslateLeft);
            }
        }, 2000);

        WebSettings webSet = mWebView.getSettings();
        webSet.setJavaScriptEnabled(true);
        webSet.setGeolocationEnabled(true);
        webSet.setJavaScriptCanOpenWindowsAutomatically(true);
        webSet.setUserAgentString(webSet.getUserAgentString() + " " + getPackageName());
        mWebView.addJavascriptInterface(new JSInterface(), "android");
        mWebView.loadUrl(Cvalue.MainDomain);

        //웹뷰 설정
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                if (!isFirst) {
                    mProgress.show();
                }
                isFirst = false;
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mProgress.dismiss();
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                new AlertDialog.Builder(view.getContext()).setTitle("네트워크 에러").setMessage("인터넷 설정을 확인해 주세요.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }
        });

        //웹 크롬 설정
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(view.getContext()).setTitle("Rproject").setMessage(message).setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        }).create().show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(view.getContext()).setTitle("Rproject").setMessage(message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                }).create().show();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    new AlertDialog.Builder(mWebView.getContext()).setTitle("환경설정").setMessage("위치정보 설정이 필요합니다.").setCancelable(false)
                            .setPositiveButton("GPS 설정", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(gpsOptionsIntent);
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
                } else {
                    callback.invoke(origin, true, false);
                }
            }
        });

        mSplashTranslateLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLlSplashFrame.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mSetupTranslateLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mLlSetUpFrame.bringToFront();
                if(mUserData.getNotificationSetting()){
                    mIvNotificationStatus.setSelected(true);
                }else{
                    mIvNotificationStatus.setSelected(false);
                }

                if(mUserData.getAnotherSetting()){
                    mIvAnotherState.setSelected(true);
                }else{
                    mIvAnotherState.setSelected(false);
                }
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                mLlSetUpFrame.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mSetupTranslateRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLlSetUpFrame.setVisibility(View.GONE);
                mLlMainFrame.bringToFront();
                mLlProgressFrame.bringToFront();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            mUserData.setStoragePermission(true);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("notice");
        FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(mWebView.getContext()).setTitle("권한설정").setMessage("지도를 사용하기 위해서 위치정보 권한이 필요합니다.").setCancelable(false)
                            .setPositiveButton("권한설정", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                                    startActivity(intent);
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
                }

                break;

            case 2:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(mWebView.getContext()).setTitle("권한설정").setMessage("포토맵을 사용하기 위해서 저장작업 권한이 필요합니다.").setCancelable(false)
                            .setPositiveButton("권한설정", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                                    startActivity(intent);
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
                }
                break;

            default:
                toastShort("onRequestPermissionsResultDefault");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isSetupState) {
            isSetupState = false;
            onSetupCloseClick(null);
            return;
        }

        if (mWebView.getUrl().contains("#")) {
            for (int i = 0; i < 2; i++) {
                mWebView.goBack();
            }
            return;
        }

        if (mWebView.getUrl().equals("http://dev-m.krauction.co.kr/")) {
            if (System.currentTimeMillis() > backPressedTime + 2000) {
                backPressedTime = System.currentTimeMillis();
                toastShort("한번 더 누르시면 종료됩니다.");
            } else if (System.currentTimeMillis() <= backPressedTime + 2000) {
                finish();
                System.exit(0);
            }
            return;
        }

        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public void notificationClick(View v) {
        if(mIvNotificationStatus.isSelected()) {
            mIvNotificationStatus.setSelected(false);
            mUserData.setNotificationSetting(false);
        } else {
            mIvNotificationStatus.setSelected(true);
            mUserData.setNotificationSetting(true);
        }
    }

    public void anotherClick(View v) {
        if(mIvAnotherState.isSelected()) {
            mIvAnotherState.setSelected(false);
            mUserData.setAnotherSetting(false);
        } else {
            mIvAnotherState.setSelected(true);
            mUserData.setAnotherSetting(true);
        }
    }

    public void onHomeClick(View v) {
        mWebView.loadUrl("http://dev-m.krauction.co.kr/");
    }

    public void onSetupOpenClick(View v) {
        isSetupState = true;
        mLlSetUpFrame.startAnimation(mSetupTranslateLeft);
        mLlSetUpFrame.setClickable(true);
    }

    public void onSetupCloseClick(View v) {
        isSetupState = false;
        mLlSetUpFrame.startAnimation(mSetupTranslateRight);
        mLlSetUpFrame.setClickable(false);
    }

    public void onPhotomapClick(View v) {
        Intent intent = new Intent(this, PhotoViewActivity.class);
        startActivity(intent);
    }

    public void notiBack(View v) {
        onSetupCloseClick(null);
    }

    public void onRefreshClick(View v) {
        mWebView.reload();
//        Intent cIntent = new Intent(this, TestActivity.class);
//        startActivity(cIntent);
    }

    public void toastShort(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }


    public void startLocationService() {
        isGeoState = false;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mGpsListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, mGpsListener);
    }

    public class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            mGeoLat = location.getLatitude();
            mGeoLng = location.getLongitude();
            isGeoState = true;
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }

    public class JSInterface {
        @JavascriptInterface
        public void setGeoState(boolean state) {
            isGeoState = state;
        }

        @JavascriptInterface
        public boolean getGeoState() {
            return isGeoState;
        }

        @JavascriptInterface
        public double getGeoLat() {
            return mGeoLat;
        }

        @JavascriptInterface
        public double getGeoLng() {
            return mGeoLng;
        }

        @JavascriptInterface
        public void startGeoLatLng() {
            startLocationService();
        }

        @JavascriptInterface
        public void finishGeoLatLng() {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.removeUpdates(mGpsListener);
        }

        @JavascriptInterface
        public void photoMapStart() {
            onPhotomapClick(null);
        }
    }
}
