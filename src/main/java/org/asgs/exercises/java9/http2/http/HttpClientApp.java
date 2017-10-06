package org.asgs.exercises.java9.http2.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpClientApp {
  private static ExecutorService executorService = Executors.newSingleThreadExecutor();

  public static void main(String[] args)
      throws ExecutionException, InterruptedException, IOException {
    HttpClientApp httpClientApp = new HttpClientApp();
    httpClientApp.makeGetCall();
    executorService.shutdown();
  }

  private void makeGetCall() throws IOException, InterruptedException {
    HttpClient httpClient =
        HttpClient.newBuilder()
            .executor(executorService)
            .followRedirects(HttpClient.Redirect.SECURE)
            .build();
    HttpRequest httpRequest =
        HttpRequest.newBuilder().GET().uri(URI.create("http://asgs.tech")).build();
    HttpResponse<String> httpResponse =
        httpClient.send(httpRequest, HttpResponse.BodyHandler.asString());
    dumpResponse(httpResponse);
  }

  private <T> void dumpResponse(HttpResponse<T> httpResponse) {
    System.out.println(
        "Status Line is " + httpResponse.statusCode() + " " + httpResponse.version());
    System.out.println("Headers are " + httpResponse.headers().map());
    System.out.println("Body is " + httpResponse.body());
  }
}
