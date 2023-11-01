package repositories;

import entities.Person;
import io.vertx.core.Future;

public interface IPersonRepository {
  Future<Person> findById(String id);

  Future<Person> save(Person person);

  Future<Void> deleteById(String id);
}
