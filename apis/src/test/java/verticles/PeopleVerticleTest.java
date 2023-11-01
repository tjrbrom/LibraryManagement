package verticles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class PeopleVerticleTest {

  @BeforeAll
  static void deployVerticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(
        new PeopleVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void testGetPerson(Vertx vertx, VertxTestContext testContext) {
    WebClient webClient = WebClient.create(vertx);

    JsonObject expectedPerson = new JsonObject().put("id", "12345").put("name", "a name");

    webClient
        .get(8080, "localhost", "/api/libmanagement/people/person" + "/12345")
        .send(
            testContext.succeeding(
                response -> {
                  assertEquals(200, response.statusCode());
                  assertEquals("application/json", response.getHeader("Content-Type"));
                  JsonObject actualPerson = response.bodyAsJsonObject();
                  assertEquals(expectedPerson, actualPerson);
                  testContext.completeNow();
                }));
  }
}
