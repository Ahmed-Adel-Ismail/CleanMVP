package com.base.abstraction.interfaces;

/**
 * interface implemented by any class that has an operation and want to represent it's success
 * or failure
 * <p/>
 * Created by Ahmed Adel on 8/31/2016.
 */
public interface Successable {

  /**
   * check if the current Object state is Successful or not
   *
   * @return {@code true} if the current state is successful, else it will return {@code false}
   */
  boolean isSuccessful();

}