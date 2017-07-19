package com.example.wanglixin.slidingmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by wanglixin on 2017/7/12.
 */

public class SlideView extends RelativeLayout implements MenuFunction, GestureDetector.OnGestureListener{
    private int layoutContent, layoutMenu;
    private int menuWidth;
    private View contentView, menuView;
    private float slidedWidth;
    private boolean isOpen;
    private GestureDetector detector;
    public SlideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlideView);
        layoutContent = ta.getResourceId(R.styleable.SlideView_slide_layout_content, -1);
        layoutMenu = ta.getResourceId(R.styleable.SlideView_slide_layout_menu, -1);
        menuWidth = ta.getDimensionPixelSize(R.styleable.SlideView_slide_menu_width, 800);
        ta.recycle();
        detector =  new GestureDetector(context,this);
        init(context);
    }

    private void init(Context context) {
        contentView = addSlideView(context, layoutMenu);
        contentView.setBackgroundColor(Color.BLUE);
        menuView = addSlideView(context, layoutContent);
    }

    private View addSlideView(Context context, int resId) {
        if (resId == -1) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.setLayoutParams(params);
            addView(frameLayout);
            return frameLayout;
        }
        return LayoutInflater.from(context).inflate(resId, this);
    }

    private float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - lastX;
                slidedWidth = contentView.getTranslationX() + dx;
                contentView.setTranslationX(slidedWidth);
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (slidedWidth < menuWidth / 2) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;
        }
        return true;
    }

    @Override
    public void openMenu() {
        contentView.animate().translationX(menuWidth).setDuration(250).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpen = true;
            }
        });
    }

    @Override
    public void closeMenu() {
        contentView.animate().translationX(0).setDuration(250).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpen = false;
            }
        });
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if(isOpen){
            closeMenu();
        }else {
            openMenu();
        }
        return false;
    }
}
