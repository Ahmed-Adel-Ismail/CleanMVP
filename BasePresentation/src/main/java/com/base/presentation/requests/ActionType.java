package com.base.presentation.requests;

import java.io.Serializable;

/**
 * an action related to a {@link android.content.Context}, like starting an
 * {@link android.app.Activity} or a {@link android.app.Service} or finishing an
 * {@link android.app.Activity}
 */
public enum ActionType implements Serializable {
    START_ACTIVITY,
    START_SERVICE,
    FINISH

}