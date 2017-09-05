package com.deyizai.taptapdetaildemo.player;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.deyizai.taptapdetaildemo.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created  on 2017-8-2.
 *
 * @author cdy
 * @version 1.0.0
 */

public class ScreenVideoView extends RelativeLayout{

    public String vidoeUrl = "https://video2.taptapdada.com/t/20170228/240p_m3u8/loASOKZQgHC3kTJEmxy3tHj-hzo-.mp4/v.m3u8";

    public String localUrl = "/storage/emulated/0/tencent/MicroMsg/WeiXin/1504509811238.mp4";

    public IJKPlayer ijkPlayer;

    public LinearLayout funcBar;

    SeekBar seekBar;

    ImageView screenImgView,audioImgView;


    Handler handler;
    private int oldHeight;
    private int oldWidth;
    private float oldX,oldY;

    public ScreenVideoView(Context context) {
        super(context);
        init(context);
    }

    public ScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        funcBar = (LinearLayout)findViewById(R.id.func_bar);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setMax(1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               // Log.i("ScreenVideoView","onProgressChanged#progress:"+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                setProgress(progress);
            }
        });

        screenImgView = (ImageView) findViewById(R.id.screen_img);

        audioImgView = (ImageView)findViewById(R.id.audio_img);
        audioImgView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void init(Context context) {

        ijkPlayer = new IJKPlayer(context,this);

        ijkPlayer.initRenders(Constant.IJK_RENDER_VIEW);

        if(handler==null) {
            handler = new Handler(context.getMainLooper()){

                @Override
                public void dispatchMessage(Message msg) {
                    if(msg.what == 1){
                        setSeekBarProgress();
                        sendMessageDelayed(obtainMessage(1), 200);
                    }
                    super.dispatchMessage(msg);
                }
            };
            handler.sendEmptyMessage(1);
        }
        else{
            if(!handler.hasMessages(1)){
                handler.sendEmptyMessage(1);
            }
        }

        IJKPlayerListener listener = new IJKPlayerListener(handler,ijkPlayer);

        ijkPlayer.setOnCompletionListener(listener);
        ijkPlayer.setOnPreparedListener(listener);
        ijkPlayer.setOnErrorListener(listener);
        ijkPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {

                switch (i) {
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        hideLoading();
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        showLoading();
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        hideLoading();
                        break;
                }

                return false;
            }
        });

    }

    public void initPlay(String vidoeUrl){
        if(vidoeUrl!=null) {
            ijkPlayer.setVideoURI(Uri.parse(vidoeUrl), 0);
            setVisibility(View.VISIBLE);
            setClickable(true);
        }
    }

    public static int pos = 0;//按home键会调surfaceview的surfaceDestory方法，因此需要控制
    public static int dur = 0;

    private void setSeekBarProgress() {
        pos = ijkPlayer.getCurrentPosition();
        if(ijkPlayer.getDuration()!=0)
            dur = ijkPlayer.getDuration();
        seekBar.setProgress(1000 * pos / dur);
    }

    private void showLoading() {

    }

    private void hideLoading() {

    }

    private void setProgress(int progress) {
        int index = progress * ijkPlayer.getDuration() /1000;
        ijkPlayer.seekTo(index);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("##########   ScreenVideoView","dispatchTouchEvent#"+ev.getAction());
        //截取移动事件
        if(ev.getAction() == MotionEvent.ACTION_MOVE) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        else{
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setSmallScreen() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
        params.height = oldHeight;
        params.width = oldWidth;
        setX(oldX);
        setY(oldY);
        setLayoutParams(params);
    }

    public void setFullScreen(int fullHeight) {
        oldX = getX();
        oldY = getY();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
        oldHeight = params.height;
        oldWidth = params.width;
        setX(0);
        setY(0);
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = fullHeight;
        //DecorView的宽高是按照竖屏的
        setLayoutParams(params);
    }

    public void doOnDestory(){
        if(handler!=null) {
            Log.i("ScreenVideoView","doOnDestory#remove runnable   !!!");
            handler.removeMessages(1);
        }
        handler=null;
    }
}
