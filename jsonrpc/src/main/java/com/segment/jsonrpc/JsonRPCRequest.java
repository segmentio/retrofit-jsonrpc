package com.segment.jsonrpc;

import java.util.Collections;
import java.util.List;
import java.util.Random;

class JsonRPCRequest {
  static final Random RANDOM = new Random();

  final String method;
  final List<Object> params;
  final long id;

  JsonRPCRequest(String method, List<Object> params, long id) {
    this.method = method;
    this.params = params;
    this.id = id;
  }

  static JsonRPCRequest create(String method, Object args) {
    long id = Math.abs(RANDOM.nextLong());
    return new JsonRPCRequest(method, Collections.singletonList(args), id);
  }
}
