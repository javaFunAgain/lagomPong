/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package pl.setblack.pongi.game.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import pl.setblack.pongi.score.ScoreService;
import pl.setblack.pongi.users.UsersService;


public class GamesModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindServices(serviceBinding(GamesService.class, GamesServiceImpl.class));
    bindClient(UsersService.class);
    bindClient(ScoreService.class);
  }
}
