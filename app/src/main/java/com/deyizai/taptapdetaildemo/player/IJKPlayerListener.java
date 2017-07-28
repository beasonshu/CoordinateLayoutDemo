package com.deyizai.taptapdetaildemo.player;

import android.os.Handler;

import iapp.eric.utils.base.Trace;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created  on 2017-7-27.
 *
 * @author cdy
 * @version 1.0.0
 */

public class IJKPlayerListener implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener{

    private static final String TAG = "IJKPlayerListener # ";
    Handler mHandler;
    private boolean isPrepare;
    IJKPlayer ijkPlayer;
    private boolean isCarousel;

    public IJKPlayerListener(Handler handler,IJKPlayer ijkPlayer){
        mHandler = handler;
        this.ijkPlayer = ijkPlayer;
    }

    //TODO 控制播放状态的变化 ————————————————————————————————————————
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        Trace.Info(TAG + "onPrepared");
        isPrepare = true;
        if (ijkPlayer.getmTargetState() == IJKPlayer.STATE_PLAYBACK_COMPLETED) {
            Trace.Info(TAG + " onPrepared");
            iMediaPlayer.start();
        }
        if (!isCarousel) {
            iMediaPlayer.start();

        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        Trace.Info(TAG + "onCompletion");
        Trace.Info(TAG + this);
        isPrepare = false;
        if (isCarousel) {
            if (mCurrentRotateChannel == null) {
                iniVideoView(true, channelID);
            } else {
                iniVideoView(mCurrentRotateChannel);
            }

        } else {
            ijkPlayer.setVideoURI(ijkPlayer.getmUri(), 0);
        }


    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        Trace.Info(TAG + " onError " + "what = " + i + " extra = " + i1);
//        showError();
//        ijkPlayer.release(false);
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {

//        Trace.Info(TAG +" onInfo " + "what = " + i + " extra = " + i1);


        return false;
    }
}
