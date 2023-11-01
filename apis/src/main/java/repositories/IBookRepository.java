package repositories;

import entities.Book;
import io.vertx.core.Future;

public interface IBookRepository {
  Future<Book> findById(String id);

  Future<Book> save(Book book);

  Future<Void> deleteById(String id);
}
