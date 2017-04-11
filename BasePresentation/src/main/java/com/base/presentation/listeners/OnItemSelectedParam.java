package com.base.presentation.listeners;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Wafaa on 9/26/2016.
 */
public class OnItemSelectedParam {

    public AdapterView<?> parent;
    public View view;
    public int position;
    public long id;

    public OnItemSelectedParam(AdapterView<?> parent, View view, int position, long id){
        this.parent = parent;
        this.view = view;
        this.position = position;
        this.id = id;
    }

}
