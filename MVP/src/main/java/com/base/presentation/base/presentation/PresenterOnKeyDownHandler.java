package com.base.presentation.base.presentation;

import android.view.KeyEvent;

import com.base.presentation.base.abstracts.features.AbstractActivity;
import com.base.presentation.exceptions.OnKeyDownConsumedException;
import com.base.presentation.models.Model;

/**
 * a handler for the {@link AbstractActivity#onKeyDown(int, KeyEvent)} invocations, if any
 * method threw {@link OnKeyDownConsumedException}, this will cause the {@link AbstractActivity}
 * to return {@code true} and stop invoking the rest of the handler methods, the event id
 * is based in {@link KeyEvent#getKeyCode()} value, like {@link KeyEvent#KEYCODE_VOLUME_UP}
 * <p>
 * Created by Ahmed Adel Ismail on 4/20/2017.
 */
public class PresenterOnKeyDownHandler<
        P extends Presenter<P, V, M>,
        V extends ViewModel,
        M extends Model> extends PresenterHandler<KeyEvent, P, V, M> {
}
