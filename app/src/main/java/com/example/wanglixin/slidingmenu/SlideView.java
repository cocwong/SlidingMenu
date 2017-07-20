package com.example.wanglixin.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by wanglixin on 2017/7/12.
 */

public class SlideView extends RelativeLayout implements MenuFunction {
    private int layoutContent, layoutMenu;
    private int menuWidth;
    private View contentView, menuView;
    private boolean isOpen;
    private boolean isTransOnProgress;
    private Scroller mScroller;

    public SlideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlideView);
        layoutContent = ta.getResourceId(R.styleable.SlideView_slide_layout_content, -1);
        layoutMenu = ta.getResourceId(R.styleable.SlideView_slide_layout_menu, -1);
        menuWidth = ta.getDimensionPixelSize(R.styleable.SlideView_slide_menu_width, 800);
        ta.recycle();
        mScroller = new Scroller(context, new DecelerateInterpolator());
        init(context);
    }

    private void init(Context context) {
        contentView = addSlideView(context, layoutMenu);
        contentView.setBackgroundColor(Color.LTGRAY);
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
                scrollBy((int) -dx, 0);
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(getScrollX()) < menuWidth / 2) {
                    mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
                    invalidate();
                } else {
                    mScroller.startScroll(getScrollX(), 0, -(menuWidth - Math.abs(getScrollX())), 0);
                    invalidate();
                }
                break;
        }
        return true;
    }

    @Override
    public void openMenu() {
        mScroller.startScroll(getScrollX(), 0, menuWidth, 0);
        invalidate();
    }

    @Override
    public void closeMenu() {
        mScroller.startScroll(getScrollX(), 0, -menuWidth, 0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

}
