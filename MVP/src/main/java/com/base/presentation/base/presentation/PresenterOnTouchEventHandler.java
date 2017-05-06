package com.base.presentation.base.presentation;

import android.view.MotionEvent;

import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.exceptions.OnTouchEventConsumedException;
import com.base.presentation.models.Model;

/**
 * a handler for the {@link AbstractActivity#onTouchEvent(MotionEvent)} invocations, if any
 * method threw {@link OnTouchEventConsumedException}, this will cause the {@link AbstractActivity}
 * to return {@code true} and stop invoking the rest of the handler methods, the event id
 * is based in {@link MotionEvent#getAction()} value, like {@link MotionEvent#ACTION_DOWN}
 * <p>
 * Created by Ahmed Adel Ismail on 4/20/2017.
 */
public class PresenterOnTouchEventHandler<
        P extends Presenter<P, V, M>,
        V extends ViewModel,
        M extends Model> extends PresenterHandler<MotionEvent, P, V, M> {


}
