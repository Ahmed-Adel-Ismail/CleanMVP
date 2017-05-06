package com.base.presentation.requests;

/**
 * a Class that holds the result for
 * {@link android.app.Activity#onRequestPermissionsResult(int, String[], int[])}
 * <p/>
 * Created by Ahmed Adel on 9/5/2016.
 */
public class PermissionResult extends CodedResult {

  public final String[] permissions;
  public final int[] grantResults;

  public PermissionResult(int requestCode, String[] permissions, int[] grantResults) {
    super(requestCode);
    this.grantResults = grantResults;
    this.permissions = permissions;
  }
}
