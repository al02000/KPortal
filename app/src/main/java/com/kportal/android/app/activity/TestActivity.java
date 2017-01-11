package com.kportal.android.app.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kportal.android.app.common.Cutil;
import com.kportal.android.app.common.Cvalue;
import com.kportal.android.app.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by KR8 on 2016-11-09.
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    private Button defaultNoti, bigTextNoti, bigImageNoti;
    private ImageView ivImg;
//    private final String TAG = "TestActivity";

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.test_act);
        setLayout();

    }

    private void setLayout() {
        defaultNoti = (Button)findViewById(R.id.default_noti);
        bigTextNoti = (Button)findViewById(R.id.big_text_noti);
        bigImageNoti = (Button)findViewById(R.id.big_img_noti);
        ivImg = (ImageView)findViewById(R.id.iv_test_img);
        ivImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        defaultNoti.setOnClickListener(this);
        bigTextNoti.setOnClickListener(this);
        bigImageNoti.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.default_noti:
                sendNotification(1);
//                cameraIntent();
                break;

            case R.id.big_text_noti:
                sendNotification(2);
                break;

            case R.id.big_img_noti:
                sendNotification(3);
//                galleryIntent();
                break;
        }
    }

    private void sendNotification(int type) {
        Intent cIntent = new Intent(this, HomeActivity.class);
        cIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, cIntent, PendingIntent.FLAG_ONE_SHOT);
        String content = "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        if(type == 1){
            content = "Content Text";
        }else if(type == 2){
            content = "아래로 당겨 주세요";
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText("RProject Big Text Test")
                    .setBigContentTitle("Big Text Title")
                    .setSummaryText("Son Jin Woo");
            notificationBuilder.setStyle(bigTextStyle);
        }else if(type == 3){
            content = "아래로 당겨 주세요";
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.menu_02_on))
                    .setBigContentTitle("Big Picture Title")
                    .setSummaryText("Son Jin Woo");
            notificationBuilder.setStyle(bigPictureStyle);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_noti_white_test)
                .setColor(Color.parseColor("#FF000000"))
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Push Title")
                .setSound(defaultSoundUri)
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakeLock.acquire(5000);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private int getSmallNotiIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.bt_input : R.drawable.ic_control;
    }


    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(Intent.createChooser(intent, "SelectFile"), 1);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == 0) {
                onCaptureImageResult(data);
            } else if(requestCode == 1){
                onSelectFromGalleryResult(data);
            }
        }else{
            return;
        }
    }

    private void onCaptureImageResult(Intent data) {
        Cutil.recycleBitmap(ivImg);
        Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd'_'HH_mm_ss", Locale.KOREA);
        Date date = new Date();
        String fileName = "nb_img_"+simpleDateFormat.format(date)+".jpg";
        fileName.replace(":", "_");
        File destination = new File(Cvalue.BASE_FOLDER_PATH, fileName);
        FileOutputStream fo;
        try{
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        thumbnail = Cutil.resizeBitmapImage(thumbnail, Cvalue.HeightPixel);
        ivImg.setImageBitmap(thumbnail);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(destination)));
    }

    private void onSelectFromGalleryResult(Intent data) {
        Cutil.recycleBitmap(ivImg);
        Bitmap bm = null;
        if(data != null) {
            try{
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                bm = Cutil.resizeBitmapImage(bm, Cvalue.HeightPixel);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivImg.setImageBitmap(bm);
    }


}
