package com.appzoneltd.lastmile.driver.features.main.home;

import android.speech.tts.Voice;
import android.widget.Toast;

import com.base.abstraction.logs.Logger;
import com.base.abstraction.system.App;
import com.mapzen.helpers.RouteEngine;
import com.mapzen.helpers.RouteListener;
import com.mapzen.model.ValhallaLocation;
import com.mapzen.valhalla.Route;

/**
 * Created by Wafaa on 1/31/2017.
 */

 class RouteListenerImpl implements RouteListener {

    private HomeViewModel viewModel;
    private Route route;

    RouteListenerImpl(HomeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onRouteStart() {
        viewModel.speakerBox.play(route.getRouteInstructions().get(0)
                .getVerbalPreTransitionInstruction());
    }

    @Override
    public void onRecalculate(ValhallaLocation location) {
        Logger.getInstance().error(getClass(), "changedLocation : [" + location.getLatitude() + ","
                + location.getLongitude() + "]");
        if(App.getInstance().isDebugging()) {
            Toast.makeText(viewModel.getHostActivity(), "route is changed", Toast.LENGTH_SHORT).show();
        }
        viewModel.routeChangedObservable.onNext(location);
    }

    @Override
    public void onSnapLocation(ValhallaLocation originalLocation, ValhallaLocation snapLocation) {
        Logger.getInstance().error(getClass(), "snapLocation : [" + snapLocation.getLatitude() + " , "
                + snapLocation.getLongitude() + "]");
        viewModel.locationChangedObservable.onNext(snapLocation);
    }

    @Override
    public void onMilestoneReached(int index, RouteEngine.Milestone milestone) {
        if (!isLastInstruction(index)) {
            viewModel.speakerBox.playMilestone(route.getRouteInstructions().get(index), milestone);
        }
    }

    @Override
    public void onApproachInstruction(int index) {
        if (!isLastInstruction(index)) {
            viewModel.speakerBox.play(route.getRouteInstructions().get(index).getVerbalPreTransitionInstruction());
        }
    }

    @Override
    public void onInstructionComplete(int index) {
        if (index != 0) {
            viewModel.speakerBox.play(route.getRouteInstructions().get(index).getVerbalPostTransitionInstruction());
        }
    }

    @Override
    public void onUpdateDistance(int distanceToNextInstruction, int distanceToDestination) {
    }

    @Override
    public void onRouteComplete() {
        int finalIndex = route.getRouteInstructions().size() - 1;
        viewModel.speakerBox.play(route.getRouteInstructions()
                .get(finalIndex).getVerbalPreTransitionInstruction());
        int lastLocationsSize = route.getGeometry().size() - 1;
        viewModel.clearRouteAndGoToLastPoint(route.getGeometry().get(lastLocationsSize));
        viewModel.engine.setListener(null);
    }

    private boolean isLastInstruction(int index) {
        return index == route.getRouteInstructions().size() - 1;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
