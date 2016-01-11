package com.segment.jsonrpc;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.MoshiConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonRPCAdapterTest {
  @Rule public final MockWebServer server = new MockWebServer();

  Retrofit retrofit;

  @Before public void setUp() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient client = new OkHttpClient().newBuilder()
        .addInterceptor(logging)
        .build();

    retrofit = new Retrofit.Builder()
        .client(client)
        .baseUrl(server.url("/")) // Local Server: "http://localhost:1234"
        .addConverterFactory(JsonRPCConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build();
  }

  static class MultiplicationArgs {
    final int a;
    final int b;

    MultiplicationArgs(int a, int b) {
      this.a = a;
      this.b = b;
    }
  }

  interface MultiplicationService {
    @JsonRPC @POST("/rpc")
    Call<Integer> multiply(@Method("Arith.Multiply") @Body MultiplicationArgs args);
  }

  @Test public void foo() throws IOException {
    server.enqueue(new MockResponse()
        .addHeader("Content-Type", "application/json; charset=utf-8")
        .setBody("{"
            + "\"id\": 4,"
            + "\"result\": 6"
            + "}" //
        ));

    MultiplicationService service = retrofit.create(MultiplicationService.class);

    MultiplicationArgs args = new MultiplicationArgs(2, 3);
    assertThat(service.multiply(args).execute().body()).isEqualTo(6);
  }
}
