package verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import com.commons.MyDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.PersonRepository;

public final class PeopleVerticle extends AbstractVerticle {

  private final Logger logger = LoggerFactory.getLogger(PeopleVerticle.class);
  private PersonRepository personRepository;

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);
    router.get("/api/libmanagement/people/person/:id").handler(this::getPerson);

    vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(
            config().getInteger("http.port", 8080),
            result -> {
              if (result.succeeded()) {
                startPromise.complete();
              } else {
                startPromise.fail(result.cause());
              }
            });
    this.personRepository = new PersonRepository(MyDatabase.getInstance(vertx).getMySQLPool());
  }

  private void getPerson(RoutingContext routingContext) {
    String personId = routingContext.request().getParam("id");

    personRepository
        .findById(personId)
        .onSuccess(
            person -> {
              if (person != null) {
                routingContext
                    .response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(Json.encodePrettily(person));
              } else {
                routingContext.response().setStatusCode(404).end();
              }
            })
        .onFailure(
            error -> {
              routingContext.response().setStatusCode(500).end("Internal Server Error");
            });
  }
}
