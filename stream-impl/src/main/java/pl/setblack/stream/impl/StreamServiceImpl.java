/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package pl.setblack.stream.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import pl.setblack.hello.api.HelloService;
import pl.setblack.stream.api.StreamService;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the HelloString.
 */
public class StreamServiceImpl implements StreamService {

  private final HelloService helloService;

  @Inject
  public StreamServiceImpl(HelloService helloService) {
    this.helloService = helloService;
  }

  @Override
  public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> stream() {
    return hellos -> completedFuture(
        hellos.mapAsync(8, name -> helloService.hello(name).invoke()));
  }
}
