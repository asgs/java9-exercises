package org.asgs.exercises.java9.http2.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class ASyncHttpClientApp {
  private static ExecutorService executorService = Executors.newWorkStealingPool();

  public static void main(String[] args)
      throws ExecutionException, InterruptedException, IOException, InstantiationException,
          IllegalAccessException {
    ASyncHttpClientApp httpClientApp = new ASyncHttpClientApp();
    httpClientApp.makeGetCall();
    httpClientApp.makePostCall();

    // TODO - this is not the right way to wait for. Executor should be shutdown first and then
    // awaited for termination. Yet it is the only it works and NOT the way it's intended to.
    // Possible explanation is HttpClient delegates the call to another thread that's NOT managed by
    // the executorService which we passed to it. Hence executorService thinks its tasks are already
    // completed, thus it doesn't wait anymore.
    executorService.awaitTermination(10, TimeUnit.SECONDS);
    executorService.shutdown();
  }

  private void makeGetCall()
      throws IOException, InterruptedException, IllegalAccessException, InstantiationException {
    HttpRequest httpRequest =
        HttpRequest.newBuilder().GET().uri(URI.create("http://www.internet.com")).build();

    getHttpClient()
        .sendAsync(httpRequest, HttpResponse.BodyHandler.asString())
        .whenComplete(this::dumpResponse)
        .thenRun(() -> System.out.println("Done!"));
  }

  private void makePostCall() throws IOException, InterruptedException {
    HttpRequest httpRequest =
        HttpRequest.newBuilder()
            .POST(
                HttpRequest.BodyProcessor.fromByteArray(
                    getXmlPayload().getBytes(StandardCharsets.UTF_8)))
            .uri(URI.create("https://www.google.co.in/"))
            .build();

    CompletableFuture<HttpResponse<String>> completableFuture =
        getHttpClient().sendAsync(httpRequest, HttpResponse.BodyHandler.asString());
    completableFuture
        .whenComplete(this::dumpResponse)
        .thenRun(() -> System.out.println("Done again!"));
  }

  private HttpClient getHttpClient() {
    return HttpClient.newBuilder()
        .executor(executorService)
        // .followRedirects(HttpClient.Redirect.SECURE)
        .version(HttpClient.Version.HTTP_2)
        .build();
  }

  private String getXmlPayload() {
    return "<bookmark url=\"http://www.example.org/one\" time=\"2005-10-21T19:07:30Z\">\n"
        + "    <description>Example of a Delicious bookmark</description>\n"
        + "    <tags>\n"
        + "        <tag name=\"example\"/>\n"
        + "        <tag name=\"test\"/>\n"
        + "    </tags>\n"
        + "</bookmark>";
  }

  private <T> void dumpResponse(HttpResponse<T> httpResponse, Throwable error) {
    if (error != null) {
      System.err.print("Exception occurred! ");
      StackTraceElement[] stackTrace = error.getStackTrace();
      StringBuilder builder = new StringBuilder();
      for (StackTraceElement stackTraceElement : stackTrace) {
        builder.append(stackTraceElement.toString());
        builder.append('\n');
      }
      builder.append("----Caused By----:");
      stackTrace = error.getCause().getStackTrace();
      for (StackTraceElement stackTraceElement : stackTrace) {
        builder.append(stackTraceElement.toString());
        builder.append('\n');
      }
      System.err.println(builder.toString());
    }

    if (httpResponse != null) {
      System.out.println(
          "HTTP Status Line is " + httpResponse.statusCode() + " " + httpResponse.version());
      System.out.println("Response Headers are " + httpResponse.headers().map());
      System.out.println("Response Body is " + httpResponse.body());
    }
  }
}
