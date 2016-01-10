package com.segment.jsonrpc;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.MoshiConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonRPCAdapterTest {

  static class RPCServiceArgs {
    int a;
    int b;
  }

  interface RPCService {
    @JsonRPC @POST("/rpc") Call<Integer>
    multiply(@Method("Arith.Multiply") @Body RPCServiceArgs args);
  }

  @Test public void foo() throws IOException {
    MockWebServer server = new MockWebServer();
    server.enqueue(new MockResponse()
        .setBody("{"
            + "\"id\": 4,"
            + "\"result\": 6"
            + "}" //
        ));
    server.start();
    HttpUrl baseUrl = server.url("/");

    assertThat("foo").isEqualToIgnoringCase("foo");

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient client = new OkHttpClient().newBuilder()
        .addInterceptor(logging)
        .build();

    Retrofit retrofit = new Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(JsonRPCConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(JsonRPCCallAdapterFactory.create())
        .build();

    RPCService rpcService = retrofit.create(RPCService.class);

    RPCServiceArgs args = new RPCServiceArgs();
    args.a = 2;
    args.b = 3;
    assertThat(rpcService.multiply(args).execute().body()).isEqualTo(6);
  }

  private static class LoggingInterceptor implements Interceptor {
    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
      long t1 = System.nanoTime();
      Request request = chain.request();
      System.out.println(String.format("Sending request %s on %s%n%s",
          request.url(), chain.connection(), request.headers()));
      Response response = chain.proceed(request);

      long t2 = System.nanoTime();
      System.out.println(String.format("Received response for %s in %.1fms%n%s",
          request.url(), (t2 - t1) / 1e6d, response.headers()));
      return response;
    }
  }
}
