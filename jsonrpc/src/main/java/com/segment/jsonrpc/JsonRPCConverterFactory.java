package com.segment.jsonrpc;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.*;
import retrofit2.Converter;

public class JsonRPCConverterFactory extends Converter.Factory {
  public static JsonRPCConverterFactory create() {
    return new JsonRPCConverterFactory();
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type,
      Annotation[] annotations,
      Retrofit retrofit) {
    if (Types.getRawType(type) != JsonRPCResponse.class) {
      return null;
    }

    final Converter<ResponseBody, JsonRPCResponse> delegate =
        retrofit.nextResponseBodyConverter(this, type, annotations);
    //noinspection unchecked
    return new JSONRPCResponseBodyConverter(delegate);
  }

  static class JSONRPCResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    final Converter<ResponseBody, JsonRPCResponse<T>> delegate;

    JSONRPCResponseBodyConverter(Converter<ResponseBody, JsonRPCResponse<T>> delegate) {
      this.delegate = delegate;
    }

    @Override public T convert(ResponseBody responseBody) throws IOException {
      JsonRPCResponse<T> response = delegate.convert(responseBody);
      return response.result;
    }
  }

  @Override
  public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    String method = null;
    for (Annotation annotation : annotations) {
      if (annotation instanceof Method) {
        method = ((Method) annotation).value();
      }
    }
    if (method == null) {
      return null;
    }

    final Converter<JsonRPCRequest, RequestBody> delegate =
        retrofit.nextRequestBodyConverter(this, JsonRPCRequest.class, annotations);
    //noinspection unchecked
    return new JSONRPCRequestBodyConverter(method, delegate);
  }

  static class JSONRPCRequestBodyConverter<T> implements Converter<T, RequestBody> {
    final String method;
    final Converter<JsonRPCRequest, RequestBody> delegate;

    JSONRPCRequestBodyConverter(String method, Converter<JsonRPCRequest, RequestBody> delegate) {
      this.method = method;
      this.delegate = delegate;
    }

    @Override public RequestBody convert(T value) throws IOException {
      return delegate.convert(JsonRPCRequest.create(method, value));
    }
  }
}