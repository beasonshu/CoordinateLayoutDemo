package com.deyizai.taptapdetaildemo.player;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created  on 2017-7-27.
 *
 * @author cdy
 * @version 1.0.0
 */

public class Constant {
    //设置初始状态的ijkPlayer
    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;

    public static int IJK_MEDIA_PLAYER = PV_PLAYER__AndroidMediaPlayer;
    public static int IJK_RENDER_VIEW = IJKPlayer.RENDER_SURFACE_VIEW;

    public static int IJK_RENDER_VIEW_DETAIL = IJKPlayer.RENDER_TEXTURE_VIEW;

    public static int IJK_LOG_DEBUG = IjkMediaPlayer.IJK_LOG_DEBUG;
    public static boolean IJK_MEDIA_CODEC = true;
    public static boolean IJK_OPENSLES = false;
}
