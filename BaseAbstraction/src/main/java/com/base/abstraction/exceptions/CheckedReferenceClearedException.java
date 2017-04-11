package com.base.abstraction.exceptions;

import com.base.abstraction.references.CheckedReference;

/**
 * a {@link RuntimeException} thrown when a {@link CheckedReference#get()} is invoked when the
 * Object has been cleared from memory
 * <p>
 * Created by Ahmed Adel on 10/30/2016.
 */
public class CheckedReferenceClearedException extends RuntimeException {
}
