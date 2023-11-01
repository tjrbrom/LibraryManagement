package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Person {
  private final List<Book> lentBooks;
  private String id;
  private String name;
  private String email;

  public Person(String id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.lentBooks = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Book> getLentBooks() {
    return lentBooks;
  }

  public void lendBook(Book book) {
    lentBooks.add(book);
  }

  public void returnBook(Book book) {
    lentBooks.remove(book);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(id, person.id)
        && Objects.equals(name, person.name)
        && Objects.equals(email, person.email)
        && Objects.equals(lentBooks, person.lentBooks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, email, lentBooks);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("name", name)
        .append("email", email)
        .append("lentBooks", lentBooks)
        .toString();
  }
}
