package com.kportal.android.app.util;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

/**
 * Created by KR8 on 2016-12-07.
 */

public class ViewUnbindHelper {


    public static void unbindReferences(View view) {
        try{
            if(view != null) {
                unbindViewReferences(view);
                if(view instanceof ViewGroup){
                    unbindViewGroupReferences((ViewGroup)view);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unbindReferences(Activity activity, int viewID) {
        try{
            View view = activity.findViewById(viewID);
            if(view != null) {
                unbindViewReferences(view);
                if(view instanceof ViewGroup) {
                    unbindViewGroupReferences((ViewGroup)view);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void unbindViewGroupReferences(ViewGroup viewGroup) {
        int nrOfChildren = viewGroup.getChildCount();
//        Log.i("TEST", "child ==> "+nrOfChildren);
        for(int i=0; i<nrOfChildren; i++) {
            View view = viewGroup.getChildAt(i);
            unbindViewReferences(view);
            if(view instanceof ViewGroup) {
                unbindViewGroupReferences((ViewGroup)view);
            }
        }
        try{
            viewGroup.removeAllViews();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void unbindViewReferences(View view) {
        try{
            view.setOnClickListener(null);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            view.setOnCreateContextMenuListener(null);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        try {
            view.setOnFocusChangeListener(null);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        try {
            view.setOnKeyListener(null);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        try {
            view.setOnLongClickListener(null);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        try {
            view.setTouchDelegate(null);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        try{
            view.setOnTouchListener(null);
        }catch (Exception e){
            e.printStackTrace();
        }

        Drawable d = view.getBackground();
        if (d != null) {
            try {
                d.setCallback(null);
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }

        if (view instanceof ImageView) {
            ImageView imageView = (ImageView)view;
            d = imageView.getDrawable();
            if (d != null) {
                d.setCallback(null);
            }
            imageView.setImageDrawable(null);
        } else if (view instanceof WebView) {
            ((WebView)view).destroyDrawingCache();
            ((WebView)view).destroy();
        }

        try {
            view.setBackgroundDrawable(null);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        try {
            view.setAnimation(null);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        try {
            view.setContentDescription(null);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        try {
            view.setTag(null);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }
}
