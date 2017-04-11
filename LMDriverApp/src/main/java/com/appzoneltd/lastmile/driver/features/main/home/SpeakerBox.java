package com.appzoneltd.lastmile.driver.features.main.home;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.text.TextUtils;

import com.appzoneltd.lastmile.driver.R;
import com.base.abstraction.interfaces.Clearable;
import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.AppResources;
import com.mapzen.helpers.RouteEngine;
import com.mapzen.valhalla.Instruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Wafaa on 2/6/2017.
 */

 class SpeakerBox implements TextToSpeech.OnInitListener, Clearable{

    private final TextToSpeech textToSpeech;
    private static final String UTTERANCE_ID_NONE = "-1";
    private int queueMode = TextToSpeech.QUEUE_FLUSH;
    private String playOnInit;
    private boolean initialized;
    private boolean muted = false;


    public SpeakerBox(Context context) {
        this.textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            initialized = true;
            textToSpeech.setSpeechRate(1);
            if (playOnInit != null) {
                playInternal(playOnInit, UTTERANCE_ID_NONE);
            }
        } else {
            Logger.getInstance().error(getClass(), "Initialization failed.");
        }
    }


    private void playInternal(String text, String utteranceId) {
        if (muted) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, queueMode, null, utteranceId);
        } else {
            final HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
            textToSpeech.speak(text, queueMode, params);
        }
    }


    /**
     * Find the runnable for a given utterance id, run it on the main thread and then remove
     * it from the map
     *
     * @param utteranceId the id key to use
     * @param hashMap     utteranceIds to runnable map to use
     * @return whether value was found
     */
    private boolean detectAndRun(String utteranceId, HashMap<String, Runnable> hashMap) {
        if (hashMap.containsKey(utteranceId)) {
            Runnable runnable = hashMap.get(utteranceId);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(runnable);
            hashMap.remove(utteranceId);
            return true;
        } else {
            return false;
        }
    }

    public void play(CharSequence text) {
        play(text.toString());
    }


    public void play(String text) {
        if (initialized) {
            String utteranceId = String.valueOf(SystemClock.currentThreadTimeMillis());
            playInternal(text, utteranceId);
        } else {
            playOnInit = text;
        }
    }

    void playMilestone(Instruction instruction, RouteEngine.Milestone milestone) {
        if (TextUtils.isEmpty(instruction.getVerbalTransitionAlertInstruction())) {
            return;
        }
        playMilestoneMiles(instruction, milestone);
    }

    private void playMilestoneMiles(Instruction instruction, RouteEngine.Milestone milestone) {
        if (milestone.equals(RouteEngine.Milestone.ONE_MILE)) {
            play(AppResources.string(R.string.milestone_one_and_a_half_km) +
                    instruction.getVerbalTransitionAlertInstruction());
        } else if (milestone.equals(RouteEngine.Milestone.TWO_MILE)) {
            play(AppResources.string(R.string.milestone_three_km) +
                    instruction.getVerbalTransitionAlertInstruction());
        } else if (milestone.equals(RouteEngine.Milestone.QUARTER_MILE)) {
            play(AppResources.string(R.string.milestone_half_km) +
                    instruction.getVerbalTransitionAlertInstruction());
        }
    }

    @Override
    public void clear() {
        textToSpeech.stop();
        textToSpeech.shutdown();
    }
}
