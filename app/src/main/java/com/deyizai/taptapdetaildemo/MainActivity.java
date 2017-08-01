package com.deyizai.taptapdetaildemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.deyizai.taptapdetaildemo.player.Constant;
import com.deyizai.taptapdetaildemo.player.IJKPlayer;
import com.deyizai.taptapdetaildemo.player.IJKPlayerListener;
import com.deyizai.taptapdetaildemo.player.IRenderView;
import com.deyizai.taptapdetaildemo.player.TextureRenderView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created  on 2017-7-27.
 *
 * @author cdy
 * @version 1.0.0
 */

public class MainActivity extends AppCompatActivity {

    String iconUrl = "https://img.taptapdada.com/market/lcs/170978c6eb89f5a1ed5f4734b2cb9952_360.png";
    String vidoeUrl = "https://video2.taptapdada.com/t/20170228/240p_m3u8/loASOKZQgHC3kTJEmxy3tHj-hzo-.mp4/v.m3u8";

    String[] indicator_tabs;

    @Bind(R.id.tab_indicator)
    MagicIndicator indicator;
    
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.main_poster)
    ImageView poster;

    @Bind(R.id.smallScreen)
    RelativeLayout smallScreenView;

    @Bind(R.id.fullScreen)
    RelativeLayout fullScreenView;

    @Bind(R.id.game_icon)
    CircleImageView iconView;

    @Bind(R.id.appbarLayout)
    AppBarLayout appbarLayout;

    @Bind(R.id.toolbar)
    CommAlphaToolbar toolbar;

    IJKPlayer ijkPlayer;

    IRenderView mVideoView;

    Handler handler = new Handler();

    private boolean mIsTheTitleContainerVisible = true;
    private boolean mBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initIndicator();
        initViewPager();
        initAppBarLayout();

        Glide.with(this).load(iconUrl).into(iconView);

        ijkPlayer = new IJKPlayer(this,smallScreenView);

        ijkPlayer.initRenders(Constant.IJK_RENDER_VIEW);

        IJKPlayerListener listener = new IJKPlayerListener(handler,ijkPlayer);

        ijkPlayer.setOnCompletionListener(listener);
        ijkPlayer.setOnPreparedListener(listener);
        ijkPlayer.setOnErrorListener(listener);
        ijkPlayer.setOnInfoListener(listener);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTranslucentWindows(this);

        if(vidoeUrl!=null) {
            ijkPlayer.setVideoURI(Uri.parse(vidoeUrl), 0);
            smallScreenView.setVisibility(View.VISIBLE);
            poster.setVisibility(View.INVISIBLE);
            smallScreenView.setClickable(true);

            smallScreenView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ijkPlayer.isPlaying()) {
                        Log.i("MainActivity","onClick#"+getRequestedOrientation());
                        if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) smallScreenView.getLayoutParams();
                            oldHeight = params.height;
                            oldWidth = params.width;
                            params.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    //DecorView的宽高是按照竖屏的
                            smallScreenView.setLayoutParams(params);
                        }
                        else if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) smallScreenView.getLayoutParams();
                            params.height = oldHeight;
                            params.width = oldWidth;
                            smallScreenView.setLayoutParams(params);

                        }
                    }
                }
            });
            fullScreenView.setClickable(true);
            fullScreenView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ijkPlayer.isPlaying()) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                }
            });

        }
    }
    static public void setTranslucentWindows(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    int oldHeight,oldWidth;

    private void initAppBarLayout() {
        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int total = appBarLayout.getTotalScrollRange();
                Log.i("MainActivity","onOffsetChanged#total:"+total+"/verticalOffset:"+verticalOffset);
                float percentage = (float)Math.abs(verticalOffset)/(float)total;
                Log.i("MainActivity","onOffsetChanged#");
                refleshAlpha(toolbar, 500, percentage);
            }
        });
    }

    private void refleshAlpha(CommAlphaToolbar toolbar, int duration, float percent) {
        int color = (int)(((0xff - 0x20) * percent + 0x20)) << 24;
        toolbar.setBackgroundColor(color);
    }

    private void initViewPager() {
        viewPager.setAdapter(new GameDetailFragmentAdapter(getSupportFragmentManager()));
    }

    int unselect_color,select_color;

    private void initIndicator() {
        indicator_tabs = getResources().getStringArray(R.array.game_tabs);
        CommonNavigator navigator = new CommonNavigator(this);
        unselect_color = getResources().getColor(R.color.textColor_unselect);
        select_color = getResources().getColor(R.color.textColor_select);
        navigator.setAdjustMode(true);//自动调整
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return indicator_tabs.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int i) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(unselect_color);
                colorTransitionPagerTitleView.setSelectedColor(select_color);
                colorTransitionPagerTitleView.setText(indicator_tabs[i]);
                colorTransitionPagerTitleView.setTextSize(16);
                colorTransitionPagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(i);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
                indicator.setColors(select_color);
                return indicator;
            }
        });
        indicator.setNavigator(navigator);
        final LinearLayout container = navigator.getTitleContainer();
        container.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        ColorDrawable d = new ColorDrawable(0x30000000){
            @Override
            public int getIntrinsicWidth() {
                return 1;
            }

        };
        container.setDividerPadding(UIUtil.dip2px(this,10));
        container.setDividerDrawable(d);
        ViewPagerHelper.bind(indicator,viewPager);

    }

    public class GameDetailFragmentAdapter extends FragmentPagerAdapter{

        ArrayList<Fragment> fs = new ArrayList<>();

        public GameDetailFragmentAdapter(FragmentManager fm) {
            super(fm);
            fs.add(new Fragment_Detail());
            fs.add(new Fragment_Evalue());
            fs.add(new Fragment_Discuss());
        }

        @Override
        public Fragment getItem(int position) {
            return fs.get(position);
        }

        @Override
        public int getCount() {
            return fs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return indicator_tabs[position];
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        mBackPressed = true;
        super.onBackPressed();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //点击返回或不允许后台播放时 释放资源
        if (mBackPressed) {
            ijkPlayer.stopPlayback();
            ijkPlayer.release(true);
        }
    }

    /*
    public void changeScreenOrientation(boolean isLandScape){
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            if (isLandScape) {
                fullScreenView.setVisibility(View.VISIBLE);
                ((ViewGroup)videoView.getParent()).removeAllViews();
                fullScreenView.addView(videoView);
                int mHideFlags =
                        View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        ;
                fullScreenView.setSystemUiVisibility(mHideFlags);
            }
        }
        else if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if (!isLandScape) {
                fullScreenView.setVisibility(View.GONE);
                fullScreenView.removeAllViews();
                ((ViewGroup)videoView.getParent()).removeAllViews();
                smallScreenView.addView(videoView);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                int mShowFlags =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                fullScreenView.setSystemUiVisibility(mShowFlags);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }
    */
}
