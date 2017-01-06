//package com.kportal.android.app.common;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.ColorMatrix;
//import android.graphics.ColorMatrixColorFilter;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.media.ExifInterface;
//import android.net.Uri;
//import android.opengl.GLES20;
//import android.os.ParcelFileDescriptor;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.sjw.rproject.R;
//
//import java.io.File;
//import java.io.FileDescriptor;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.IntBuffer;
//
///**
// * Created by KR8 on 2016-11-21.
// */
//
//public class ImageUtil {
//
//    public static Bitmap getResizedResourceImage(Bitmap originBitmap, int newWidth, int newHeight) {
//        int width = originBitmap.getWidth();
//        int height = originBitmap.getHeight();
//
//        if(width == newWidth && height == newHeight) return originBitmap;
//
//        final float widthScale = (float) newWidth / (float) width;
//        final float heightScale = (float) newHeight / (float) height;
//
//        final int scaledWidth = (int) (width * widthScale);
//        final int scaledHeight = (int) (height * heightScale);
//
//        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originBitmap, scaledWidth, scaledHeight, true);
//
//        return resizedBitmap;
//    }
//
//    public static Bitmap getDisplayBitmap(View v, int width, int height) {
//        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
//        Canvas canvas = new Canvas(bmp);
//        Drawable d = v.getBackground();
//        if (BitmapDrawable.class.isInstance(d)) {
//            BitmapDrawable bd = (BitmapDrawable)d;
//            Bitmap bgbmp = bd.getBitmap();
//            if(bgbmp != null)	canvas.drawBitmap(bgbmp, 0, 0, new Paint());
//        }
//        v.draw(canvas);
//        return bmp;
//    }
//
//    public static Bitmap toBitmap(View v, int left, int top, int width, int height) {
//        v.measure(width, height);
//        v.layout(left, top, width, height);
//
//        boolean isDrawingCacheEnabled = v.isDrawingCacheEnabled();
//        v.setDrawingCacheEnabled(true);
//
//        Bitmap rbmp = null;
//        Bitmap bmp = v.getDrawingCache();
//        if(bmp != null) rbmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
//
//        v.setDrawingCacheEnabled(isDrawingCacheEnabled);
//
//        return rbmp;
//    }
//
//    public static Bitmap toBitmap(View v) {
//        Rect r = new Rect();
//        v.getDrawingRect(r);
//        Log.i("ghi", "   rect " + r.flattenToString());
//        return toBitmap(v, r.left, r.top, r.width(), r.height());
//    }
//
//    public static void recycle(Bitmap bmp) {
//        if(bmp != null && !bmp.isRecycled()) {
//            bmp.recycle();
//        }
//    }
//
//    public static void recycle(ImageView image) {
//        if(image == null) return;
//
//        Drawable d = image.getDrawable();
//        if(d != null && BitmapDrawable.class.isInstance(d))
//        {
//            BitmapDrawable bd = (BitmapDrawable)d;
//            Bitmap bm = bd.getBitmap();
//            if(bm != null) bm.recycle();
//        }
//    }
//
//    public static Bitmap resourceToBitmap(Context ctx, int resid) {
//        return BitmapFactory.decodeResource(ctx.getResources(), resid);
//    }
//
//    public static Bitmap drawableToBitmap(BitmapDrawable d) {
//        return d.getBitmap();
//    }
//
//    public static Drawable bitmapToDrawable(Bitmap b) {
//        return new BitmapDrawable(b);
//    }
//
//    protected static Canvas getCanvas(Bitmap b) {
//        if(b == null) {
//            return new Canvas();
//        }
//        else if(!b.isMutable()) {
//            Bitmap mutable = b.copy(Bitmap.Config.ARGB_8888, true);
//            return new Canvas(mutable);
//        } else {
//            return new Canvas(b);
//        }
//    }
//
//    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
//        int width, height;
//        height = bmpOriginal.getHeight();
//        width = bmpOriginal.getWidth();
//
//        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//        Canvas c = new Canvas(bmpGrayscale);
//        Paint paint = new Paint();
//        ColorMatrix cm = new ColorMatrix();
//        cm.setSaturation(0);
//        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//        paint.setColorFilter(f);
//        c.drawBitmap(bmpOriginal, 0, 0, paint);
//        return bmpGrayscale;
//    }
//
//
//    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int width, int height, int round) {
//        return getRoundedCornerBitmap(bitmap, width, height, round, false, 0.0f, 0.0f, 0.0f, 0);
//    }
//
//    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int width, int height, int round
//            , boolean shadow, float shadowRound, float shadowXOffset, float shadowYOffset, int shadowColor) {
//
//        Bitmap output = Bitmap.createBitmap(width + (int)shadowXOffset, height + (int)shadowXOffset, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0, 0, width, height);
//        final RectF rectF = new RectF(rect);
//        float roundPx = round;
//
//        final Rect srect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//        if (shadow) paint.setShadowLayer(shadowRound, shadowXOffset, shadowYOffset, shadowColor);
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//
//        canvas.drawBitmap(bitmap, srect, rect, paint);
//
//        return output;
//    }
//
//    public static Bitmap rotateBitmap(Bitmap bmp, int rotation) {
//        int w = bmp.getWidth();
//        int h = bmp.getHeight();
//        // Setting post rotate to 90
//        Matrix mtx = new Matrix();
//        mtx.postRotate(rotation);
//        // Rotating Bitmap
//        Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
//        return rotatedBMP;
//    }
//
//    public static Bitmap getBitmapByExifRotation(String imagepath, BitmapFactory.Options opt) {
//        Bitmap bitmap = BitmapFactory.decodeFile(imagepath, opt);
//        try {
//            ExifInterface exif = new ExifInterface(imagepath);
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//            int rotate = 0;
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    rotate -= 90;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    rotate -= 90;
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    rotate -= 90;
//            }
//            if (rotate != 0) {
//                Bitmap rbitmap = ImageUtil.rotateBitmap(bitmap, (-1) * rotate);
//                ImageUtil.recycle(bitmap);
//                return rbitmap;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//    public static Bitmap getBitmapByExifRotation(String imagePath) {
//        return getBitmapByExifRotation(imagePath, null);
//    }
//
//    /**
//     * save opengl es 20 surface screen to png file
//     * @param x
//     * @param y
//     * @param w
//     * @param h
//     * @param name	ex) "/sdcard/myapp/my.png"
//     * @param gles20	is OpenGL ES 2.0 version?
//     */
//    public static void saveImage(int x, int y, int w, int h, String name, boolean gles20) {
////        Log.i("ghi", "  imageutil save image : " + x + " , " + y + " , " + w + " , " + h);
//        Bitmap bmp = savePixels(x,y,w,h);
//        try {
//            FileOutputStream fos = new FileOutputStream(name);
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            try {
//                fos.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    public static Bitmap getLoading(Context ctx) {
//        Bitmap bm = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.figure_dot);
//        Bitmap newbm = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(newbm);
//        canvas.drawBitmap(bm, 0, 0, null);
//        return newbm;
//    }
//
//    public static Bitmap scaleImage(Context context, File imageFile, int maxWidth, int maxHeight) throws IOException {
//
//        String pathName = imageFile.getAbsolutePath();
//
//        BitmapFactory.Options dbo = new BitmapFactory.Options();
//        dbo.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(pathName, dbo);
//
//        int rotatedWidth, rotatedHeight;
//        int orientation = getOrientation(context, pathName);
//
//        if (orientation == 90 || orientation == 270) {
//            rotatedWidth = dbo.outHeight;
//            rotatedHeight = dbo.outWidth;
//        } else {
//            rotatedWidth = dbo.outWidth;
//            rotatedHeight = dbo.outHeight;
//        }
//
//        Bitmap srcBitmap;
//        if (rotatedWidth > maxWidth || rotatedHeight > maxHeight) {
//            float widthRatio = ((float) rotatedWidth) / ((float) maxWidth);
//            float heightRatio = ((float) rotatedHeight) / ((float) maxHeight);
//            float maxRatio = Math.max(widthRatio, heightRatio);
//
//            // Create the bitmap from file
//            BitmapFactory.Options opts = new BitmapFactory.Options();
//            opts.inSampleSize = (int) maxRatio;
//            srcBitmap = BitmapFactory.decodeFile(pathName, opts);
//        } else {
//            srcBitmap = BitmapFactory.decodeFile(pathName);
//        }
//
//        // if the orientation is not 0 (or -1, which means we don't know), we have to do a rotation.
//        if (orientation > 0) {
//            Matrix matrix = new Matrix();
//            matrix.postRotate(orientation);
//
//            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
//                    srcBitmap.getHeight(), matrix, true);
//        }
//        return srcBitmap;
//    }
//
//    public static int getOrientation(Context context, String imagepath) throws IOException {
//        ExifInterface exif = new ExifInterface(imagepath);
//        return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//    }
//
//    /**
//     * EXIF정보를 회전각도로 변환하는 메서드
//     * @param exifOrientation EXIF 회전각
//     * @return 실제 각도
//     */
//    public static int exifOrientationToDegrees(int exifOrientation) {
//        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
//            return 90;
//        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
//            return 180;
//        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
//            return 270;
//        }
//        return 0;
//    }
//
//    /**
//     * 이미지를 회전시킵니다.
//     *
//     * @param bitmap 비트맵 이미지
//     * @param degrees
//     *            회전 각도
//     * @return 회전된 이미지
//     */
//    public static Bitmap rotate(Bitmap bitmap, int degrees) {
//        if (degrees != 0 && bitmap != null) {
//            Matrix m = new Matrix();
//            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
//                    (float) bitmap.getHeight() / 2);
//
//            try {
//                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
//                        bitmap.getWidth(), bitmap.getHeight(), m, true);
//                if (bitmap != converted) {
//                    bitmap.recycle();
//                    bitmap = converted;
//                }
//            } catch (OutOfMemoryError ex) {
//                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
//            }
//        }
//        return bitmap;
//    }
//
//    public static String getImageRealPathFromURI(ContentResolver cr, Uri contentUri) {
//        // can post image
//        String[] proj = { MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns._ID };
//
//        Cursor cursor = cr.query(contentUri, proj, // Which columns to return
//                null, // WHERE clause; which rows to return (all rows)
//                null, // WHERE clause selection arguments (none)
//                null); // Order-by clause (ascending by name)
//
//        if (cursor == null) {
//            return contentUri.getPath();
//        } else {
//            int path = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String tmp = cursor.getString(path);
//            cursor.close();
//            return tmp;
//        }
//    }
//
//    public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri) throws IOException {
//        ParcelFileDescriptor parcelFileDescriptor = cr.openFileDescriptor(uri, "r");
//        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//        Bitmap img = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//        parcelFileDescriptor.close();
//        return img;
//    }
//
//    public static Bitmap savePixels(int x, int y, int w, int h) {
////        Log.i("ghi", "imageutil save pixels w*(y+h) ? " + w*(y+h));
//        int b[] = new int[w*(y+h)];
//        int bt[] = new int[w*h];
//        IntBuffer ib = IntBuffer.wrap(b);
//        ib.position(0);
//        GLES20.glReadPixels(x, 0, w, y+h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);
//        for(int i=0, k=0; i<h; i++, k++) {
//            //remember, that OpenGL bitmap is incompatible with Android bitmap
//            //and so, some correction need.
//            for(int j=0; j<w; j++) {
//                int pix = b[i*w+j];
//                int pb = (pix>>16)&0xff;
//                int pr = (pix<<16)&0x00ff0000;
//                int pix1 = (pix&0xff00ff00) | pr | pb;
//                bt[(h-k-1)*w+j] = pix1;
//            }
//        }
//        return Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
//    }
//}