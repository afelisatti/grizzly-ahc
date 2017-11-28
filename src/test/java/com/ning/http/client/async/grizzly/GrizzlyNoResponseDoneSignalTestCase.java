/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.ning.http.client.async.grizzly;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.async.NoResponseDoneSignalTestCase;
import com.ning.http.client.async.ProviderUtil;

public class GrizzlyNoResponseDoneSignalTestCase extends NoResponseDoneSignalTestCase {

  @Override
  public AsyncHttpClient getAsyncHttpClient(AsyncHttpClientConfig config) {
    return ProviderUtil.grizzlyProvider(config);
  }
}
