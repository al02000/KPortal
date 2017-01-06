package com.kportal.android.app.common;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.kportal.android.app.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;

/**
 * Created by KR8 on 2016-10-25.
 */

public class Cutil {

    private static final String TAG = "CUtil";


    /**
     경로에 이미 파일이 있을경우 파일을 삭제 한다
     낮은 버전에서는 파일을 덮어 쓸 경우 배경이 검은색으로 나오는 경우가 있음
     **/
    public static void resetImageFileIfIsExist(Uri uri) {
        if(uri != null) {
            File f = new File(uri.getPath());
            if(f.exists()) {
                f.delete();
            }
        }
    }

    public static void copyFile(File fromFile, File toFile) {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(fromFile);
            os = new FileOutputStream(toFile);
            FileChannel fcIn = is.getChannel();
            FileChannel fcOut = os.getChannel();
            long size = fcIn.size();
            fcIn.transferTo(0, size, fcOut);
            fcOut.close();
            fcIn.close();
            os.close();
            is.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 경로에 이미지 파일을 가져와서 이미지 회전 정보를 반환한다
     * @param src
     * @return
     * @throws IOException
     */
    public static int getExIfOrientation(String src) throws IOException {
        ExifInterface exif = null;
        try{
            exif = new ExifInterface(src);
        }catch (IOException e){
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        return orientation;
    }

    /**
     * 이미지 회전을 변경한다
     * @param bitmap
     * @param orientation
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
//        Log.i(TAG, "Orientation > "+orientation);
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 화면의 픽셀 정보를 반환한다
     * @param activity
     * @return
     */
    public static int[] getViewSize(Activity activity) {
        int nSize[] = new int[]{0,0};
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        nSize[0] = size.x;
        nSize[1] = size.y;
        return nSize;
    }

    /**
     * 이미지 뷰의 설정되어 있는 Bitmap 을 해제 한다
     * @param view
     */
    public static void recycleBitmap(ImageView view) {
        Drawable d = view.getDrawable();
        if(d instanceof BitmapDrawable) {
            Log.e(TAG, "RECYCLE");
            Bitmap b = ((BitmapDrawable)d).getBitmap();
            b.recycle();
        }
    }

    /**
     * 픽셀값을 받아서 해상도에 맞는 Dp값을 반환한다
     * @param context
     * @param px
     * @return
     */
    public static int convertToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int)(px/metrics.density);
        return dp;
    }

    /**
     * Dp값을 받아서 해당 Dp의 픽셀값을 반환한다
     * @param context
     * @param dp
     * @return
     */
    public static float convertToPx(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp*metrics.density;
        return px;
    }

    /**
     * 워터마크 삽입 메서드
     * @param context
     * @param src
     * @param view
     * @return
     */
    public static Bitmap addWaterMark(Context context, Bitmap src, Bitmap view) {
        int w = src.getWidth();
        int h = src.getHeight();
        int margin = (int)convertToPx(context, 50);
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        Bitmap waterMark = BitmapFactory.decodeResource(context.getResources(), R.drawable.watermark);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAlpha(0x80);
        canvas.drawBitmap(src, 0, 0, null);
        if(view != null) {
            canvas.drawBitmap(view, 0, 0, null);
        }
        canvas.drawBitmap(waterMark, w-(margin*2), h-(margin*2), paint);
        return result;
    }

    /**
     * 비트맵을 가로 / 세로 사이즈를 리사이징 한다
     * @param source
     * @param maxResolution
     * @return
     */
    public static Bitmap resizeBitmapImage(Bitmap source, int maxResolution) {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width > height) {
            if(maxResolution < width) {
                rate = maxResolution / (float)width;
                newHeight = (int)(height * rate);
                newWidth = maxResolution;
            }
        } else {
            if(maxResolution < height) {
                rate = maxResolution / (float)height;
                newWidth = (int)(width * rate);
                newHeight = maxResolution;
            }
        }
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    /**
     * 이미지 실제 경로를 반환한다.
     * @param cr
     * @param contentUri
     * @return
     */
    public static String getImageRealPathFromURI(ContentResolver cr, Uri contentUri) {
        // can post image
        String[] proj = { MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns._ID };

        Cursor cursor = cr.query(contentUri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        if (cursor == null) {
            return contentUri.getPath();
        } else {
            int path = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String tmp = cursor.getString(path);
            cursor.close();
            return tmp;
        }
    }
}
