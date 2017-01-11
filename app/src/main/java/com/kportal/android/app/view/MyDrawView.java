package com.kportal.android.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.kportal.android.app.R;
import com.kportal.android.app.common.Cvalue;

import java.util.ArrayList;

/**
 * Created by KR8 on 2016-11-14.
 */

public class MyDrawView extends View {
    private final String TAG = "MyDrawView";
    public final int RECT = 101;
    public final int CIRCLE = 102;
    public final int TRIANGLE = 103;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private Context mContext;
    private int mSize = 200;
    private int mGroupId = -1;
    private int mBallId = 0;
    int actionMode = 1;
    private ArrayList<ColorBall> colorBalls = new ArrayList<ColorBall>();
    private ArrayList<ArrayList<ColorBall>> mArrColorBalls = new ArrayList<ArrayList<ColorBall>>();
    private Point[][] points = new Point[5][5];
    int bX, bY = 0;
    private int mMode = RECT;
    private int nWidth = 10;
    private ArrayList<Integer> mArrModes = new ArrayList<Integer>();
    private int nPointIndex = 0;
    private ArrayList<Path> mArrPaths = new ArrayList<Path>();
    private boolean isDrawed = true;

    public void colorChanged(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    public void deleteFigure() {
        if(mArrPaths.size() > 0) {
            points[mArrPaths.size()-1][0] = null;
            mArrPaths.remove(mArrPaths.size()-1);
            mArrModes.remove(mArrPaths.size());
            if(mArrColorBalls.size() > 0)
                mArrColorBalls.remove(mArrPaths.size());
            invalidate();
        } else {
            Toast.makeText(mContext, "지울 도형이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public int getFigureCount() {
        return mArrPaths.size();
    }

    public void selectedMode(int nMode) {
        this.mMode = nMode;
        if(mArrPaths.size() < 5) {
            if(isDrawed == true) {
                mArrModes.add(mMode);
                isDrawed = false;
                mArrPaths.add(mPath);
            } else {
                mArrModes.set(mArrModes.size()-1, mMode);
                mArrPaths.set(mArrPaths.size()-1, mPath);
            }
        }
    }

    public void changedWidth(int width) {
        this.nWidth = width;
        mPaint.setStrokeWidth(nWidth);
        invalidate();
    }

    public int getCurrentColor() {
        return mPaint.getColor();
    }

    public int getNWidth() {
        return this.nWidth;
    }

    public MyDrawView(Context con) {
        super(con);
        this.mContext = con;
        init();
    }

    public MyDrawView(Context con, AttributeSet attrs) {
        super(con, attrs);
        this.mContext = con;
        init();
    }

    private void init() {
        mBitmap = Bitmap.createBitmap(Cvalue.WidthPixel, Cvalue.HeightPixel, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.app_black));
        mPaint.setStrokeWidth(nWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.alpha(0));
        canvas.drawBitmap(mBitmap,0, 0, null);
        for(int k=0; k<mArrPaths.size(); k++) {
            if(points[k][0] == null) {
                return;
            }
            int left, top, right, bottom;
            left = points[k][0].x;
            top = points[k][0].y;
            right = points[k][2].x;
            bottom = points[k][2].y;

            for(int i=1; i<points[k].length; i++) {
                left = left > points[k][i].x ? points[k][i].x : left;
                top = top > points[k][i].y ? points[k][i].y : top;
                right = right < points[k][i].x ? points[k][i].x : right;
                bottom = bottom < points[k][i].y ? points[k][i].y : bottom;
            }

            if(mArrModes.get(k) == TRIANGLE) {
                mPath = new Path();
                mPath.moveTo(((right+mArrColorBalls.get(k).get(0).getWidthOfBall()/2)+(left+mArrColorBalls.get(k).get(0).getWidthOfBall()/2))/2, (top+mArrColorBalls.get(k).get(0).getHeightOfBall()/2));
                mPath.lineTo((left+mArrColorBalls.get(k).get(0).getWidthOfBall()/2), (bottom+mArrColorBalls.get(k).get(0).getWidthOfBall()/2));
                mPath.lineTo((right+mArrColorBalls.get(k).get(0).getWidthOfBall()/2), (bottom+mArrColorBalls.get(k).get(0).getWidthOfBall()/2));
                mPath.lineTo(((right+mArrColorBalls.get(k).get(0).getWidthOfBall()/2)+(left+mArrColorBalls.get(k).get(0).getWidthOfBall()/2))/2, (top+mArrColorBalls.get(k).get(0).getHeightOfBall()/2));
                canvas.drawPath(mPath, mPaint);
            } else if(mArrModes.get(k) == RECT) {
                mPath = new Path();
                mPath.addRect(left+mArrColorBalls.get(k).get(0).getWidthOfBall()/2,
                        top+mArrColorBalls.get(k).get(0).getWidthOfBall()/2,
                        right+mArrColorBalls.get(k).get(2).getWidthOfBall()/2,
                        bottom+mArrColorBalls.get(k).get(2).getWidthOfBall()/2, Path.Direction.CW);
                canvas.drawPath(mPath, mPaint);
            } else if(mArrModes.get(k) == CIRCLE) {
                mPath = new Path();
                float cx = (left+mArrColorBalls.get(k).get(0).getWidthOfBall()/2+right+mArrColorBalls.get(k).get(0).getWidthOfBall()/2)/2;
                float cy = (top+mArrColorBalls.get(k).get(0).getWidthOfBall()/2+bottom+mArrColorBalls.get(k).get(0).getWidthOfBall()/2)/2;
                float radius = (float)Math.hypot(top-bottom, right-left)/2;
                mPath.addCircle(cx, cy, radius, Path.Direction.CCW);
                canvas.drawPath(mPath, mPaint);
            }

            if(Cvalue.nTouch == 5) {
                for(int i=0; i<mArrColorBalls.get(k).size(); i++) {
                    ColorBall ball = mArrColorBalls.get(k).get(i);
                    if(i != mArrColorBalls.get(k).size()-1) {
                        canvas.drawBitmap(drawableToBitmap(ball.getBitmap()), ball.getX(), ball.getY(), mPaint);
                    } else {
                        canvas.drawBitmap(drawableToBitmap(ContextCompat.getDrawable(mContext, R.drawable.ic_control)), ball.getX()-(ball.getWidthOfBall()/2), ball.getY()-(ball.getHeightOfBall()/2), mPaint);
                    }
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(Cvalue.nTouch == 3 || Cvalue.nTouch == 5) {
            int eventAction = event.getAction();
            int X = (int)event.getX();
            int Y = (int)event.getY();

            switch (eventAction) {
                case MotionEvent.ACTION_DOWN:
                    actionTouchDown(X, Y);
                    break;

                case MotionEvent.ACTION_MOVE:
                    if(Cvalue.nTouch == 5 && isDrawed) {
                        actionTouchMove(X, Y, mBallId, mGroupId, nPointIndex);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    actionMode = 0;
                    nPointIndex = -1;
                    bX = 0;
                    bY = 0;
                    isDrawed = true;
                    break;
            }
            return true;
        } else {
            return false;
        }
    }

    public void actionTouchDown(int mX, int mY) {
        bX = mX;
        bY = mY;
        for(int z = 0; z < mArrPaths.size(); z++) {
            if(points[z][0] == null) {
                points[z][0] = new Point();
                points[z][0].x = mX-(mSize/2);
                points[z][0].y = mY-(mSize/2);

                points[z][1] = new Point();
                points[z][1].x = mX-(mSize/2);
                points[z][1].y = mY+(mSize/2);

                points[z][2] = new Point();
                points[z][2].x = mX+(mSize/2);
                points[z][2].y = mY+(mSize/2);

                points[z][3] = new Point();
                points[z][3].x = mX+(mSize/2);
                points[z][3].y = mY-(mSize/2);

                points[z][4] = new Point();
                points[z][4].x = mX;
                points[z][4].y = mY;

                colorBalls = new ArrayList<ColorBall>();
                for(Point pt : points[z]) {
                    colorBalls.add(new ColorBall(getContext(), R.drawable.figure_dot, pt));
                }
                mArrColorBalls.add(colorBalls);
            } else {
                for(int w = mArrColorBalls.get(z).size()-1; w >= 0; w--) {
                    ColorBall ball = mArrColorBalls.get(z).get(w);
                    int centerX = ball.getX();
                    int centerY = ball.getY();

                    double radCircle = Math.sqrt((double)(((centerX - mX) * (centerX - mX)) + (centerY - mY) * (centerY - mY)));
                    if(radCircle < ball.getWidthOfBall()) {
                        mBallId = ball.getID();
                        nPointIndex = z;
                        if(mBallId == 0 || mBallId == 2) {
                            mGroupId = 2;
                        } else if(mBallId == 1 || mBallId == 3) {
                            mGroupId = 1;
                        } else {
                            mGroupId = 3;
                        }
                        break;
                    }
                }
            }
        }
        invalidate();
    }

    private void actionTouchMove(int mX, int mY, int ballID, int groupId, int nPointIndex) {
        if(nPointIndex > -1) {
            mArrColorBalls.get(nPointIndex).get(ballID).setX(mX);
            mArrColorBalls.get(nPointIndex).get(ballID).setY(mY);
            if(groupId == 2) {
                mArrColorBalls.get(nPointIndex).get(1).setX(mArrColorBalls.get(nPointIndex).get(0).getX());
                mArrColorBalls.get(nPointIndex).get(1).setY(mArrColorBalls.get(nPointIndex).get(2).getY());
                mArrColorBalls.get(nPointIndex).get(3).setX(mArrColorBalls.get(nPointIndex).get(2).getX());
                mArrColorBalls.get(nPointIndex).get(3).setY(mArrColorBalls.get(nPointIndex).get(0).getY());
            } else if(groupId == 1) {
                mArrColorBalls.get(nPointIndex).get(0).setX(mArrColorBalls.get(nPointIndex).get(1).getX());
                mArrColorBalls.get(nPointIndex).get(0).setY(mArrColorBalls.get(nPointIndex).get(3).getY());
                mArrColorBalls.get(nPointIndex).get(2).setX(mArrColorBalls.get(nPointIndex).get(3).getX());
                mArrColorBalls.get(nPointIndex).get(2).setY(mArrColorBalls.get(nPointIndex).get(1).getY());
            } else {
                int deltaX = mX-bX;
                int deltaY = mY-bY;
                bX = mX;
                bY = mY;
                mArrColorBalls.get(nPointIndex).get(0).setX(mArrColorBalls.get(nPointIndex).get(0).getX()+deltaX);
                mArrColorBalls.get(nPointIndex).get(0).setY(mArrColorBalls.get(nPointIndex).get(0).getY()+deltaY);
                mArrColorBalls.get(nPointIndex).get(1).setX(mArrColorBalls.get(nPointIndex).get(1).getX()+deltaX);
                mArrColorBalls.get(nPointIndex).get(1).setY(mArrColorBalls.get(nPointIndex).get(1).getY()+deltaY);
                mArrColorBalls.get(nPointIndex).get(2).setX(mArrColorBalls.get(nPointIndex).get(2).getX()+deltaX);
                mArrColorBalls.get(nPointIndex).get(2).setY(mArrColorBalls.get(nPointIndex).get(2).getY()+deltaY);
                mArrColorBalls.get(nPointIndex).get(3).setX(mArrColorBalls.get(nPointIndex).get(3).getX()+deltaX);
                mArrColorBalls.get(nPointIndex).get(3).setY(mArrColorBalls.get(nPointIndex).get(3).getY()+deltaY);
            }
            mArrColorBalls.get(nPointIndex).get(4).setX((mArrColorBalls.get(nPointIndex).get(0).getX()+mArrColorBalls.get(nPointIndex).get(3).getX())/2);
            mArrColorBalls.get(nPointIndex).get(4).setY((mArrColorBalls.get(nPointIndex).get(0).getY()+mArrColorBalls.get(nPointIndex).get(1).getY())/2);
            invalidate();
        }
    }

    public static class ColorBall {
        private Drawable bitmap;
        private Context mContext;
        private Point point;
        private int id;
        static int count = 0;

        public ColorBall(Context context, int resourceId, Point point) {
            if(count > 4) {
                count = 0;
            }
            this.id = count++;
            bitmap = ContextCompat.getDrawable(context, resourceId);
            this.mContext = context;
            this.point = point;
        }

        public int getWidthOfBall() {
            return bitmap.getIntrinsicWidth();
        }

        public int getHeightOfBall() {
            return bitmap.getIntrinsicHeight();
        }

        public Drawable getBitmap() {
            return bitmap;
        }

        public int getX() {
            return point.x;
        }

        public int getY() {
            return point.y;
        }

        public int getID() {
            return id;
        }

        public void setX(int x) {
            point.x = x;
        }

        public void setY(int y) {
            point.y = y;
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
        drawable.draw(mCanvas);
        return bitmap;
    }
}