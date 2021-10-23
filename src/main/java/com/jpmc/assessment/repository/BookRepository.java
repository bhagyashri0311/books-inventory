package com.jpmc.assessment.repository;

import com.jpmc.assessment.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {
  List<Book> findAll();

  Optional<Book> findByAuthor(String author);

  Optional<Book> findByTitle(String title);

  Optional<List<Book>> findByTags(String tag);
}
