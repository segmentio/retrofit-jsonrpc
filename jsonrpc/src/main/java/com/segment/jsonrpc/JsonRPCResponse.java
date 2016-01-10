package com.segment.jsonrpc;

public class JsonRPCResponse<T> {
  long id;
  T result;
  Object error;
}
