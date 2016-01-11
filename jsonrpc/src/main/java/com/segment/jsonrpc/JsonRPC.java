package com.segment.jsonrpc;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Make a JSON-RPC request. */
@Target(METHOD)
@Retention(RUNTIME)
public @interface JsonRPC {
  /** The name of RPC method being invoked by this call. */
  String value() default "";
}
