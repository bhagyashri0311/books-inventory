package com.jpmc.assessment.controller;

import com.jpmc.assessment.model.Book;
import com.jpmc.assessment.repository.BookRepository;
import com.jpmc.assessment.service.BookService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
@Tag(name = "Book API", description = "API for ")
public class BookController {

  private final BookRepository bookRepository;
  private final BookService bookService;

  public BookController(BookRepository bookRepository, BookService bookService) {
    this.bookRepository = bookRepository;
    this.bookService = bookService;
  }

  @GetMapping(value = "/isbn/{isbn}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Book.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
      @ApiResponse(responseCode = "404", description = "Resource or content not found")})
  public ResponseEntity<Book> getBookByIsbn(@PathVariable("isbn") long isbn) {
    Optional<Book> book = bookRepository.findById(isbn);
    return book.isPresent() ? ResponseEntity.ok(book.get()) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping(value = "/isbn/{isbn}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error")})
  public ResponseEntity<String> deleteIsbn(@PathVariable("isbn") long isbn) {
    bookRepository.deleteById(isbn);
    return ResponseEntity.ok("Book Deleted");
  }

  @PostMapping(value = "/book")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Book.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
      @ApiResponse(responseCode = "412", description = "Book with given ISBN already exists")})
  public ResponseEntity<Book> saveBook(@RequestBody Book book) {
    Optional<Book> existingBook = bookRepository.findById(book.getIsbn());
    return !existingBook.isPresent() ? ResponseEntity.ok(bookRepository.save(book)) : new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
  }

  @PutMapping(value = "/book")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Book.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
      @ApiResponse(responseCode = "404", description = "Resource to update does not exist")})
  public ResponseEntity<Book> updateBook(@RequestBody Book book) {
    Optional<Book> existingBook = bookRepository.findById(book.getIsbn());
    return existingBook.isPresent() ? ResponseEntity.ok(bookRepository.save(book)) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/author/{author}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Book.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
      @ApiResponse(responseCode = "404", description = "Resource or content not found")})
  public ResponseEntity<Book> getBookByAuthor(@PathVariable("author") String author) {
    Optional<Book> book = bookRepository.findByAuthor(author);
    return book.isPresent() ? ResponseEntity.ok(book.get()) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/title/{title}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = Book.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
      @ApiResponse(responseCode = "404", description = "Resource or content not found")})
  public ResponseEntity<Book> getBookByTitle(@PathVariable("title") String title) {
    Optional<Book> book = bookRepository.findByTitle(title);
    return book.isPresent() ? ResponseEntity.ok(book.get()) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/tag/{tag}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "success",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class)))),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
      @ApiResponse(responseCode = "404", description = "Resource or content not found")})
  public ResponseEntity<List<Book>> getBooksByTag(@PathVariable("tag") String tag) {
    Optional<List<Book>> book = bookRepository.findByTags(tag);
    return book.isPresent() ? ResponseEntity.ok(book.get()) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping(value = "/books")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class)))),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
      @ApiResponse(responseCode = "404", description = "Resource or content not found")})
  public ResponseEntity<List<Book>> getAllBooks() {
    Optional<List<Book>> books = Optional.ofNullable(bookRepository.findAll());
    return books.isPresent() && !books.get().isEmpty() ? ResponseEntity.ok(books.get()) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PostMapping(value = "/upload")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error")})
  public ResponseEntity<String> handleFileUpload(@RequestPart(required = true) MultipartFile file) throws IOException {
    try (InputStream inputStream = file.getInputStream();
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      bookService.upload(reader);
    }
    return ResponseEntity.ok("File " + file.getOriginalFilename() + " Uploaded!");
  }
}
