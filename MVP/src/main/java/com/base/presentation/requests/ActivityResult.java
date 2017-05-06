package com.base.presentation.requests;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.base.abstraction.system.AppResources;
import com.base.abstraction.R;

import java.io.Serializable;

/**
 * a Class that represents the parameters of
 * {@link android.app.Activity#onActivityResult(int, int, Intent)} method
 * <p/>
 * Created by Ahmed Adel on 9/4/2016.
 */
public class ActivityResult extends CodedResult {


  public final Intent data;
  public final int resultCode;

  public ActivityResult(int requestCode, int resultCode, Intent data) {
    super(requestCode);
    this.data = data;
    this.resultCode = resultCode;
  }

  /**
   * get the {@link Serializable} extra in the {@link #data} intent if available
   *
   * @param keyStringResource the {@code String} resource of the key value
   * @param <T>               the expected type of the value, it should be {@link Serializable}
   * @return the value casted to the desired type, or {@code null} if not available or if
   * the {@link #data} was {@code null}
   * @throws ClassCastException if the found value does not match the expected type
   */
  public <T extends Serializable> T getExtra(int keyStringResource)
          throws ClassCastException {
    if (data != null) {
      return getSerializableExtra(keyStringResource);
    }
    return null;

  }

  @Nullable
  @SuppressWarnings("unchecked")
  private <T extends Serializable> T getSerializableExtra(int keyStringResource) {
    Serializable s = data.getSerializableExtra(AppResources.string(keyStringResource));
    if (s != null) {
      return (T) s;
    } else {
      return null;
    }
  }


  /**
   * check if the result code is the same as the value of the passed {@code integer} resource
   *
   * @param resultCodeIntResource the {@link com.base.abstraction.R.integer} resource
   *                              that contains the result code value
   * @return {@code true} if the value of the {@code integer} resource value is the same as the
   * {@link #requestCode}
   * @see com.base.abstraction.R.integer#resultCodeOk
   * @see com.base.abstraction.R.integer#resultCodeCanceled
   * @see com.base.abstraction.R.integer#resultCodeFirstUser
   */
  public boolean hasResultCode(int resultCodeIntResource) {
    return AppResources.integer(resultCodeIntResource) == resultCode;
  }

  public boolean isResultCodeOk() {
    return hasResultCode(R.integer.resultCodeOk);
  }

  public boolean isResultCodeCancel() {
    return hasResultCode(R.integer.resultCodeCanceled);
  }

  public boolean isResultCodeFirstUser() {
    return hasResultCode(R.integer.resultCodeFirstUser);
  }


}
