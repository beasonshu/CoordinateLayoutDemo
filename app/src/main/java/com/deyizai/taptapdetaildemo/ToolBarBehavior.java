package com.deyizai.taptapdetaildemo;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created  on 2017-7-27.
 *
 * @author cdy
 * @version 1.0.0
 */

public class ToolBarBehavior extends CoordinatorLayout.Behavior<CommAlphaToolbar>{


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CommAlphaToolbar child, View dependency) {
        return dependency instanceof LinearLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CommAlphaToolbar child, View dependency) {

        int[] depLoc = new int[2];

        dependency.getLocationOnScreen(depLoc);

        float depY = depLoc[1];

        child.getLocationOnScreen(depLoc);

        float childY = depLoc[1];

        if((int)childY + child.getHeight() == (int)depY){

        }

        return true;
    }
}
