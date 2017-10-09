package org.asgs.exercises.java9.http2.ws;

import jdk.incubator.http.WebSocket;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

/** */
public class WsListener implements WebSocket.Listener {

  @Override
  public void onOpen(WebSocket socket) {
    System.out.println("Opening connection to socket " + socket);
    socket.request(1);
  }

  @Override
  public CompletionStage<?> onText(
      WebSocket webSocket, CharSequence message, WebSocket.MessagePart part) {
    webSocket.request(1);
    System.out.println(message);
    return null;
  }

  @Override
  public void onError(WebSocket webSocket, Throwable error) {
    try {
      error.printStackTrace();
      webSocket.abort();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
    System.out.println("Received close from Remote WS server; Reason is " + reason);
    return null;
  }
}
