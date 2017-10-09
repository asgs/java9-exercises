package org.asgs.exercises.java9.http2.ws;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.WebSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WsClientApp implements WsCloseEventHandler {

  private HttpClient httpClient = HttpClient.newHttpClient();

  public static void main(String[] args)
      throws ExecutionException, InterruptedException, IOException {
    WsClientApp clientApp = new WsClientApp();
    CompletableFuture<WebSocket> wsFuture =
        clientApp
            .httpClient
            .newWebSocketBuilder(
                URI.create("ws://localhost:8080/jshell-ws/"), new WsListener(clientApp))
            .buildAsync();
    WebSocket webSocket = wsFuture.get();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String userInput = reader.readLine();
    while (!userInput.equals("/ex")) {
      webSocket.sendText(userInput, true);
      // System.out.println("Sending message to remote WS Server - " + userInput);
      userInput = reader.readLine();
    }
    System.out.println("Closing connection to remote WS Server.");
    webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Done!");
  }

  @Override
  public void onClose() {
    System.exit(0); // Our job is done here.
  }
}
