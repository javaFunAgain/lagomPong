/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package pl.setblack.pongi.users.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import pl.setblack.pongi.users.UsersService;

public class UsersModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindServices(serviceBinding(UsersService.class, UsersServiceImpl.class));
  }
}
