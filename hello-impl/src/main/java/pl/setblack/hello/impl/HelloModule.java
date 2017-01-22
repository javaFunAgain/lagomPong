/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package pl.setblack.hello.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import pl.setblack.hello.api.HelloService;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class HelloModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindServices(serviceBinding(HelloService.class, HelloServiceImpl.class));
  }
}
