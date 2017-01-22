/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package pl.setblack.pongi.users.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import pl.setblack.pongi.users.UsersService;


/**
 * The module that binds the HelloService so that it can be served.
 */
public class UsersModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindServices(serviceBinding(UsersService.class, UsersServiceImpl.class));
  }
}
