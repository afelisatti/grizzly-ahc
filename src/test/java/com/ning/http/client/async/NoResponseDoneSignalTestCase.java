/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.ning.http.client.async;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public abstract class NoResponseDoneSignalTestCase extends AbstractBasicTest {

  private int port3;

  @BeforeClass(alwaysRun = true)
  public void setUpServer() throws Exception {
    port3 = findFreePort();
    MockServer server = new MockServer();
    server.start();
    log.info("Local HTTP server started successfully");
  }

  @Test
  public void receivesRequestWhenServerClosesConnection() throws Throwable {
    try (AsyncHttpClient client = getAsyncHttpClient(new AsyncHttpClientConfig.Builder().build())) {
      Request request = new RequestBuilder("GET").setUrl("http://localhost:" + port3).build();
      Future<Response> responseFuture = client.executeRequest(request);
      Response response = responseFuture.get();
      Assert.assertEquals(response.getStatusCode(), HttpServletResponse.SC_OK);
    }
  }

  private class MockServer implements Runnable {

    public void start() {
      Thread serverThread = new Thread(this);
      serverThread.start();
    }

    @Override
    public void run() {
      try {
        ServerSocket serverSocket = new ServerSocket(port3);
        Socket socket = serverSocket.accept();
        OutputStream outputStream = socket.getOutputStream();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream), 1);
        writer.write("HTTP/1.1 200 OK\n"
// //                    UNCOMMENT TO SEE TEST WORK
//                       + "Connection: close\n"
                       + "\n"
                       + "200 OK\n");

        writer.close();
        socket.close();
        serverSocket.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }

}
