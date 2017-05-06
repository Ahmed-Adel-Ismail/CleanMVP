package com.base.presentation.listeners;

import android.media.MediaPlayer;

/**
 * an Object that holds the parameters received from {@link MediaPlayer.OnErrorListener}
 * <p>
 * Created by Ahmed Adel Ismail on 4/20/2017.
 */
public class OnMediaPlayerErrorParams {

    public final MediaPlayer mediaPlayer;
    public final int what;
    public final int extra;

    public OnMediaPlayerErrorParams(MediaPlayer mediaPlayer, int what, int extra) {
        this.mediaPlayer = mediaPlayer;
        this.what = what;
        this.extra = extra;
    }
}
