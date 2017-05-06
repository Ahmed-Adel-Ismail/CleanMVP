package com.base.presentation.base.abstracts.features;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

/**
 * An interface that holds the main UI life-cycle methods
 * <p/>
 * Created by Ahmed Adel on 10/16/2016.
 */
interface ScreenLifeCycle extends CreatableView {


    /**
     * called in {@code onActivityResult()} method , like
     * {@link Activity#onActivityResult(int, int, Intent)}  and
     * {@link Fragment#onActivityResult(int, int, Intent)}
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the {@link Intent} that holds data
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);


    /**
     * called in {@code onSaveInstanceState} method
     *
     * @param bundle the {@link Bundle} already saved
     */
    void onSaveInstanceState(Bundle bundle);

    /**
     * called in {@code onStart} method
     */

    void onStart();

    /**
     * called in {@code onResume()} method , like {@link Activity#onResume()} and
     * {@link Fragment#onResume()}
     */
    void onResume();

    /**
     * called in {@code onPause()} method , like {@link Activity#onPause()} and
     * {@link Fragment#onPause()}
     */
    void onPause();

    /**
     * called in {@code onDestroy()} method , like {@link Activity#onDestroy()} and
     * {@link Fragment#onDestroy()}
     */
    void onDestroy();


    /**
     * called in {@code onStop} method
     */
    void onStop();


}
