package com.deyizai.taptapdetaildemo;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created  on 2017-7-27.
 *
 * @author cdy
 * @version 1.0.0
 */

public class PlayerBehavior extends CoordinatorLayout.Behavior<RelativeLayout>{

    public static boolean isVaild = true;

    public PlayerBehavior(Context context, AttributeSet attrs) {
        //不加
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        return dependency.getId() == R.id.appbarLayout && isVaild;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        Log.i("PlayerBehavior","onDependentViewChanged#");
        int[] depLoc = new int[2];
        float[] childLoc = new float[2];

        childLoc[0] = child.getX();
        childLoc[1] = child.getY();

        dependency.getLocationOnScreen(depLoc);

        child.setX(depLoc[0]);
        child.setY(depLoc[1]);

        return true;
    }
}
