package com.jpmc.assessment.service;

import com.jpmc.assessment.model.Book;
import com.jpmc.assessment.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
  private final BookRepository bookRepository;

  @Autowired
  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public void upload(BufferedReader reader) {
    List<String> lines = reader.lines().collect(Collectors.toList());
    lines.remove(0);
    lines.stream()
        .map(line -> processLine(line))
        .forEach(bookRepository::save);
  }
  private Book processLine(final String line) {
    var rowData = Arrays.asList(line.split(","));
    var book = new Book();
    book.setIsbn(Long.parseLong(rowData.get(0)));
    book.setTitle(rowData.get(1));
    book.setAuthor(rowData.get(2));
    book.setTags(rowData.get(3).contains("|") ? Arrays.asList(rowData.get(3).split("\\|")) : Arrays.asList(rowData.get(3)));
    return book;
  }
}
