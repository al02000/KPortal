package com.kportal.android.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kportal.android.app.common.Cvalue;

import java.util.ArrayList;


/**
 * Created by KR8 on 2016-10-24.
 */

public class MyTouchView extends View {
    private final String TAG = "MyTouchView";
    public Paint mPaint;
//    public MaskFilter mEmboss;
//    public MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private float mX, mY, sX, sY;
    private final float TOUCH_TOLERANCE = 4;
    public boolean mEraserMode;
    private int nMode, nWidth = 5;
    public final int ERASER = 4;
    public final int STRAIGHT = 11;
    public final int CURVE = 12;
    public final int ARROW = 13;
    public final int DOTTED_LINE = 14;
    private ArrayList<PointF> mArrPoints = new ArrayList<PointF>();
    private boolean mSelect = false;
    private Paint mEraserPaint;

    public void setSelect(boolean select) {
        this.mSelect = select;
    }

    public void colorChanged(int color) {
        mPaint.setColor(color);
        this.mPaint = setPaint(nMode);
    }

    public void selectedMode(int nMode) {
        this.nMode = nMode;
        this.mPaint = setPaint(nMode);
    }

    public int getSelectedMode() {
        return this.nMode;
    }

    public void changedWidth(int width) {
        this.nWidth = width;
        this.mPaint = setPaint(nMode);
    }

    public int getnWidth() {
        return this.nWidth;
    }

    public MyTouchView(Context context) {
        super(context);
    }

    public void resetView() {
        mPath.reset();
        mBitmap.recycle();
        mBitmap = null;
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mBitmap = Bitmap.createBitmap(Cvalue.WidthPixel, Cvalue.HeightPixel, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        invalidate();
    }

    public MyTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //영역 크기
        mBitmap = Bitmap.createBitmap(Cvalue.WidthPixel, Cvalue.HeightPixel, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);

//        mEmboss = new EmbossMaskFilter(new float[]{1,1,1}, 0.4f, 6, 3.5f);
//        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        mEraserMode = false;
        mSelect = false;
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.alpha(0));
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        switch (nMode) {
            case CURVE:
                canvas.drawPath(mPath, mPaint);
                break;

            case DOTTED_LINE:
                canvas.drawPath(mPath, mPaint);
                break;

            case STRAIGHT:
                canvas.drawLine(sX, sY, mX, mY, mPaint);
                break;

            case ARROW:
                canvas.drawLine(sX, sY, mX, mY, mPaint);
                break;
        }
    }

    // 터치 시작
    private void touch_start(float x, float y) {
        mPath.reset();		// 패스를 초기화
        mPath.moveTo(x, y); // 좌표로 이동
        mX = x;				// 다음을 위해 x좌표를 mX에 저장
        mY = y;				// 다음을 위해 y좌표를 mY에 저장
        sX = x;
        sY = y;
        switch (nMode) {
            case ARROW:
                mArrPoints.clear();
                break;

            default:
                drawLine(x, y);
                mCanvas.drawPoint(mX, mY, mPaint);
                break;
        }
    }

    // 터치후 이동시
    private void touch_move(float x, float y, MotionEvent event) {
        if(mSelect) {
            switch (nMode) {
                case ARROW:
                    drawLine(x, y);
                    int hSize = event.getHistorySize();
                    PointF aux, auxH;
                    if(hSize > 0) {
                        for(int i=0; i<hSize; i++) {
                            auxH = new PointF(event.getHistoricalX(i), event.getHistoricalY(i));
                            mArrPoints.add(auxH);
                        }
                    }
                    aux = new PointF(x, y);
                    mArrPoints.add(aux);
                    break;

                default:
                    drawLine(x, y);
                    break;
            }
        }
    }

    public void drawLine(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
            if(nMode == ERASER) {
                mCanvas.drawPath(mPath, mPaint);
            }
        }
    }

    private void touch_up(float fx, float fy) {
        if(mSelect) {
            if(nMode == CURVE || nMode == DOTTED_LINE) {
                mPath.lineTo(mX, mY); // 줄긋기
                // commit the path to our offscreen
                mCanvas.drawPath(mPath, mPaint);
            } else if(nMode == STRAIGHT) {
                mCanvas.drawLine(sX, sY, mX, mY, mPaint);
            } else if(nMode == ARROW) {
                PointF pEnd = new PointF();
                pEnd.x = fx;
                pEnd.y = fy;
                Path arrPath = drawArrow(pEnd);
                mCanvas.drawLine(sX, sY, mX, mY, mPaint);
                mCanvas.drawPath(arrPath, mPaint);
                arrPath.reset();
            }
            // kill this so we don't double draw
            mPath.reset();
        }
    }

    public Path drawArrow(PointF pFinal) {
        float dx, dy;
        PointF p1, p2;
        PointF pStart;
        Path auxPath = new Path();
        if(mArrPoints.size() > 0) {
            PointF[] auxArray = mArrPoints.toArray(new PointF[mArrPoints.size()]);
            int index = 0;
            pStart = auxArray[index];

            dx = pFinal.x - pStart.x;
            dy = pFinal.y - pStart.y;

            float length = (float)Math.sqrt(dx * dx + dy * dy);
            float unitDx = dx / length;
            float unitDy = dy / length;

            final int arrowSize = 20;

            p1 = new PointF((float)(pFinal.x - unitDx * arrowSize - unitDy * arrowSize),
                    (float)(pFinal.y - unitDy * arrowSize + unitDx * arrowSize));

            p2 = new PointF((float)(pFinal.x - unitDx * arrowSize + unitDy * arrowSize),
                    (float)(pFinal.y - unitDy * arrowSize - unitDx * arrowSize));

            auxPath.moveTo(pFinal.x, pFinal.y);
            auxPath.lineTo(p1.x, p1.y);
            auxPath.moveTo(pFinal.x, pFinal.y);
            auxPath.lineTo(p2.x, p2.y);
            auxPath.close();
        }

        return auxPath;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(Cvalue.nTouch == 1) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;

                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y, event);
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    touch_up(x, y);
                    invalidate();
                    break;
            }
            return true;
        }else{
            return false;
        }
    }

    public Paint setPaint(int nType) {
        mSelect = true;
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(nWidth);
        mPaint.setColor(this.mPaint.getColor());
        switch (nType) {
            case DOTTED_LINE:
                DashPathEffect dashPathEffect = new DashPathEffect(new float[]{5,nWidth+5}, 2);
                mPaint.setPathEffect(dashPathEffect);
                mPaint.setStrokeWidth(nWidth);
                mPaint.setColor(this.mPaint.getColor());
                break;

            case ERASER:
                mPaint.setStrokeWidth(125);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                break;
        }
        return mPaint;
    }
}