package org.asgs.exercises.java9.http2.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

public class HttpClientApp {

  public static void main(String[] args) {
    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest httpRequest =
        HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("http://www.google.co.in?q=hello+google"))
            .build();
    CompletableFuture<HttpResponse<String>> future =
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandler.asString());
    future.thenAccept(System.out::println).thenRun(() -> System.out.println("Done!"));
  }
}
