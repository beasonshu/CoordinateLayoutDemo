package com.deyizai.taptapdetaildemo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created  on 2017-7-26.
 * 解决在（Nested）scrollview中，wrap_content失效的问题
 * @author cdy
 * @version 1.0.0
 */

public class MeasurableViewPager extends ViewPager{
    public MeasurableViewPager(Context context) {
        super(context);
    }

    public MeasurableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /** TODO: 2017-7-27
         * 如果是Scrollview[viewpager[content]]格式，此时viewpager == content 高度，需要按content宽高调整的需要以下代码；
         * 现在是Viewpager[Scrollview[content]]格式，此时viewpager == scrollview == match_parent 即可,通过xml设置
         */
        /*
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        */
    }
}
