package org.asgs.exercises.java9.http2.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncHttpClientApp {
  private static ExecutorService executorService = Executors.newSingleThreadExecutor();

  public static void main(String[] args)
      throws ExecutionException, InterruptedException, IOException {
    SyncHttpClientApp httpClientApp = new SyncHttpClientApp();
    httpClientApp.makeGetCall();
    httpClientApp.makePostCall();
    executorService.shutdown();
  }

  private void makeGetCall() throws IOException, InterruptedException {
    HttpRequest httpRequest =
        HttpRequest.newBuilder().GET().uri(URI.create("http://asgs.tech")).build();
    HttpResponse<String> httpResponse =
        getHttpClient().send(httpRequest, HttpResponse.BodyHandler.asString());
    dumpResponse(httpResponse);
  }

  private void makePostCall() throws IOException, InterruptedException {
    HttpRequest httpRequest =
        HttpRequest.newBuilder()
            .POST(
                HttpRequest.BodyProcessor.fromByteArray(
                    getXmlPayload().getBytes(StandardCharsets.UTF_8)))
            .uri(URI.create("https://www.google.co.in/"))
            .build();
    HttpResponse<String> httpResponse =
        getHttpClient().send(httpRequest, HttpResponse.BodyHandler.asString());
    dumpResponse(httpResponse);
  }

  private HttpClient getHttpClient() {
    return HttpClient.newBuilder()
        .executor(executorService)
        .followRedirects(HttpClient.Redirect.SECURE)
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

  private <T> void dumpResponse(HttpResponse<T> httpResponse) {
    System.out.println(
        "HTTP Status Line is " + httpResponse.statusCode() + " " + httpResponse.version());
    System.out.println("Response Headers are " + httpResponse.headers().map());
    System.out.println("Response Body is " + httpResponse.body());
  }
}
