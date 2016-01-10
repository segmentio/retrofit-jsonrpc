package com.segment.jsonrpc;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class JsonRPCCallAdapterFactory implements CallAdapter.Factory {

  static JsonRPCCallAdapterFactory create() {
    return new JsonRPCCallAdapterFactory();
  }

  @Override
  public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
    if (!Utils.isAnnotationPresent(annotations, JsonRPC.class)) {
      return null;
    }

    Type parameterType = ((ParameterizedType) returnType).getActualTypeArguments()[0];
    Type responseType = Types.newParameterizedType(JsonRPCResponse.class, parameterType);

    CallAdapter delegate = retrofit.nextCallAdapter(this, returnType, annotations);
    return new JSONRPCCallAdapter<>(delegate, responseType);
  }

  static class JSONRPCCallAdapter<T> implements CallAdapter<T> {
    final CallAdapter<T> delegate;
    Type responseType;

    JSONRPCCallAdapter(CallAdapter<T> delegate, Type responseType) {
      this.delegate = delegate;
      this.responseType = responseType;
    }

    @Override public Type responseType() {
      return responseType;
    }

    @Override public <R> T adapt(final Call<R> call) {
      return delegate.adapt(call);
    }
  }
}