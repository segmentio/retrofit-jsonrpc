# retrofit-jsonrpc

JSON-RPC with Retrofit.

# Usage

Declare your RPC Service.

```java
interface MultiplicationService {
    @JsonRPC @POST("/rpc")
    Call<Integer> multiply(@Method("Arith.Multiply") @Body MultiplicationArgs args);
}
```

Register the `JsonRPCConverterFactory` while building your `Retrofit` instance.
This must be done before any other converters are applied.

```java
Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://localhost:1234")
        .addConverterFactory(JsonRPCConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build();
```

Use Retrofit to build your service.

```java
MultiplicationService service = retrofit.create(MultiplicationService.class);
```

Use your service.

```java
service.multiply(MultiplicationArgs.create(2, 3)).execute().body(); // -> 6
```
