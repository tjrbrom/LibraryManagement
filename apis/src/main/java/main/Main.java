package main;

import io.vertx.core.Vertx;
import verticles.BooksVerticle;
import verticles.PeopleVerticle;

public class Main {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(new PeopleVerticle());
    vertx.deployVerticle(new BooksVerticle());
  }
}
