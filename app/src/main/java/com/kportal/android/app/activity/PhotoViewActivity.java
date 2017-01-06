package com.kportal.android.app.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kportal.android.app.dialog.PhotoMapExitDlg;
import com.kportal.android.app.dialog.PhotoMapRemoveAllDialog;
import com.kportal.android.app.dialog.ShareViewChoiceDialog;
import com.kportal.android.app.dto.UserData;
import com.kportal.android.app.R;
import com.kportal.android.app.common.Cutil;
import com.kportal.android.app.common.Cvalue;
import com.kportal.android.app.dialog.FigureChangeDlg;
import com.kportal.android.app.dialog.LineTypeChangeDlg;
import com.kportal.android.app.dialog.LineWidthDlg;
import com.kportal.android.app.dialog.ScreenCaptureDlg;
import com.kportal.android.app.dialog.TextStyleChangeDlg;
import com.kportal.android.app.interfaces.DlgClickListener;
import com.kportal.android.app.interfaces.FigureChangeListener;
import com.kportal.android.app.interfaces.LineTypeChangeListener;
import com.kportal.android.app.interfaces.OnColorChangedListener;
import com.kportal.android.app.interfaces.TextChangedListener;
import com.kportal.android.app.interfaces.WidthChangeListener;
import com.kportal.android.app.util.MyProgress;
import com.kportal.android.app.util.ViewUnbindHelper;
import com.kportal.android.app.dialog.ColorPickerDialog;
import com.kportal.android.app.view.MyDrawView;
import com.kportal.android.app.view.MyEditText;
import com.kportal.android.app.view.MyTextView;
import com.kportal.android.app.view.MyTouchView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by KR8 on 2016-10-24.
 */

public class PhotoViewActivity extends AppCompatActivity
        implements View.OnClickListener, OnColorChangedListener, WidthChangeListener, LineTypeChangeListener, FigureChangeListener, TextChangedListener,
        DlgClickListener, OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SensorEventListener{

    private final String TAG = "PhotoViewActivity";
    private final int CAMERA = 1001, GALLERY = 1002, EXIT = 1003, SHARE = 1004, GALLERY_SHARE = 1005, REMOVE_EDIT=1006;

    private ImageView mIvBack, mIvChoice, mIvSave, mIvShare, mIvEdit, mIvCamera, mIvGallery, mIvSelectImg, mIvPen, mIvText, mIvFigure, mIvEraser, mIvEditText, mIvDelFigure;
    private RelativeLayout mRlParent, mRlBottomArrow;
    private LinearLayout mLlImageChoice, mLlEditItem, mLlBottomMenu, mLlBottomItem;
    private FrameLayout mFlEditText;
    private MyEditText mEtEdit;
    private FrameLayout rlEditText;
    private Uri mCropedImageUri;
    private MyTouchView mView;
    private MyDrawView mFigureView;
    private MyTextView mEditText;
    private ArrayList<ImageView> mArrBigOptions, mArrTitleBts;
    private boolean isText;
    private ArrayList<View> mArrBottomOptions;
    private LinearLayout mLlImageAlphaChange;
    private TextView mTvImageAlphaChange, mTvChoiceCancel;
    private SeekBar mSbImageAlphaChange;
    private int nSelectMode;
    private File mFile;
    private ImageView mIvCurrentColor, mIvEditClose;
    private LinearLayout mLlImageAlphaChangeParent;
    private RelativeLayout mRlImageAlphaChangeArrow;
    private boolean isImage = false, isFirst = true;
    private ArrayList<MyTextView> mArrTextViews = new ArrayList<MyTextView>();
    private LinearLayout mLlSelectType;
    private ImageView mIvSelectType1, mIvSelectType2;
    private ImageView mIvBlack;
    private Context mContext;
    private Activity act;
    private int nLineType = 0;
    private boolean isMap = false, isEraserMode = false;
    private int nTextColor = -16777216, nTextSize = 12, nTextStyle = 0;
    private int nBeforeTextColor = -16777216, nBeforeLineColor = -16777216, nBeforeFigureColor = -16777216;
    private UserData mUserData;

    // 파일 쓰기 & 읽기 권한 타입(카메라, 갤러리, 저장, 공유)
    private final int REQUEST_CODE_STORAGE_CAMERA = 4001, REQUEST_CODE_STORAGE_GALLERY = 4002, REQUEST_CODE_STORAGE_SAVE = 4003, REQUEST_CODE_STORAGE_SHARE = 4004;

    //GoogleMap Setting
    private final LatLng SEOUL = new LatLng(37.56, 126.97);
    private final int REQUEST_CODE_LOCATION = 2000;
    private final int REQUEST_CODE_GPS = 2001;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient = null;
    private boolean setGPS = false;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private MapFragment mMapFragment;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float[] mRotationMatrix = new float[16];
    private float mDeclination;
    private float mZoomLevel = 17.0f;
    private LatLng mCurrentLatLng = new LatLng(37.56, 126.97);
    private ImageView mIvMapControl;
    private boolean isMapShow = true, isMapControlShow = true, isMapReady = false;
    private ImageView mIvMapCameraControl;
    private int mMapCameraMode;
    private final int CAMERA_MODE_CURRENT_POSITION = 1, CAMERA_MODE_ANOTHER_POSITION = 2, CAMERA_MODE_COMPASS_OFF = 3, CAMERA_MODE_COMPASS_ON = 4, CAMERA_MODE_COMPASS_OFF_FROM_USER = 5;
    private double mBeforeBearing = 0;
    private MarkerOptions mBearingMarker;
    //////////////////////////////////////////////////////

    //구글맵 Api 연결 설정
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    //GPS 활성화를 위한 다이얼로그 보여주기
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS가 비활성화 상태입니다. 활성화 하겠습니까")
                .setCancelable(false)
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(callGPSSettingIntent, REQUEST_CODE_GPS);
                    }
                });
        alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    boolean isSensor = false;
    Timer timer = new Timer();
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.photoview_activity);
        Log.e("TEST", "onCreate");
        mContext = getApplicationContext();
        act = this;
        setLayout();
        mUserData = new UserData(mContext);
        //버전 호환 문제로 딜레이매서드 호출 대신 타이머로 대체
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                isSensor = true;
            }
        }, 0, 1000);

        //센서 매니저 설정(19버전 이하버전에서는 딜레이 매서드가 없어 분기처리
//        if(Build.VERSION.SDK_INT >= 19){
//            mSensorManager.registerListener(this, mSensor, 1000000, 1000000);
//        }else{
            mSensorManager.registerListener(this, mSensor, 1000000);
//        }
    }

    //기본 셋팅
    private void setLayout() {
        mRlParent = (RelativeLayout)findViewById(R.id.rl_photoView_activity_parent);
        mIvBack = (ImageView)findViewById(R.id.iv_photoView_activity_back);
        mIvChoice = (ImageView)findViewById(R.id.iv_photoView_activity_choice);
        mIvSave = (ImageView)findViewById(R.id.iv_photoView_activity_save);
        mIvShare = (ImageView)findViewById(R.id.iv_photoView_activity_share);
        mIvEdit = (ImageView)findViewById(R.id.iv_photoView_activity_edit);
        mIvCamera = (ImageView)findViewById(R.id.iv_photoView_activity_camera);
        mIvGallery = (ImageView)findViewById(R.id.iv_photoView_activity_gallery);
        mIvSelectImg = (ImageView)findViewById(R.id.iv_photoView_activity_selectImg);
        mIvPen = (ImageView)findViewById(R.id.iv_photoView_activity_pen);
        mIvText = (ImageView)findViewById(R.id.iv_photoView_activity_text);
        mIvFigure = (ImageView)findViewById(R.id.iv_photoView_activity_figure);
        mLlImageChoice = (LinearLayout)findViewById(R.id.ll_photoView_activity_imgChoice);
        mLlEditItem = (LinearLayout)findViewById(R.id.ll_photoView_activity_editItem);
        mView = (MyTouchView) findViewById(R.id.editView);
        mLlBottomMenu = (LinearLayout)findViewById(R.id.ll_photoView_activity_editMenu);
        mLlBottomItem = (LinearLayout)findViewById(R.id.ll_photoView_activity_editMenu_item);
        mRlBottomArrow = (RelativeLayout)findViewById(R.id.rl_photoView_activity_editMenu_arrow);
        mFlEditText = (FrameLayout) findViewById(R.id.ll_photoView_activity_editText);
        mIvEraser = (ImageView)findViewById(R.id.iv_photoView_activity_eraser);
        mFigureView = (MyDrawView)findViewById(R.id.figureView);
        mTvChoiceCancel = (TextView)findViewById(R.id.tv_photoView_activity_choice_cancel);
        mIvDelFigure = (ImageView)findViewById(R.id.iv_delete_figure);
        mIvEditClose = (ImageView)findViewById(R.id.iv_photoView_activity_close_edit);
        rlEditText = (FrameLayout) findViewById(R.id.rl_photoView_activity_editText);
        mLlImageAlphaChange = (LinearLayout)findViewById(R.id.ll_photoView_activity_imgAlpha_change);
        mTvImageAlphaChange = (TextView)findViewById(R.id.tv_photoView_activity_imgAlpha_change);
        mSbImageAlphaChange = (SeekBar)findViewById(R.id.sb_photoView_activity_imgAlpha_change);
        mLlImageAlphaChangeParent = (LinearLayout)findViewById(R.id.ll_photoView_activity_imgAlpha_change_parent);
        mRlImageAlphaChangeArrow = (RelativeLayout)findViewById(R.id.rl_photoView_activity_image_alpha_change);
        mIvMapControl = (ImageView)findViewById(R.id.iv_photoView_activity_map);
        mIvMapCameraControl = (ImageView)findViewById(R.id.iv_photoView_activity_map_control);
        mMapCameraMode = CAMERA_MODE_COMPASS_ON;

        mLlSelectType = (LinearLayout)findViewById(R.id.ll_photoView_activity_selectType);
        mIvSelectType1 = (ImageView)findViewById(R.id.iv_photoView_activity_type1);
        mIvSelectType2 = (ImageView)findViewById(R.id.iv_photoView_activity_type2);
        mIvBlack = (ImageView)findViewById(R.id.iv_photoview_activity_black);

        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        mMapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.photoView_map);
        mMapFragment.getMapAsync(this);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        mSbImageAlphaChange.setProgress(40);
        nSelectMode = 0;

        mArrBigOptions = new ArrayList<ImageView>();
        mArrTitleBts = new ArrayList<>();
        mArrBigOptions.add(mIvPen);
        mArrBigOptions.add(mIvText);
        mArrBigOptions.add(mIvFigure);
        mArrBigOptions.add(mIvEraser);

        mArrTitleBts.add(mIvChoice);
        mArrTitleBts.add(mIvSave);
        mArrTitleBts.add(mIvShare);
        mArrTitleBts.add(mIvEdit);

        mTvImageAlphaChange.setText("투명도 40%");
        isText = false;

        mLlEditItem.setVisibility(View.GONE);
        mLlImageChoice.setVisibility(View.GONE);
        mLlBottomMenu.setVisibility(View.GONE);
        mIvBack.setOnClickListener(this);
        mIvChoice.setOnClickListener(this);
        mIvSave.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mIvEdit.setOnClickListener(this);
        mIvCamera.setOnClickListener(this);
        mIvGallery.setOnClickListener(this);
        mIvPen.setOnClickListener(this);
        mIvText.setOnClickListener(this);
        mIvFigure.setOnClickListener(this);
        mRlBottomArrow.setOnClickListener(this);
        mIvEraser.setOnClickListener(this);
        mTvChoiceCancel.setOnClickListener(this);
        mIvDelFigure.setOnClickListener(this);
        mIvEditClose.setOnClickListener(this);
        mRlImageAlphaChangeArrow.setOnClickListener(this);
        mIvSelectType1.setOnClickListener(this);
        mIvSelectType2.setOnClickListener(this);
        mIvMapControl.setOnClickListener(this);
        mIvMapCameraControl.setOnClickListener(this);

        mSbImageAlphaChange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvImageAlphaChange.setText("투명도 "+progress+"%");
                mIvSelectImg.setAlpha((float)(100-progress)/100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public boolean checkLocationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
                }
                return false;
            } else {
                if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS) {
                    showGPSDisabledAlertToUser();
                }
                if(mGoogleApiClient == null) {
                    buildGoogleApiClient();
                }
                mGoogleApiClient.reconnect();
                mGoogleMap.setMyLocationEnabled(true);
            }
        } else {
            if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS) {
                showGPSDisabledAlertToUser();
            }
            if(mGoogleApiClient == null) {
                buildGoogleApiClient();
            }
            mGoogleMap.setMyLocationEnabled(true);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //퍼미션이 허가된 경우
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS) {
                            showGPSDisabledAlertToUser();
                        }
                        if(mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "퍼미션 취소", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_STORAGE_CAMERA:
                onRequestStoragePermissionResult(REQUEST_CODE_STORAGE_CAMERA, grantResults);
                break;

            case REQUEST_CODE_STORAGE_GALLERY:
                onRequestStoragePermissionResult(REQUEST_CODE_STORAGE_GALLERY, grantResults);
                break;

            case REQUEST_CODE_STORAGE_SAVE:
                onRequestStoragePermissionResult(REQUEST_CODE_STORAGE_SAVE, grantResults);
                break;

            case REQUEST_CODE_STORAGE_SHARE:
                onRequestStoragePermissionResult(REQUEST_CODE_STORAGE_SHARE, grantResults);
                break;
        }
    }

    private void onRequestStoragePermissionResult(final int nRequestCode, int[] grantResults) {
        String message = "";
        if(nRequestCode == REQUEST_CODE_STORAGE_CAMERA) {
            message = "카메라 기능을 사용하기 위해선 권한이 필요 합니다.";
        } else if(nRequestCode == REQUEST_CODE_STORAGE_GALLERY){
            message = "갤러리 기능을 사용하기 위해선 권한이 필요 합니다.";
        }else if(nRequestCode == REQUEST_CODE_STORAGE_SAVE){
            message = "저장 기능을 사용하기 위해선 권한이 필요 합니다.";
        }else if(nRequestCode == REQUEST_CODE_STORAGE_SHARE){
            message = "공유 기능을 사용하기 위해선 권한이 필요 합니다.";
        }
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            makeBaseFolder();
            mUserData.setStoragePermission(true);
            setPermissionResult(nRequestCode);
        } else {
            new AlertDialog.Builder(act).setTitle("권한 설정").setMessage(message).setCancelable(false)
                .setPositiveButton("권한설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:"+mContext.getPackageName()));
                        startActivityForResult(intent, nRequestCode);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mUserData.setStoragePermission(false);
                    }
            }).create().show();
        }
    }


    //구글 맵 객체를 정상적으로 받아왔을때 호출되는 메서드, 현재위치정보 X
    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        final Animation showAni = AnimationUtils.loadAnimation(act, R.anim.map_control_show_animation);
        final Animation hideAni = AnimationUtils.loadAnimation(act, R.anim.map_control_hide_animation);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(mZoomLevel));
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);

        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkLocationPermission();
                }else{
                    if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS) {
                        showGPSDisabledAlertToUser();
                    }
                    if(mGoogleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    mGoogleMap.setMyLocationEnabled(true);
                }
                startTracking(isMapShow);
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(mZoomLevel));
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        //구글맵의 카메라시점 변경 이벤트
        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if(i==GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    //사용자가 손으로 터치하여서 움직였을때 호출
                    mMapCameraMode = CAMERA_MODE_ANOTHER_POSITION;
                    typeWithMap(mMapCameraMode);
                } else if(i == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION && mMapCameraMode == CAMERA_MODE_ANOTHER_POSITION) {
                    //사용자 터치가 아닌 다른요소에 의해서 움직였을때 호출
                    mMapCameraMode = CAMERA_MODE_CURRENT_POSITION;
                    mIvMapCameraControl.setBackgroundResource(R.drawable.map_current_position_on);
                }
            }
        });

        //구글맵 클릭 (옵션버튼 보여주기 / 사라지기)
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(isMapControlShow) {
                    isMapControlShow = false;
                    mIvMapCameraControl.startAnimation(hideAni);
                    mIvMapControl.startAnimation(hideAni);
                } else {
                    isMapControlShow = true;
                    mIvMapCameraControl.startAnimation(showAni);
                    mIvMapControl.startAnimation(showAni);
                }
            }
        });

        hideAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIvMapCameraControl.setVisibility(View.GONE);
                mIvMapControl.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        showAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIvMapCameraControl.setVisibility(View.VISIBLE);
                mIvMapControl.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //위치정보를 받아왔을때 호출되는 메서드
    @Override
    public void onConnected(Bundle bundle) {
        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            setGPS = true;
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(1000);
//        Log.i("TEST", "LocationRequest ==> "+mLocationRequest.toString());

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(!mGoogleApiClient.isConnected()) mGoogleApiClient.connect();

            if(setGPS && mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if(location == null) {
                    return;
                }
                mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mBearingMarker = new MarkerOptions()
                        .position(mCurrentLatLng)
                        .alpha(0.0f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_way_bg));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mCurrentLatLng));
                isMapReady = true;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //구글 플레이 서비스 연결이 해제되었을 때, 재연결 시도
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //연결 실패
//        Log.e(TAG, "Connection Failed ==> "+connectionResult.getErrorCode());
    }

    //위치정보를 주기적으로 전송해준다.
    @Override
    public void onLocationChanged(Location location) {
        String errorMessage = "";
        mCurrentLatLng = new LatLng((float)location.getLatitude(), (float)location.getLongitude());
        GeomagneticField field = new GeomagneticField(
                (float)location.getLatitude(),
                (float)location.getLongitude(),
                (float)location.getAltitude(),
                System.currentTimeMillis()
        );
        mDeclination = field.getDeclination();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("TEST", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TEST", "onResume");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                mUserData.setStoragePermission(false);
            }else{
                mUserData.setStoragePermission(true);
            }
        }else{
            mUserData.setStoragePermission(true);
        }
        if(isFirst) {
            isFirst = false;
            return;
        }
        if(getSelectedTitleButton() != 4) {
            typeWithMap(mMapCameraMode);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        stopTracking();
        Log.e("TEST", "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TEST", "onPause");
    }

    @Override
    protected void onDestroy() {
        Log.e("TEST", "onDestroy");
        Cutil.recycleBitmap(mIvSelectImg);
        Cvalue.nTouch = 0;
        mContext = null;
        mView.destroyDrawingCache();
        mView = null;
        mFigureView.destroyDrawingCache();
        mFigureView = null;
        isFirst = true;
        mRlParent.removeAllViews();
        mRlParent = null;
        mSensorManager.unregisterListener(this);
        ViewUnbindHelper.unbindReferences(getWindow().getDecorView());
        if(mGoogleApiClient != null){
            mGoogleApiClient.unregisterConnectionFailedListener(this);
            mGoogleApiClient.unregisterConnectionCallbacks(this);
            if(mGoogleApiClient.isConnected()){
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
        super.onDestroy();
    }

    //방향센서의 결과 전송 메서드
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!isSensor){
            return;
        }
        isSensor = false;
        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
//            Log.i(TAG, "Sensor");
            SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(mRotationMatrix, orientation);
            mBeforeBearing = Math.toDegrees(orientation[0])+mDeclination;
            updateCamera(mBeforeBearing);
        }
    }

    //방향 센서의 결과값으로 헤더의 방향을 새로 그려준다
    public void updateCamera(double bearing) {
        if(isMapReady) {
            mGoogleMap.clear();
            if(mMapCameraMode == CAMERA_MODE_COMPASS_ON) {
                CameraPosition oldPos = mGoogleMap.getCameraPosition();
                CameraPosition pos = CameraPosition.builder(oldPos)
                        .bearing((float)bearing)
                        .tilt(0)
                        .build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos), 500, null);
                setBearingRotation(mBearingMarker, true, bearing);
            }else{
                setBearingRotation(mBearingMarker, false, bearing);
            }
        }
    }

    private void setBearingRotation(MarkerOptions markerOptions, boolean compassMode, double bearing) {
        markerOptions.position(mCurrentLatLng)
                .alpha(0.5f);
        if(compassMode){
            markerOptions.rotation(0)
                    .flat(false);
        }else{
            markerOptions.rotation((float)bearing)
                    .flat(true);
        }
        mGoogleMap.addMarker(markerOptions);
    }

    //방향 센서가 처음 연결되었을때 호출됨
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        Log.i(TAG, "onAccuracyChanged");
    }


    private void requestStoragePermission(final int requestCode){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //첫화면 촬영 / 갤러리 버튼
            case R.id.iv_photoView_activity_type1:
                if(mLlImageChoice.getVisibility() == View.VISIBLE) {
                    mLlImageChoice.setVisibility(View.GONE);
                } else {
                    mRlParent.bringChildToFront(mLlImageChoice);
                    mLlImageChoice.setVisibility(View.VISIBLE);
                }
                mLlSelectType.setVisibility(View.GONE);
                break;

            //첫화면 지도보기 버튼
            case R.id.iv_photoView_activity_type2:
                isImage = false;
                isMap = true;
                mLlSelectType.setVisibility(View.GONE);
                mIvBlack.setVisibility(View.GONE);
                break;

            //포토맵 뒤로가기 버튼
            case R.id.iv_photoView_activity_back:
                onBackPressed();
                break;

            //타이틀바 (사진가져오기)
            case R.id.iv_photoView_activity_choice:
                setTypeWithView(Cvalue.MainOption1);
                if(mLlImageChoice.getVisibility() == View.VISIBLE) {
                    mLlImageChoice.setVisibility(View.GONE);
                    selectedTitleBt(0);
                } else {
                    mRlParent.bringChildToFront(mLlImageChoice);
                    mLlImageChoice.setVisibility(View.VISIBLE);
                }
                typeWithMap(mMapCameraMode);
                break;

            //타이틀바 (저장하기)
            case R.id.iv_photoView_activity_save:
                saveClick();
                break;

            //타이틀바 (공유하기)
            case R.id.iv_photoView_activity_share:
                shareClick();
                break;

            //타이틀바 (판서)
            case R.id.iv_photoView_activity_edit:
                if(getSelectedTitleButton() != 1) {
                    if(getSelectedTitleButton() == 0) {
                        if(mMapCameraMode == CAMERA_MODE_COMPASS_ON) {
                            mMapCameraMode = CAMERA_MODE_COMPASS_OFF;
                        }
                        typeWithMap(mMapCameraMode);
                    }else if(getSelectedTitleButton() == 4) {
                        if(getSelectedBOption() == 0){
                            if(mMapCameraMode == CAMERA_MODE_COMPASS_OFF){
                                mMapCameraMode = CAMERA_MODE_COMPASS_ON;
                            }
                        }
                        typeWithMap(mMapCameraMode);
                    }
                    setTypeWithView(Cvalue.MainOption4);
                }
                break;

            //촬영하기
            case R.id.iv_photoView_activity_camera:
                cameraClick();
                break;

            //갤러리 호출
            case R.id.iv_photoView_activity_gallery:
                galleryClick();
                break;

            //판서(펜)
            case R.id.iv_photoView_activity_pen:
                setTypeWithView(Cvalue.MiddleOption1);
                break;

            //판서(텍스트)
            case R.id.iv_photoView_activity_text:
                setTypeWithView(Cvalue.MiddleOption2);
                break;

            //판서(도형)
            case R.id.iv_photoView_activity_figure:
                setTypeWithView(Cvalue.MiddleOption3);
                break;

            //판서(초기화)
            case R.id.iv_photoView_activity_eraser:
//                stopTracking();
                Intent removeIntent = new Intent(act, PhotoMapRemoveAllDialog.class);
                startActivityForResult(removeIntent, REMOVE_EDIT);
                break;

            //판서(닫기)
            case R.id.iv_photoView_activity_close_edit:
                if(mMapCameraMode == CAMERA_MODE_COMPASS_OFF) {
                    mMapCameraMode = CAMERA_MODE_COMPASS_ON;
                }
                typeWithMap(mMapCameraMode);
                setTypeWithView(Cvalue.MiddleOption5);
                break;

            //판서 하단옵션 보이기/숨기기
            case R.id.rl_photoView_activity_editMenu_arrow:
                if(mLlBottomItem.getVisibility() == View.VISIBLE) {
                    mLlBottomItem.setVisibility(View.GONE);
                    mRlBottomArrow.setSelected(false);
                    if(getSelectedBOption() == 2){
                        hideKeyboard();
                        rlEditText.setVisibility(View.GONE);
                    }
                }else{
                    mLlBottomItem.setVisibility(View.VISIBLE);
                    addBottomMenu(mLlBottomItem, nSelectMode);
                    mRlBottomArrow.setSelected(true);
                    if(getSelectedBOption() == 2){
                        mEtEdit.requestFocus();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(mEtEdit, InputMethodManager.SHOW_IMPLICIT);
                        rlEditText.setVisibility(View.VISIBLE);
                    }
                }
                break;

            //Bottom Menu
            //하단메뉴 첫번째(색깔지정)
            case R.id.iv_bottom_menu_type1:
                switch (getSelectedBOption()) {
                    case 1:
                        if(!isEraserMode){
                            new ColorPickerDialog(act, this, mView.mPaint.getColor()).show();
                            setBeforeColor(mView.mPaint.getColor());
                            setBottomSelect(1);
                        }else{
                            Toast.makeText(act, "지우개 모드에선 색상 변경을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 2:
                        new ColorPickerDialog(act, this, nTextColor).show();
                        setBeforeColor(nTextColor);
                        Cvalue.nTouch = 0;
                        setBottomSelect(1);
                        break;

                    case 3:
                        new ColorPickerDialog(act, this, mFigureView.getCurrentColor()).show();
                        setBeforeColor(mFigureView.getCurrentColor());
                        Cvalue.nTouch = 3;
                        setBottomSelect(1);
                        break;
                }
                mFigureView.invalidate();
                break;

            //하단메뉴 두번째(굵기 조절)
            case R.id.iv_bottom_menu_type2:
                switch (getSelectedBOption()) {
                    case 1:
                        if(isEraserMode){
                            Toast.makeText(act, "지우개 모드에선 굵기 조절을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            setBottomSelect(4);
                        }else{
                            new LineWidthDlg(act, this, mView.getnWidth(), " 선 굵기 조절").show();
                            setBottomSelect(2);
                        }
                        break;

                    case 2:
                        new LineWidthDlg(act, this, nTextSize, "글자 크기 조절").show();
                        setBottomSelect(2);
                        break;

                    case 3:
                        new LineWidthDlg(act, this, mFigureView.getNWidth(), "도형 굵기 조절").show();
                        setBottomSelect(2);
                        Cvalue.nTouch = 3;
                        break;
                }
                mFigureView.invalidate();
                break;

            //하단메뉴 세번째
            case R.id.iv_bottom_menu_type3:
                switch (getSelectedBOption()) {
                    case 1:
//                        Log.i(TAG, "EraserMode ==> "+isEraserMode);
                        if(!isEraserMode) {
                            setBottomSelect(3);
                            new LineTypeChangeDlg(act, this, nLineType).show();
                        }else{
                            Toast.makeText(act, "지우개 모드에선 선종류를 선택 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 2:
                        setBottomSelect(3);
                        new TextStyleChangeDlg(act, this, nTextStyle).show();
                        Cvalue.nTouch = 0;
                        break;

                    case 3:
                        setBottomSelect(3);
                        if(mFigureView.getFigureCount() < 5){
                            new FigureChangeDlg(act, this, 0).show();
                        }else{
                            Toast.makeText(act, "도형은 최대 5개까지 가능합니다.", Toast.LENGTH_SHORT).show();
                            setBottomSelect(0);
                        }
                        Cvalue.nTouch = 3;
                        break;
                }
                mFigureView.invalidate();
                break;

            //하단메뉴 네번째
            case R.id.iv_bottom_menu_type4:
                if(getSelectedBOption() == 2) {
                    if(isText == true) {
                        if(getSelectedBottom() == 3) {
                            mEditText.isTouch = false;
                            Cvalue.nTouch = 0;
                            mArrBottomOptions.get(3).setSelected(false);
                        } else {
                            Cvalue.nTouch = 2;
                            setBottomSelect(4);
                            for(int i=0; i<mArrTextViews.size(); i++) {
                                mEditText = mArrTextViews.get(i);
                                mEditText.isTouch = true;
                            }
                        }
                    } else {
                        Toast.makeText(act, "글자 입력상태에선 위치를 이동할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else if(getSelectedBOption() == 3) {
                    if(getSelectedBottom() == 3) {
                        Cvalue.nTouch = 0;
                        mArrBottomOptions.get(3).setSelected(false);
                    } else {
                        Cvalue.nTouch = 5;
                        setBottomSelect(4);
                    }
                } else if(getSelectedBOption() == 1) {
                    if(isEraserMode) {
                        isEraserMode = false;
                        setBottomSelect(0);
                        selectedBOption(1);
                        mLlImageAlphaChangeParent.setVisibility(View.GONE);
                        mLlBottomMenu.setVisibility(View.VISIBLE);
                        mIvEdit.setBackgroundResource(R.drawable.menu_04_01);
                        mLlEditItem.setVisibility(View.GONE);
                        mRlBottomArrow.setVisibility(View.VISIBLE);
                        mRlBottomArrow.setSelected(true);
                        nSelectMode = 0;
                        rlEditText.setVisibility(View.GONE);
                        Cvalue.nTouch = 1;
                        hideKeyboard();
                        mLlBottomItem.setVisibility(View.VISIBLE);
                        mIvDelFigure.setVisibility(View.GONE);
                        if(nLineType == 0) {
                            mView.selectedMode(mView.CURVE);
                        } else if(nLineType == 1) {
                            mView.selectedMode(mView.DOTTED_LINE);
                        } else if(nLineType == 2) {
                            mView.selectedMode(mView.STRAIGHT);
                        } else if(nLineType == 3) {
                            mView.selectedMode(mView.ARROW);
                        }
                    } else {
                        isEraserMode = true;
                        setBottomSelect(4);
                        mLlImageAlphaChangeParent.setVisibility(View.GONE);
                        mIvEdit.setBackgroundResource(R.drawable.menu_04_04);
                        mView.selectedMode(mView.ERASER);
                        rlEditText.setVisibility(View.GONE);
                        nSelectMode = 0;
                        Cvalue.nTouch = 1;
                        mIvDelFigure.setVisibility(View.GONE);
                        hideKeyboard();
                    }
                }
                mFigureView.invalidate();
                break;

            // 카메라 & 갤러리 창 취소버튼
            case R.id.tv_photoView_activity_choice_cancel:
                if(!isMap && !isImage) {
                    mLlSelectType.setVisibility(View.VISIBLE);
                    mLlImageChoice.setVisibility(View.GONE);
                    selectedTitleBt(0);
                } else {
                    mLlSelectType.setVisibility(View.GONE);
                    mLlImageChoice.setVisibility(View.GONE);
                    selectedTitleBt(0);
                    if(mMapCameraMode == CAMERA_MODE_COMPASS_OFF) {
                        mMapCameraMode = CAMERA_MODE_COMPASS_ON;
                    }
                    typeWithMap(mMapCameraMode);
                }
                break;

            // 판서 하단 도형 / 텍스트 지우기
            case R.id.iv_delete_figure:
                if(nSelectMode == 1){
                    if(mArrTextViews.size() > 0){
                        int size = mArrTextViews.size()-1;
                        mArrTextViews.remove(size);
                        mFlEditText.removeViewAt(size);
                    }else{
                        Toast.makeText(act, "지울 글자가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }else if(nSelectMode == 2){
                    mFigureView.deleteFigure();
                }
            break;

            //이미지 투명도 조절
            case R.id.rl_photoView_activity_image_alpha_change:
                if(mRlImageAlphaChangeArrow.isSelected() == true) {
                    mRlImageAlphaChangeArrow.setSelected(false);
                    mLlImageAlphaChange.setVisibility(View.GONE);
                } else {
                    mRlImageAlphaChangeArrow.setSelected(true);
                    mLlImageAlphaChange.setVisibility(View.VISIBLE);
                }
            break;

            //지도 옵션(지도 보이기 / 숨기기)
            case R.id.iv_photoView_activity_map:
                if(getSelectedTitleButton() != 1){
                    if(isMapShow){
                        if(isImage){
                            isMapShow = false;
                            mMapFragment.getView().setVisibility(View.GONE);
                            mIvMapCameraControl.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(act, "지도를 끄기 위해선 이미지가 필요 합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        isMapShow = true;
                        typeWithMap(mMapCameraMode);
                        mMapFragment.getView().setVisibility(View.VISIBLE);
                        mIvMapCameraControl.setVisibility(View.VISIBLE);
                    }
                }
            break;

            //지도 모드 변경버튼
            case R.id.iv_photoView_activity_map_control:
                if(mMapCameraMode == CAMERA_MODE_ANOTHER_POSITION) {
                    mMapCameraMode = CAMERA_MODE_CURRENT_POSITION;
                } else if(mMapCameraMode == CAMERA_MODE_CURRENT_POSITION) {
                    mMapCameraMode = CAMERA_MODE_COMPASS_ON;
                } else if(mMapCameraMode == CAMERA_MODE_COMPASS_ON) {
                    mMapCameraMode = CAMERA_MODE_COMPASS_OFF_FROM_USER;
                } else if(mMapCameraMode == CAMERA_MODE_COMPASS_OFF || mMapCameraMode == CAMERA_MODE_COMPASS_OFF_FROM_USER) {
                    mMapCameraMode = CAMERA_MODE_COMPASS_ON;
                }
                typeWithMap(mMapCameraMode);
            break;
        }
    }

    //지도 모드 변경에 따른 아이콘 변경 및 동작메서드
    private void typeWithMap(int mapMode) {
        switch (mapMode) {
            case CAMERA_MODE_ANOTHER_POSITION:
                mIvMapCameraControl.setBackgroundResource(R.drawable.map_current_position_off);
                break;

            case CAMERA_MODE_CURRENT_POSITION:
                mIvMapCameraControl.setBackgroundResource(R.drawable.map_current_position_on);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLatLng, mZoomLevel), 2000, null);
                break;

            case CAMERA_MODE_COMPASS_ON:
                mIvMapCameraControl.setBackgroundResource(R.drawable.map_compass_on);
                break;

            case CAMERA_MODE_COMPASS_OFF:
                mIvMapCameraControl.setBackgroundResource(R.drawable.map_compass_off);
                break;

            case CAMERA_MODE_COMPASS_OFF_FROM_USER:
                mIvMapCameraControl.setBackgroundResource(R.drawable.map_compass_off);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File fromFile = null;
        File toFile = null;
        switch (requestCode) {
            case CAMERA:
                if(resultCode == RESULT_CANCELED) {
                    mLlImageChoice.setVisibility(View.VISIBLE);
                    return;
                }
                String imgPath = Cutil.getImageRealPathFromURI(act.getContentResolver(), Uri.fromFile(mFile));
                setImage(mIvSelectImg, imgPath);
                act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mFile)));
                break;

            case GALLERY:
                if(resultCode == RESULT_CANCELED){
                    selectedTitleBt(0);
                    return;
                }
                onSelectFromGalleryResult(data);
                break;

            case GALLERY_SHARE:
                if(resultCode == RESULT_CANCELED){
                    selectedTitleBt(0);
                    return;
                }
                Cutil.resetImageFileIfIsExist(mCropedImageUri);
                mCropedImageUri = data.getData();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, mCropedImageUri);
                startActivity(Intent.createChooser(shareIntent, "Choose"));
                selectedTitleBt(0);
                break;

            case EXIT:
                if(resultCode == RESULT_OK){
                    finish();
                }else{
                    typeWithMap(mMapCameraMode);
                }
                break;

            case REQUEST_CODE_GPS:
                if(mLocationManager == null) {
                    mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
                }
                if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    setGPS = true;
                    mMapFragment.getMapAsync(this);
                }
                break;

            case SHARE:
                if(mMapCameraMode == CAMERA_MODE_COMPASS_OFF) {
                    mMapCameraMode = CAMERA_MODE_COMPASS_ON;
                }
                typeWithMap(mMapCameraMode);
                if(resultCode == RESULT_OK && data != null) {
                    if(data.getExtras().get("share").equals(0)) {
                        captureScreen(mRlParent, 2);
                    } else if(data.getExtras().get("share").equals(1)) {
                        Intent picIntent = new Intent(Intent.ACTION_PICK);
                        picIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(picIntent, GALLERY_SHARE);
                    }
                } else {
                    selectedTitleBt(0);
                }
                break;

            case REMOVE_EDIT:
                if(resultCode == RESULT_OK){
                    mView.selectedMode(mView.CURVE);
                    nLineType = 0;
                    nBeforeLineColor = -16777216;
                    nTextColor = -16777216;
                    nBeforeFigureColor = -16777216;
                    mView.changedWidth(5);
                    mView.colorChanged(nBeforeLineColor);
                    setBeforeColor(nBeforeLineColor);
                    mView.resetView();
                    System.gc();
                    if(mArrTextViews.size() > 0) {
                        nTextSize = 12;
                        nTextStyle = 0;
                        for(int i=mArrTextViews.size()-1; i>=0; i--) {
                            mArrTextViews.remove(i);
                            mFlEditText.removeViewAt(i);
                        }
                    }

                    if(mFigureView.getFigureCount() > 0) {
                        mFigureView.colorChanged(nBeforeFigureColor);
                        mFigureView.changedWidth(10);
                        for(int i=mFigureView.getFigureCount()-1; i>=0; i--) {
                            mFigureView.deleteFigure();
                        }
                    }
                }
                break;

            case REQUEST_CODE_STORAGE_CAMERA:
                setPermissionResult(REQUEST_CODE_STORAGE_CAMERA);
                break;

            case REQUEST_CODE_STORAGE_GALLERY:
                setPermissionResult(REQUEST_CODE_STORAGE_GALLERY);
                break;

            case REQUEST_CODE_STORAGE_SAVE:
                setPermissionResult(REQUEST_CODE_STORAGE_SAVE);
                break;

            case REQUEST_CODE_STORAGE_SHARE:
                setPermissionResult(REQUEST_CODE_STORAGE_SHARE);
                break;
        }
    }

    private void setPermissionResult(final int nResultCode){
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mUserData.setStoragePermission(true);
        } else {
            mUserData.setStoragePermission(false);
        }
        switch (nResultCode){
            case REQUEST_CODE_STORAGE_CAMERA:
                cameraClick();
                break;

            case REQUEST_CODE_STORAGE_GALLERY:
                galleryClick();
                break;

            case REQUEST_CODE_STORAGE_SAVE:
                saveClick();
                break;

            case REQUEST_CODE_STORAGE_SHARE:
                shareClick();
                break;
        }
    }

    //카메라, 갤러리에서 불러온 이미지를 이미지뷰에 보여주는 메서드
    private void setImage(ImageView ivPic, String path) {
        try {
            Cutil.recycleBitmap(ivPic);
            ivPic.setImageBitmap(null);
            int orientation = Cutil.getExIfOrientation(path);
            BitmapFactory.Options bmOption = new BitmapFactory.Options();
            bmOption.inSampleSize = 4;
            Bitmap src = BitmapFactory.decodeFile(path, bmOption);
            src = Cutil.resizeBitmapImage(src, Cvalue.HeightPixel);
            src = Cutil.rotateBitmap(src, orientation);
            ivPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivPic.setImageBitmap(src);
            ivPic.setTag(path);
            ivPic.setAlpha((float)0.4);
            mSbImageAlphaChange.setProgress(40);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(act, "파일을 찾을수 없습니다.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mLlImageChoice.setVisibility(View.GONE);
        mIvBlack.setVisibility(View.GONE);
        mIvChoice.setSelected(false);
        isImage = true;
        mLlImageAlphaChangeParent.setVisibility(View.VISIBLE);
        mRlImageAlphaChangeArrow.setSelected(true);
        mLlImageAlphaChange.setVisibility(View.VISIBLE);
        mLlSelectType.setVisibility(View.GONE);
    }

    //그리기 속성중 라인색 변경
    @Override
    public void colorChanged(int color) {
        if(getSelectedBOption() == 1) {
            nBeforeLineColor = color;
            mView.colorChanged(nBeforeLineColor);
            setBeforeColor(nBeforeLineColor);
        } else if(getSelectedBOption() == 2) {
            nTextColor = color;
            nBeforeTextColor = color;
            setBeforeColor(nBeforeTextColor);
            for(int i=0; i< mArrTextViews.size(); i++){
                mArrTextViews.get(i).changeTextColor(nBeforeTextColor);
            }
        } else if(getSelectedBOption() == 3) {
            nBeforeFigureColor = color;
            mFigureView.colorChanged(nBeforeFigureColor);
            setBeforeColor(nBeforeFigureColor);
        }
        setBottomSelect(0);
    }

    private void setBeforeColor(int color) {
        try{
            Drawable background = mIvCurrentColor.getBackground();
            if(background instanceof ShapeDrawable) {
                ShapeDrawable shape = (ShapeDrawable)background;
                shape.getPaint().setColor(color);
            } else if(background instanceof GradientDrawable) {
                GradientDrawable gradient = (GradientDrawable)background;
                gradient.setColor(color);
            } else if(background instanceof ColorDrawable) {
                ColorDrawable colorDraw = (ColorDrawable)background;
                colorDraw.setColor(color);
            }
            mIvCurrentColor.setVisibility(View.VISIBLE);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * 화면 캡쳐, 공유기능 메서드
     * @param view - Parent View
     * @param type type == 1 --> Capture / type = 2 --> Share
     * @throws Exception
     */
    private void captureScreen(final View view, final int type) {
        final MyProgress myProgress = new MyProgress(this);
        if(!new File(Cvalue.BASE_FOLDER_PATH).exists()) {
            new File(Cvalue.BASE_FOLDER_PATH).mkdir();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'_'HH.mm.ss", Locale.KOREA);
        Date date = new Date();
        final String fileName = "nb_map_"+simpleDateFormat.format(date)+".jpg";
        view.setDrawingCacheEnabled(true);
       if(isMapShow) {
           //맵이 보여지는 상태 일때 구글맵의 snapshot 메서드를 호출하여 콜백으로 비트맵 이미지를 받은다음 처리한다
           mGoogleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
               @Override
               public void onSnapshotReady(Bitmap bitmap) {
                   myProgress.show();
                   mIvMapCameraControl.setVisibility(View.GONE);
                   mIvMapControl.setVisibility(View.GONE);
                   mLlImageAlphaChangeParent.setVisibility(View.GONE);
                   Bitmap screenshot = Cutil.addWaterMark(act, bitmap, view.getDrawingCache());
                   try{
                       File mFile = new File(Cvalue.BASE_FOLDER_PATH, fileName);
                       mFile.createNewFile();
                       OutputStream outputStream = new FileOutputStream(mFile);
                       screenshot.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                       outputStream.close();
                       screenshot.recycle();
                       if(type == 1) {
                           //현재화면 캡쳐
                           act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mFile)));
                           Toast.makeText(act, "스크린샷 완료", Toast.LENGTH_SHORT).show();
                       } else if(type == 2) {
                           //현재 화면 공유하기
                           Intent shareIntent = new Intent(Intent.ACTION_SEND);
                           shareIntent.setType("image/jpg");
                           shareIntent.putExtra(Intent.EXTRA_TEXT, "공유하기 내용입니다.");
                           shareIntent.putExtra(Intent.EXTRA_TITLE, "공유 제목입니다.");
                           shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mFile));
                           startActivity(Intent.createChooser(shareIntent, "공유하기"));
                           mFile.deleteOnExit();
                       }
                       view.setDrawingCacheEnabled(false);
                   }catch (FileNotFoundException e){
                       Toast.makeText(act, "파일을 찾을수 없습니다", Toast.LENGTH_SHORT).show();
                   }catch (IOException e) {
                       e.printStackTrace();
                   }
                   typeWithMap(mMapCameraMode);
                   setImageAlphaView(isImage);
                   mIvMapCameraControl.setVisibility(View.VISIBLE);
                   mIvMapControl.setVisibility(View.VISIBLE);
                   myProgress.dismiss();
               }
           });
       } else {
           //구글맵이 보여지는 상태가 아닐때
           myProgress.show();
           mLlImageAlphaChangeParent.setVisibility(View.GONE);
           if(isImage){
               //이미지가 있을때
               try{
                   mIvMapControl.setVisibility(View.GONE);
                   Bitmap screenshot = view.getDrawingCache();
                   screenshot = Cutil.addWaterMark(act, screenshot, null);
                   File mFile = new File(Cvalue.BASE_FOLDER_PATH, fileName);
                   mFile.createNewFile();
                   OutputStream outputStream = new FileOutputStream(mFile);
                   screenshot.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                   outputStream.close();
                   screenshot.recycle();
                   mIvMapControl.setVisibility(View.VISIBLE);
                   myProgress.dismiss();
                   if(type == 1){
                       act.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mFile)));
                       Toast.makeText(act, "스크린샷 완료", Toast.LENGTH_SHORT).show();
                   }else if(type == 2){
                       Intent shareIntent = new Intent(Intent.ACTION_SEND);
                       shareIntent.setType("image/jpg");
                       shareIntent.putExtra(Intent.EXTRA_TEXT, "공유하기 내용입니다.");
                       shareIntent.putExtra(Intent.EXTRA_TITLE, "공유 제목입니다.");
                       shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mFile));
                       startActivity(Intent.createChooser(shareIntent, "공유하기"));
                       mFile.deleteOnExit();
                   }
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
                   Toast.makeText(act, "파일을 찾을수 없습니다.", Toast.LENGTH_SHORT).show();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }else{
               //이미지가 없을때
               if (type == 1) {
                   Toast.makeText(act, "빈 화면은 저장 할 수 없습니다.", Toast.LENGTH_SHORT).show();
               } else {
                   Toast.makeText(act, "빈 화면은 공유 할 수 없습니다.", Toast.LENGTH_SHORT).show();
               }
           }
           setImageAlphaView(isImage);
           myProgress.dismiss();
       }
        selectedTitleBt(0);
    }

    //판서기능 옵션에 따른 하단 메뉴 설정
   private void addBottomMenu(LinearLayout ll, int type) {
        ll.removeAllViews();
        View vItem = View.inflate(act, R.layout.bottom_menu, null);
        LinearLayout.LayoutParams lParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lParam.gravity = Gravity.CENTER;
        vItem.setLayoutParams(lParam);
        LinearLayout ivType1 = (LinearLayout) vItem.findViewById(R.id.iv_bottom_menu_type1);
        ImageView ivType1Text = (ImageView)vItem.findViewById(R.id.iv_bottom_menu_current_color_text);
        ImageView ivType2 = (ImageView)vItem.findViewById(R.id.iv_bottom_menu_type2);
        ImageView ivType3 = (ImageView)vItem.findViewById(R.id.iv_bottom_menu_type3);
        ImageView ivType4 = (ImageView)vItem.findViewById(R.id.iv_bottom_menu_type4);
        mIvCurrentColor = (ImageView)vItem.findViewById(R.id.iv_bottom_menu_current_color);
//        ivCurrentColor.setBackgroundColor(Color.BLACK);
        switch (type) {
            case 0:
                //펜
                ivType1Text.setBackgroundResource(R.drawable.selector_bottom_color_text);
                ivType2.setBackgroundResource(R.drawable.selector_bottom_width_1);
                ivType3.setBackgroundResource(R.drawable.selector_bottom_line_type);
                ivType4.setBackgroundResource(R.drawable.selector_bottom_eraser);
                break;

            case 1:
                //텍스트
                ivType1Text.setBackgroundResource(R.drawable.selector_bottom_color_text2);
                ivType2.setBackgroundResource(R.drawable.selector_bottom_width_2);
                ivType3.setBackgroundResource(R.drawable.selector_bottom_style);
                ivType4.setBackgroundResource(R.drawable.selector_bottom_moving_2);
                break;

            case 2:
                //도형
                ivType1Text.setBackgroundResource(R.drawable.selector_bottom_color_text2);
                ivType2.setBackgroundResource(R.drawable.selector_bottom_width_3);
                ivType3.setBackgroundResource(R.drawable.selector_bottom_figure);
                ivType4.setBackgroundResource(R.drawable.selector_bottom_moving_3);
                break;

            case 3:
                //초기화
                ivType1.setVisibility(View.GONE);
                ivType2.setBackgroundResource(R.drawable.selector_bottom_width_1);
                ivType3.setVisibility(View.GONE);
                ivType4.setVisibility(View.GONE);
                break;
        }
        ivType1.setOnClickListener(this);
        ivType2.setOnClickListener(this);
        ivType3.setOnClickListener(this);
        ivType4.setOnClickListener(this);
        mArrBottomOptions = new ArrayList<View>();
        mArrBottomOptions.add(ivType1);
        mArrBottomOptions.add(ivType2);
        mArrBottomOptions.add(ivType3);
        mArrBottomOptions.add(ivType4);
        ll.addView(vItem);
    }

    //굵기 조절 콜백
    @Override
    public void widthChanged(int width) {
        if(width > 0){
            if(getSelectedBOption() == 1) {
                mView.changedWidth(width);
                setBottomSelect(0);
                if(isEraserMode) {
                    setBottomSelect(4);
                }
            } else if(getSelectedBOption() == 2) {
                nTextSize = width;
                for(int i=0; i<mArrTextViews.size(); i++){
                    mArrTextViews.get(i).changeTextSize(width);
                }
                setBottomSelect(0);
            } else if(getSelectedBOption() == 3) {
                mFigureView.changedWidth(width);
                setBottomSelect(0);
            } else if(getSelectedBOption() == 4) {
                mView.changedWidth(width);
                setBottomSelect(0);
            }
        } else {
            if(getSelectedBOption() == 1 && isEraserMode) {
                setBottomSelect(4);
            } else {
                setBottomSelect(0);
            }
        }
    }

    //펜 기능 선 종류 콜백
    @Override
    public void onLineTypeChanged(int type) {
        if(type == 0) {
            mView.selectedMode(mView.CURVE);
        } else if(type == 1) {
            mView.selectedMode(mView.DOTTED_LINE);
        } else if(type == 2) {
            mView.selectedMode(mView.STRAIGHT);
        } else if(type == 3) {
            mView.selectedMode(mView.ARROW);
        }
        nLineType = type;
        setBottomSelect(0);
    }

    //판서기능 이미지 on/off 설정 메서드 초기화 = 0
    public void selectedBOption(int pos) {
        for(int i=0; i<mArrBigOptions.size(); i++){
            mArrBigOptions.get(i).setSelected(false);
        }
        if(pos != 0){
            mArrBigOptions.get(pos-1).setSelected(true);
        }
    }

    //타이틀바 메인 옵션 이미지 on/off 설정 메서드 초기화 = 0
    public void selectedTitleBt(int pos) {
        for(int i=0; i<mArrTitleBts.size(); i++) {
            mArrTitleBts.get(i).setSelected(false);
        }
        if(pos > 0) {
            mArrTitleBts.get(pos-1).setSelected(true);
        }
        if(pos != 4) {
            mLlEditItem.setVisibility(View.GONE);
            mLlBottomMenu.setVisibility(View.GONE);
        }
    }

    //현재 선택되어 있는 메인옵션을 반환한다 default = 0
    public int getSelectedTitleButton() {
        int pos = 0;
        for(int i=0; i<mArrTitleBts.size(); i++) {
            if(mArrTitleBts.get(i).isSelected() == true) {
                pos = i+1;
            }
        }
        return pos;
    }

    //현재 선택 되어 있는 판서 옵션을 반환한다 default = 0
    public int getSelectedBOption() {
        int pos = 0;
        for(int i=0; i<mArrBigOptions.size(); i++) {
            if(mArrBigOptions.get(i).isSelected() == true) {
                pos = i+1;
            }
        }
        return pos;
    }

    //현재 선택되어 있는 하단메뉴를 반환한다.
    private int getSelectedBottom() {
        int pos = 0;
        for(int i=0; i< mArrBottomOptions.size(); i++) {
            if(mArrBottomOptions.get(i).isSelected() == true) {
                pos = i;
            }
        }
        return  pos;
    }

    //도형 선택 콜백
    @Override
    public void onFigureChanged(int type) {
        if(type == 1){
            mFigureView.selectedMode(mFigureView.RECT);
            Toast.makeText(act, "도형이 그려질 영역을 터치해 주세요.", Toast.LENGTH_SHORT).show();
            Cvalue.nTouch = 5;
            setBottomSelect(4);
        }else if(type == 2){
            mFigureView.selectedMode(mFigureView.CIRCLE);
            Toast.makeText(act, "도형이 그려질 영역을 터치해 주세요.", Toast.LENGTH_SHORT).show();
            Cvalue.nTouch = 5;
            setBottomSelect(4);
        }else if(type == 3){
            mFigureView.selectedMode(mFigureView.TRIANGLE);
            Toast.makeText(act, "도형이 그려질 영역을 터치해 주세요.", Toast.LENGTH_SHORT).show();
            Cvalue.nTouch = 5;
            setBottomSelect(4);
        }else{
            Cvalue.nTouch = 0;
            setBottomSelect(0);
        }
    }

    //텍스트 스타일 변경 콜백
    @Override
    public void onStyleChanged(int type) {
        nTextStyle = type;
        for(int i=0; i<mArrTextViews.size(); i++) {
            if(type == 0) {
                mArrTextViews.get(i).onStyleChange(Typeface.NORMAL);
            } else if(type == 1) {
                mArrTextViews.get(i).onStyleChange(Typeface.BOLD);
            } else if(type == 2) {
                mArrTextViews.get(i).onStyleChange(Typeface.ITALIC);
            }
        }
        setBottomSelect(0);
    }

    //화면저장 콜백
    @Override
    public void onDlgClick(int type) {
        if(mMapCameraMode == CAMERA_MODE_COMPASS_OFF) {
            mMapCameraMode = CAMERA_MODE_COMPASS_ON;
        }
        typeWithMap(mMapCameraMode);
        if(type == 1){
            mLlImageAlphaChange.setVisibility(View.GONE);
            captureScreen(mRlParent, 1);
        }else {
            mIvSave.setSelected(false);
        }
        selectedTitleBt(0);
    }

    //하단 옵션 이미지 선택된 값 설정
    private void setBottomSelect(int pos) {
        for(int i=0; i<mArrBottomOptions.size(); i++) {
            mArrBottomOptions.get(i).setSelected(false);
        }
        if(pos != 0) {
            mArrBottomOptions.get(pos-1).setSelected(true);
        }
    }

    /**
     * 옵션 클릭에 따른 뷰 변화 메서드(상위4개 버튼, 판서 옵션 4개 버튼)
     */
    private void setTypeWithView(int type) {
        switch (type) {
            //이미지 불러오기 버튼
            case Cvalue.MainOption1:
                isEraserMode = false;
                mIvEdit.setBackgroundResource(R.drawable.ic_draw);
                selectedTitleBt(1);
                Cvalue.nTouch = 0;
                hideKeyboard();
                selectedBOption(0);
                setImageAlphaView(isImage);
                break;

            //화면 저장 버튼
            case Cvalue.MainOption2:
                isEraserMode = false;
                mIvEdit.setBackgroundResource(R.drawable.ic_draw);
                selectedTitleBt(2);
                Cvalue.nTouch = 0;
                hideKeyboard();
                selectedBOption(0);
                setImageAlphaView(isImage);
                mLlImageChoice.setVisibility(View.GONE);
                break;

            //화면 공유버튼
            case Cvalue.MainOption3:
                isEraserMode = false;
                mIvEdit.setBackgroundResource(R.drawable.ic_draw);
                selectedTitleBt(3);
                Cvalue.nTouch = 0;
                hideKeyboard();
                selectedBOption(0);
                setImageAlphaView(isImage);
                mLlImageChoice.setVisibility(View.GONE);
                break;

            //판서버튼
            case Cvalue.MainOption4:
                selectedTitleBt(4);
                hideKeyboard();
                mLlImageChoice.setVisibility(View.GONE);
                mLlImageAlphaChangeParent.setVisibility(View.GONE);
                if(mLlEditItem.getVisibility() == View.VISIBLE) {
                    mLlEditItem.setVisibility(View.GONE);
                    mIvEdit.setSelected(false);
                    mLlBottomItem.setVisibility(View.GONE);
                    mRlBottomArrow.setSelected(false);
                } else {
                    mLlEditItem.setVisibility(View.VISIBLE);
                    mIvEdit.setSelected(true);
                }
                if(getSelectedBOption() != 0) {
                    mLlBottomMenu.setVisibility(View.VISIBLE);
                    selectedBOption(getSelectedBOption());
                    mView.selectedMode(mView.getSelectedMode());
                } else {
                    mLlBottomMenu.setVisibility(View.GONE);
                    mLlBottomItem.setVisibility(View.GONE);
                    setImageAlphaView(isImage);
                }
                mLlImageAlphaChange.setVisibility(View.GONE);
                mView.setVisibility(View.VISIBLE);
                break;

            //판서(펜)
            case Cvalue.MiddleOption1:
                selectedBOption(1);
                mLlImageAlphaChangeParent.setVisibility(View.GONE);
                mLlBottomMenu.setVisibility(View.VISIBLE);
                mIvEdit.setBackgroundResource(R.drawable.menu_04_01);
                mLlEditItem.setVisibility(View.GONE);
                mRlBottomArrow.setVisibility(View.VISIBLE);
                mRlBottomArrow.setSelected(true);
                nSelectMode = 0;
                rlEditText.setVisibility(View.GONE);
                addBottomMenu(mLlBottomItem, nSelectMode);
                Cvalue.nTouch = 1;
                hideKeyboard();
                mLlBottomItem.setVisibility(View.VISIBLE);
                mIvDelFigure.setVisibility(View.GONE);
                if(nLineType == 0) {
                    mView.selectedMode(mView.CURVE);
                } else if(nLineType == 1) {
                    mView.selectedMode(mView.DOTTED_LINE);
                } else if(nLineType == 2) {
                    mView.selectedMode(mView.STRAIGHT);
                } else if(nLineType == 3) {
                    mView.selectedMode(mView.ARROW);
                }
                break;

            //판서(텍스트)
            case Cvalue.MiddleOption2:
                selectedBOption(2);
                selectedTitleBt(4);
                isEraserMode = false;
                mLlImageAlphaChangeParent.setVisibility(View.GONE);
                mLlBottomMenu.setVisibility(View.VISIBLE);
                mIvEdit.setBackgroundResource(R.drawable.menu_04_02);
                mRlBottomArrow.setVisibility(View.VISIBLE);
                mLlBottomItem.setVisibility(View.VISIBLE);
                mRlBottomArrow.setSelected(true);
                Cvalue.nTouch = 2;
                if(mArrTextViews.size() < 5){
                    addTextView();
                } else {
                    Toast.makeText(act, "최대 5개까지만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                nSelectMode = 1;
                addBottomMenu(mLlBottomItem, nSelectMode);
                mIvDelFigure.setVisibility(View.VISIBLE);
//                Log.i(TAG, "Text");
                break;

            //판서(도형)
            case Cvalue.MiddleOption3:
                selectedBOption(3);
                isEraserMode = false;
                mLlImageAlphaChangeParent.setVisibility(View.GONE);
                mLlBottomMenu.setVisibility(View.VISIBLE);
                mIvEdit.setBackgroundResource(R.drawable.menu_04_03);
                Cvalue.nTouch = 3;
                mLlBottomItem.setVisibility(View.VISIBLE);
                mRlBottomArrow.setVisibility(View.VISIBLE);
                mRlBottomArrow.setSelected(true);
                mLlEditItem.setVisibility(View.GONE);
                mFigureView.setVisibility(View.VISIBLE);
                rlEditText.setVisibility(View.GONE);
                nSelectMode = 2;
                addBottomMenu(mLlBottomItem, nSelectMode);
                mIvDelFigure.setVisibility(View.VISIBLE);
                hideKeyboard();
                if(mFigureView.getFigureCount() < 5) {
                    new FigureChangeDlg(act, this, 0).show();
                } else {
                    Toast.makeText(act, "도형은 최대 5개 까지 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            //판서(초기화)
            case Cvalue.MiddleOption4:
                selectedBOption(4);
                isEraserMode = false;
                mLlImageAlphaChangeParent.setVisibility(View.GONE);
                mIvEdit.setBackgroundResource(R.drawable.menu_04_04);
                mView.selectedMode(mView.ERASER);
                rlEditText.setVisibility(View.GONE);
                nSelectMode = 3;
                Cvalue.nTouch = 1;
                mIvDelFigure.setVisibility(View.GONE);
                hideKeyboard();
                addBottomMenu(mLlBottomItem, nSelectMode);
                break;

            //판서(닫기)
            case Cvalue.MiddleOption5:
                isEraserMode = false;
                mLlEditItem.setVisibility(View.GONE);
                mLlBottomMenu.setVisibility(View.GONE);
                mIvEdit.setBackgroundResource(R.drawable.ic_draw);
                mIvEdit.setSelected(false);
                mView.setSelect(false);
                Cvalue.nTouch = 0;
                selectedBOption(0);
                setImageAlphaView(isImage);
                break;
        }
        mFigureView.invalidate();
    }

    public void onBackPressed() {
        if(!isMap && !isImage){
            finish();
        }else {
            if(mLlImageChoice.getVisibility() == View.VISIBLE){
                mLlImageChoice.setVisibility(View.GONE);
                selectedTitleBt(0);
                return;
            }
            selectedTitleBt(0);
            Intent fIntent = new Intent(act, PhotoMapExitDlg.class);
            startActivityForResult(fIntent, EXIT);
        }
    }

    //키보드 내리기
    private void hideKeyboard() {
        View view = act.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //이미지 투명도 조절창 설정(보이기/숨기기)
    public void setImageAlphaView(boolean type){
        if(type == true){
            mLlImageAlphaChangeParent.setVisibility(View.VISIBLE);
            mLlImageAlphaChange.setVisibility(View.GONE);
            mRlImageAlphaChangeArrow.setSelected(false);
        }else{
            mLlImageAlphaChangeParent.setVisibility(View.GONE);
        }
    }

    //방향 센서와 구글Api를 해제 한다
    private void stopTracking() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
            mSensorManager.unregisterListener(this);
    }

    //방향 센서와 구글Api를 연결 한다
    private void startTracking(boolean isMapShow) {
        if(isMapShow){
            if(mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
//            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    //갤러리에서 선택된 이미지 가져오기 (Google Photo / Local)
    private void onSelectFromGalleryResult(Intent data) {
        Cutil.resetImageFileIfIsExist(mCropedImageUri);
        mCropedImageUri = data.getData();
        try{
            InputStream imInputStream = getContentResolver().openInputStream(mCropedImageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imInputStream);
            String path = saveGalaryImageOnLitkat(bitmap);
            setImage(mIvSelectImg, path);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(act, "파일을 찾을수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

//    public void setnSelectMode(int nSelectMode) {
//        this.nSelectMode = nSelectMode;
//    }

    //비트맵을 파일로 저장하고 주소를 반환 하는 메서드
    private String saveGalaryImageOnLitkat(Bitmap bitmap) {
        try {
            File cacheDir;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheDir = new File(Environment.getExternalStorageDirectory(), "BokBang");
            }else {
                cacheDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            }
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'_'HH.mm.ss", Locale.KOREA);
            Date date = new Date();
            String fileName = "nb_img_"+simpleDateFormat.format(date)+".jpg";
            File file = new File(Cvalue.BASE_FOLDER_PATH+"/"+fileName);
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            file.deleteOnExit();
            return file.getAbsolutePath();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //텍스트 버튼 입력 메서드
    private void addTextView() {
        isText = false;
        rlEditText.removeAllViews();
        rlEditText.setVisibility(View.VISIBLE);
        mEtEdit = new MyEditText(mContext);
        rlEditText.addView(mEtEdit);
        mEtEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEtEdit, InputMethodManager.SHOW_IMPLICIT);
        mIvEditText = new ImageView(mContext);
        final FrameLayout.LayoutParams lParam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lParam.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        lParam.setMargins(0, 0, (int)Cutil.convertToPx(act, 10), 0);
        mIvEditText.setLayoutParams(lParam);
        mIvEditText.setBackgroundResource(R.drawable.bt_input);
        rlEditText.addView(mIvEditText);

        mIvEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEtEdit.getText().toString().trim().length() > 0) {
                    hideKeyboard();
                    FrameLayout.LayoutParams fParam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    fParam.gravity = Gravity.CENTER;
                    isText = true;
                    mView.setSelect(false);
                    mEditText = new MyTextView(mContext);
                    mEditText.setLayoutParams(fParam);
                    mEditText.setText(mEtEdit.getText().toString());
                    mEditText.changeTextColor(nTextColor);
                    mEditText.changeTextSize(nTextSize);
                    mEditText.onStyleChange(nTextStyle);
                    mEtEdit.setText("");
                    mFlEditText.addView(mEditText);
                    mArrTextViews.add(mEditText);
                    Cvalue.nTouch = 2;
                    setBottomSelect(4);
                    for(int i=0; i<mArrTextViews.size(); i++){
                        mEditText = mArrTextViews.get(i);
                        mEditText.isTouch = true;
                    }
                }else{
                    isText = false;
                    Toast.makeText(mContext, "글자를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void makeBaseFolder() {
        if(!new File(Cvalue.BASE_FOLDER_PATH).exists()) {
            new File(Cvalue.BASE_FOLDER_PATH).mkdir();
        }
    }

    private void cameraClick(){
        if(mUserData.getStoragePermission()) {
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'_'HH.mm.ss", Locale.KOREA);
            Date date = new Date();
            String fileName = "nb_img_" + simpleDateFormat.format(date) + ".jpg";
            mFile = new File(Cvalue.BASE_FOLDER_PATH, fileName);
            camIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
            startActivityForResult(camIntent, CAMERA);
        } else {
            requestStoragePermission(REQUEST_CODE_STORAGE_CAMERA);
        }
    }

    private void galleryClick(){
        if(mUserData.getStoragePermission()) {
            Intent picIntent = new Intent(Intent.ACTION_PICK);
            picIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(Intent.createChooser(picIntent, "사진 선택"), GALLERY);
        } else {
            requestStoragePermission(REQUEST_CODE_STORAGE_GALLERY);
        }
    }

    private void saveClick(){
        if(mUserData.getStoragePermission()) {
            if (getSelectedTitleButton() != 1) {
                setTypeWithView(Cvalue.MainOption2);
                new ScreenCaptureDlg(act, this).show();
            }
        } else {
            requestStoragePermission(REQUEST_CODE_STORAGE_SAVE);
        }
    }

    private void shareClick() {
        if(mUserData.getStoragePermission()) {
            if (getSelectedTitleButton() != 1) {
                setTypeWithView(Cvalue.MainOption3);
                typeWithMap(mMapCameraMode);
                Intent shareIntent = new Intent(act, ShareViewChoiceDialog.class);
                act.startActivityForResult(shareIntent, SHARE);
            }
        } else {
            requestStoragePermission(REQUEST_CODE_STORAGE_SHARE);
        }
    }
}
