package com.deyizai.taptapdetaildemo;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created  on 2017-7-26.
 *
 * @author cdy
 * @version 1.0.0
 */

public class EventableNestScrollView extends NestedScrollView{
    public EventableNestScrollView(Context context) {
        super(context);
        init();
    }

    private void init() {

    }

    public EventableNestScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventableNestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("EventableNestScrollView","dispatchTouchEvent#");
        return super.dispatchTouchEvent(ev);
    }

    float preY = 0,curY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.i("EventableNestScrollView","onTouchEvent#"+ev.getRawY());
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            preY = ev.getRawY();
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        Log.i("EventableNestScrollView","onOverScrolled#"+clampedY);
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

}
