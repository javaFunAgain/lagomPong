/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package pl.setblack.stream.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import pl.setblack.hello.api.HelloService;
import pl.setblack.stream.api.StreamService;

/**
 * The module that binds the StreamService so that it can be served.
 */
public class StreamModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    // Bind the StreamService service
    bindServices(serviceBinding(StreamService.class, StreamServiceImpl.class));
    // Bind the HelloService client
    bindClient(HelloService.class);
  }
}
