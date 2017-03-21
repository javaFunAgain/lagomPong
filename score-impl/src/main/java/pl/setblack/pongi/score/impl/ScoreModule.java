package pl.setblack.pongi.score.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import pl.setblack.pongi.score.ScoreService;

/**
 * Created by jarek on 3/21/17.
 */
public class ScoreModule extends AbstractModule implements ServiceGuiceSupport {

    @Override
    protected void configure() {
        bindServices(serviceBinding(ScoreService.class, ScoreServiceImpl.class));
    }
}
