package com.deyizai.taptapdetaildemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.deyizai.taptapdetaildemo.player.Constant;
import com.deyizai.taptapdetaildemo.player.IJKPlayer;
import com.deyizai.taptapdetaildemo.player.IJKPlayerListener;
import com.deyizai.taptapdetaildemo.player.IRenderView;
import com.deyizai.taptapdetaildemo.player.ScreenVideoView;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

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
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.deyizai.taptapdetaildemo.player.IJKPlayer.STATE_PAUSED;

/**
 * Created  on 2017-7-27.
 *
 * @author cdy
 * @version 1.0.0
 */

public class MainActivity extends AppCompatActivity {

    String iconUrl = "https://img.taptapdada.com/market/lcs/170978c6eb89f5a1ed5f4734b2cb9952_360.png";

    Handler handler = new Handler();

    String[] indicator_tabs;

    @Bind(R.id.tab_indicator)
    MagicIndicator indicator;
    
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.main_poster)
    ImageView poster;

    @Bind(R.id.smallScreen)
    ScreenVideoView smallScreenView;

    @Bind(R.id.func_bar)
    LinearLayout func_bar;

    @Bind(R.id.game_icon)
    CircleImageView iconView;

    @Bind(R.id.appbarLayout)
    AppBarLayout appbarLayout;

    @Bind(R.id.coorLayout)
    CoordinatorLayout coorLayout;

    @Bind(R.id.toolbar)
    CommAlphaToolbar toolbar;



    private boolean mIsTheTitleContainerVisible = true;
    private boolean mBackPressed = false;

    int oldHeight,oldWidth;
    float oldX,oldY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initIndicator();
        initViewPager();
        initAppBarLayout();
        initSearchFragment();

        Glide.with(this).load(iconUrl).into(iconView);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTranslucentWindows(this);

        func_bar.setVisibility(View.INVISIBLE);

            smallScreenView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                        /*
                            turnToPortrait();
                        */
                        smallScreenView.funcBar.setVisibility(View.VISIBLE);
                        delaySetFuncBarVisiablity(false);
                    }
                    else if(smallScreenView.ijkPlayer.isPlaying()) {
                        if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                            turnToLandscape();
                        }
                    }

                }
            });

    }

    public void turnToPortrait(){
        PlayerBehavior.isVaild = true;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        smallScreenView.setSmallScreen();
        toolbar.setVisibility(View.VISIBLE);
    }

    public void turnToLandscape(){
        PlayerBehavior.isVaild = false;//会有影响
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        smallScreenView.setFullScreen(getWindow().getDecorView().getWidth());
        //appbarLayout.setExpanded(true); 代码控制展开收缩，应该会有用
        func_bar.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.INVISIBLE);
        delaySetFuncBarVisiablity(false);
    }

    Disposable visObservable = null;

    private void delaySetFuncBarVisiablity(final boolean b) {
        Log.i("MainActivity","delaySetFuncBarVisiablity#begin");

        if(visObservable!=null && !visObservable.isDisposed()){
            visObservable.dispose();
        }

        visObservable =
                Observable
                .concat(Observable.create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> e) throws Exception {

                        if(b == (func_bar.getVisibility() == View.VISIBLE)){
                            Log.i("MainActivity","subscribe#111 # 1");
                            e.onNext(b);//不执行第二个Observable
                        }
                        else{
                            Log.i("MainActivity","subscribe#111 # 2");
                            e.onComplete();//继续执行第二个
                        }
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()),Observable.create(new ObservableOnSubscribe<Boolean>(){

                    @Override
                    public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                        Log.i("MainActivity","subscribe#222");
                        func_bar.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
                        e.onNext(b);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()))
                .delaySubscription(5, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.i("MainActivity","accept#成功执行！");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("MainActivity","accept#throwable#执行异常！");
                        throwable.printStackTrace();
                    }
                });

    }

    SearchFragment searchFragment;

    private void initSearchFragment() {
        searchFragment = SearchFragment.newInstance();

        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) { //这里处理逻辑
                 Toast.makeText(MainActivity.this, "听说你要搜"+keyword, Toast.LENGTH_SHORT).show();
                 }
        });

        toolbar.search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment.show(getSupportFragmentManager(),SearchFragment.TAG);
            }
        });
    }

    static public void setTranslucentWindows(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }




    private void initAppBarLayout() {
        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.i("MainActivity","onOffsetChanged#"+verticalOffset);
                int total = appBarLayout.getTotalScrollRange();
                float percentage = (float)Math.abs(verticalOffset)/(float)total;
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
        if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            turnToPortrait();
        }
        else
            super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /**点击返回或不允许后台播放时 释放资源,没什么用，因为surfacedestory在onStop前就调用了
         if(smallScreenView!=null && smallScreenView.ijkPlayer!=null && smallScreenView.ijkPlayer.canPause()) {
         smallScreenView.ijkPlayer.pause();
         }
         **/
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(smallScreenView!=null && smallScreenView.ijkPlayer!=null && !smallScreenView.ijkPlayer.isPlaying())
            if(smallScreenView.ijkPlayer.getmUri()==null){
                Log.i("MainActivity","onStart#url init");
                smallScreenView.initPlay(smallScreenView.vidoeUrl);
            }
            else{
                if(!smallScreenView.ijkPlayer.isPlaying()){
                        //smallScreenView.ijkPlayer.play();
                }
            }
    }

    @Override
    protected void onPause() {
        Log.i("MainActivity","onPause#begin");
        smallScreenView.ijkPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i("MainActivity","onResume#begin");
        //BUG ：直接start的话，音视频不同步
        smallScreenView.ijkPlayer.play();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smallScreenView.ijkPlayer.destory();
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
