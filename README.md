# retrofit-jsonrpc

JSON-RPC with Retrofit.

# Usage

Declare your RPC Service.

```java
interface MultiplicationService {
    @JsonRPC("Arith.Multiply") @POST("/rpc")
    Call<Integer> multiply(@Body MultiplicationArgs args);
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

# Download

*Note*: Only snapshot releases are available currently.

Download [the latest JAR](https://oss.sonatype.org/content/repositories/snapshots/com/segment/retrofit/jsonrpc/jsonrpc/) or grab via Maven:
```xml
<dependency>
  <groupId>com.segment.retrofit.jsonrpc</groupId>
  <artifactId>jsonrpc</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

or Gradle:

```groovy
compile 'com.segment.retrofit.jsonrpc:jsonrpc:1.0.0-SNAPSHOT'
```
