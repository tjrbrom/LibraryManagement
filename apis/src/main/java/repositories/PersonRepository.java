package repositories;

import entities.Person;
import io.vertx.core.Future;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import java.util.NoSuchElementException;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PersonRepository implements IPersonRepository {

  private static final String SELECT_QUERY = "SELECT id, name, email FROM people WHERE id = ?";
  private static final String INSERT_QUERY =
      "INSERT INTO people (id, name, email) VALUES (?, ?, ?)";
  private static final String DELETE_QUERY = "DELETE FROM people WHERE id = ?";

  private final Logger logger = LoggerFactory.getLogger(PersonRepository.class);
  private final MySQLPool mySQLPool;

  public PersonRepository(MySQLPool mySQLPool) {
    this.mySQLPool = mySQLPool;
  }

  public Future<Person> findById(String id) {
    Tuple params = Tuple.of(id);
    return executeQuery(SELECT_QUERY, params, this::mapRowToPerson);
  }

  public Future<Person> save(Person person) {
    Tuple params = Tuple.of(person.getId(), person.getName(), person.getEmail());
    return executeQuery(INSERT_QUERY, params, result -> handleInsertResult(result, person));
  }

  public Future<Void> deleteById(String id) {
    Tuple params = Tuple.of(id);
    return executeQuery(DELETE_QUERY, params, this::handleDeleteResult);
  }

  private <T> Future<T> executeQuery(
      String query, Tuple params, Function<RowSet<Row>, T> resultMapper) {
    return mySQLPool
        .preparedQuery(query)
        .execute(params)
        .map(resultMapper)
        .onFailure(this::handleFailure);
  }

  private Person mapRowToPerson(RowSet<Row> rowSet) {
    if (rowSet.rowCount() == 0) {
      return null;
    }
    Row row = rowSet.iterator().next();
    return new Person(row.getString("id"), row.getString("name"), row.getString("email"));
  }

  private void handleFailure(Throwable err) {
    logger.error("An error occurred in the repository", err);
  }

  private Void handleDeleteResult(RowSet<Row> rowSet) {
    if (rowSet.rowCount() == 1) {
      logger.info("Successfully deleted one row");
      return null;
    } else if (rowSet.rowCount() == 0) {
      throw new NoSuchElementException("No person found with the specified ID");
    } else {
      throw new IllegalStateException("Multiple rows deleted with the same ID");
    }
  }

  private Person handleInsertResult(RowSet<Row> rowSet, Person person) {
    if (rowSet.rowCount() != 1) {
      throw new IllegalStateException("Failed to insert the person");
    }
    logger.info("Successfully inserted one row");
    return person;
  }
}
