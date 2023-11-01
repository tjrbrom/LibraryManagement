package verticles;

import com.commons.MyDatabase;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repositories.BookRepository;

public final class BooksVerticle extends AbstractVerticle {

  private final Logger logger = LoggerFactory.getLogger(BooksVerticle.class);
  private BookRepository bookRepository;

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);
    router.get("/api/libmanagement/books/book/:isbn").handler(this::getBook);

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
    this.bookRepository = new BookRepository(MyDatabase.getInstance(vertx).getMySQLPool());
  }

  private void getBook(RoutingContext routingContext) {
    String isbn = routingContext.request().getParam("isbn");

    bookRepository
        .findById(isbn)
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
