package com.deyizai.taptapdetaildemo;

import android.content.Context;

/**
 * Created  on 2017-7-26.
 *
 * @author cdy
 * @version 1.0.0
 */

public class CommonFunc {

    /**
     * 用于格式化人数数据
     * @param context
     * @param value
     * @return
     */
    public static String getFormatForInt(Context context, int value){
        int wan = value / 10000;
        if(wan >= 10000){
            return wan / 10000 + context.getResources().getString(R.string.hundred_million) +"+";
        }
        else if(wan > 0){
            return wan + context.getResources().getString(R.string.ten_thousand) +"+";
        }
        else if(value > 0)
            return value +"";
        else
            return null;
    }

    /**
     * @param context 上下文
     * @param dpValue 设备独立像素dip
     * @return 像素值
     * @brief 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        // final float scale = 1f;
        // Trace.Info("缩放比例-->"+scale);
        return (int) (dpValue * scale + 0.5f);
    }
}
