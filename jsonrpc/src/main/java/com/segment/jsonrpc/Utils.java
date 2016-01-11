package com.segment.jsonrpc;

import java.lang.annotation.Annotation;

final class Utils {
  private Utils() {
    throw new AssertionError("No instances");
  }

  /** Returns true if {@code annotations} contains an instance of {@code cls}. */
  static <T extends Annotation> boolean isAnnotationPresent(Annotation[] annotations,
      Class<T> cls) {
    return findAnnotation(annotations, cls) != null;
  }

  /** Returns an instance of {@code cls} if {@code annotations} contains an instance. */
  static <T extends Annotation> T findAnnotation(Annotation[] annotations,
      Class<T> cls) {
    for (Annotation annotation : annotations) {
      if (cls.isInstance(annotation)) {
        //noinspection unchecked
        return (T) annotation;
      }
    }
    return null;
  }
}
