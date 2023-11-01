package repositories;

import entities.Book;
import io.vertx.core.Future;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BookRepository implements IBookRepository {

  private static final String SELECT_QUERY = "SELECT isbn, title, author FROM books WHERE isbn = ?";
  private static final String INSERT_QUERY =
      "INSERT INTO books (isbn, title, author) VALUES (?, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM books WHERE isbn = ?";

  private final Logger logger = LoggerFactory.getLogger(BookRepository.class);
  private final MySQLPool mySQLPool;

  public BookRepository(MySQLPool mySQLPool) {
    this.mySQLPool = mySQLPool;
  }

  @Override
  public Future<Book> findById(String id) {
    if (id == null) {
      return Future.failedFuture(new IllegalArgumentException("isbn cannot be" + " null"));
    }

    Tuple params = Tuple.of(id);
    return executeQuery(SELECT_QUERY, params, this::mapRowToBook);
  }

  @Override
  public Future<Book> save(Book book) {
    if (book == null) {
      return Future.failedFuture(new IllegalArgumentException("Book cannot be null"));
    }

    Tuple params = Tuple.of(book.getIsbn(), book.getTitle(), book.getAuthor());
    return executeQuery(INSERT_QUERY, params, result -> handleInsertResult(result, book));
  }

  @Override
  public Future<Void> deleteById(String isbn) {
    if (isbn == null) {
      return Future.failedFuture(new IllegalArgumentException("isbn cannot be" + " null"));
    }

    Tuple params = Tuple.of(isbn);
    return executeQuery(DELETE_QUERY, params, this::handleDeleteResult);
  }

  private Book mapRowToBook(RowSet<Row> rowSet) {
    if (rowSet.rowCount() == 0) {
      return null;
    }
    Row row = rowSet.iterator().next();
    return new Book(row.getString("isbn"), row.getString("title"), row.getString("author"));
  }

  private <T> Future<T> executeQuery(
      String query, Tuple params, Function<RowSet<Row>, T> resultMapper) {
    return mySQLPool
        .preparedQuery(query)
        .execute(params)
        .map(resultMapper)
        .onFailure(this::handleFailure);
  }

  private void handleFailure(Throwable err) {
    logger.error("An error occurred in the repository", err);
  }

  private Void handleDeleteResult(RowSet<Row> rowSet) {
    if (rowSet.rowCount() == 1) {
      logger.info("Successfully deleted one row");
      return null;
    } else if (rowSet.rowCount() == 0) {
      throw new NoSuchElementException("No book found with the specified isbn");
    } else {
      throw new IllegalStateException("Multiple rows deleted with the same isbn");
    }
  }

  private Book handleInsertResult(RowSet<Row> rowSet, Book book) {
    if (rowSet.rowCount() != 1) {
      throw new IllegalStateException("Failed to insert the book");
    }
    logger.info("Successfully inserted one row");
    return book;
  }
}
