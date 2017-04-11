package com.base.presentation.base.abstracts.features;

import android.view.View;

import com.base.abstraction.interfaces.Clearable;

import java.util.Arrays;
import java.util.List;

/**
 * handle clicks in the same time
 * <p>
 * Created by Wafaa on 10/17/2016.
 */
@Deprecated
public class MultipleClicksHandler implements Clearable {

    private AbstractActivity abstractActivity;
    private long lastClickTime;
    private List<Integer> nonConcurrentClicksIds;

    public MultipleClicksHandler(Integer... nonConcurrentClicksIds) {
        if (nonConcurrentClicksIds != null && nonConcurrentClicksIds.length != 0) {
            this.nonConcurrentClicksIds = Arrays.asList(nonConcurrentClicksIds);
        } else {
            throw new UnsupportedOperationException("no view Ids to be tracked, please pass "
                    + "at least on view id");
        }

    }

    void setAbstractActivity(AbstractActivity abstractActivity) {
        this.abstractActivity = abstractActivity;
    }

    void handleOnClick(View view) {
        if (validClick(view.getId())) {
            updateClickTimeAndInvokeOnClick(view);
        }
    }

    private boolean validClick(int id) {
        return nonConcurrentClicksIds == null
                || !(nonConcurrentClicksIds.contains(id))
                || System.currentTimeMillis() - lastClickTime > 400;
    }

    private void updateClickTimeAndInvokeOnClick(View view) {
        lastClickTime = System.currentTimeMillis();
        abstractActivity.invokeOnClick(view);
    }


    @Override
    public void clear() {
        abstractActivity = null;
        nonConcurrentClicksIds = null;
    }
}
