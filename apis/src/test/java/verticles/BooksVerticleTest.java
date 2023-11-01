package verticles;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class BooksVerticleTest {

  @BeforeAll
  static void deployVerticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(
        new BooksVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void testGetBook(Vertx vertx, VertxTestContext testContext) {
    WebClient webClient = WebClient.create(vertx);
    // Define the expected book data as JSON
    JsonObject expectedBook = new JsonObject().put("isbn", "12345").put("title", "Sample Book");

    webClient
        .get(8080, "localhost", "/api/libmanagement/books/book/12345")
        .send(
            testContext.succeeding(
                response -> {
                  assertEquals(200, response.statusCode());
                  assertEquals("application/json", response.getHeader("Content-Type"));
                  JsonObject actualBook = response.bodyAsJsonObject();
                  assertEquals(expectedBook, actualBook);
                  testContext.completeNow();
                }));
  }
}
