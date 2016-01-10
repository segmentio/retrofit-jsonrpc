package com.segment.jsonrpc;

import java.lang.annotation.Annotation;

final class Utils {
  private Utils() {
    throw new AssertionError("No instances");
  }

  /** Returns true if {@code annotations} contains an instance of {@code cls}. */
  static boolean isAnnotationPresent(Annotation[] annotations,
      Class<? extends Annotation> cls) {
    for (Annotation annotation : annotations) {
      if (cls.isInstance(annotation)) {
        return true;
      }
    }
    return false;
  }
}
