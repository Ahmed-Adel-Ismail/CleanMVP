package com.base.presentation.base.abstracts.features;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * implement this interface on any Creatable View class (Activity / Fragment) so you can deal with
 * all the same way
 * <p>
 * Created by Ahmed Adel on 9/4/2016.
 */
interface CreatableView {

  /**
   * same as {@link android.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)},
   * invoke this method where you want to inflate the layout xml file, but you still dont want
   * to preInitialize your views, like in {@link android.app.Activity#onCreate(Bundle)} as well
   *
   * @param inflater           used in fragments
   * @param container          used in fragments
   * @param savedInstanceState the {@link Bundle} that is passed to fragments or
   *                           {@link android.app.Activity#onCreate(Bundle)}
   * @return same as {@link android.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
   */
  @Nullable
  View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                    @Nullable Bundle savedInstanceState);

  /**
   * same as {@link android.app.Fragment#onActivityCreated(Bundle)}, or in your Activity's
   * {@link android.app.Activity#onCreate(Bundle)} after you have set your content view
   *
   * @param savedInstanceState the {@link Bundle} that is passed to fragments or
   *                           {@link android.app.Activity#onCreate(Bundle)}
   */
  void onActivityCreated(@Nullable Bundle savedInstanceState);
}
