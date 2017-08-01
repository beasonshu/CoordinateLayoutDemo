package com.deyizai.taptapdetaildemo;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created  on 2017-7-27.
 *
 * @author cdy
 * @version 1.0.0
 */

public class CommAlphaToolbar extends Toolbar{

    View mContent;

    public CommAlphaToolbar(Context context) {
        super(context);
        init(context);
    }

    public CommAlphaToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommAlphaToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContent = LayoutInflater.from(context).inflate(R.layout.auto_toolbar,null);
        addView(mContent, WindowManager.LayoutParams.MATCH_PARENT ,WindowManager.LayoutParams.MATCH_PARENT);
    }

    int color;

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        //Log.i("CommAutoAlphaToolbar","setBackgroundColor#color");
        this.color = color;
        super.setBackgroundColor(color);
    }
}
