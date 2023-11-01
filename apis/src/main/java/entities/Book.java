package entities;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public class Book {
  private String isbn;
  private String title;
  private String author;

  public Book(String isbn, String title, String author) {
    this.isbn = isbn;
    this.title = title;
    this.author = author;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Book book = (Book) o;
    return Objects.equals(isbn, book.isbn)
        && Objects.equals(title, book.title)
        && Objects.equals(author, book.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isbn, title, author);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("isbn", isbn)
        .append("title", title)
        .append("author", author)
        .toString();
  }
}
