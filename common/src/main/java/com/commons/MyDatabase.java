package com.commons;

import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Connect to database and provide a sql pool utility. */
public final class MyDatabase {

  private static MyDatabase instance;
  private static MySQLPool mySQLPool;

  private MyDatabase(Vertx vertx) {
    Properties properties = new Properties();

    try (InputStream input =
        getClass().getClassLoader().getResourceAsStream("database.properties")) {

      properties.load(input);

      MySQLConnectOptions connectOptions =
          new MySQLConnectOptions()
              .setHost(properties.getProperty("database.host"))
              .setPort(Integer.parseInt(properties.getProperty("database.port")))
              .setDatabase(properties.getProperty("database.database"))
              .setUser(properties.getProperty("database.username"))
              .setPassword(properties.getProperty("database.password"));

      PoolOptions poolOptions =
          new PoolOptions().setMaxSize(10).setMaxWaitQueueSize(50).setIdleTimeout(30000);

      mySQLPool = MySQLPool.pool(vertx, connectOptions, poolOptions);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static MyDatabase getInstance(Vertx vertx) {
    if (instance == null) {
      instance = new MyDatabase(vertx);
    }
    return instance;
  }

  public MySQLPool getMySQLPool() {
    return mySQLPool;
  }
}
