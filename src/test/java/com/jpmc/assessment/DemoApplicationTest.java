package com.jpmc.assessment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.assessment.model.Book;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
class DemoApplicationTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  void getAllBooks() throws Exception {
    MvcResult result = mockMvc.perform(get("/api/books"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
    List<Book> books = Arrays.asList(new ObjectMapper().readValue(result.getResponse().getContentAsString(), Book[].class));
    assertFalse(books.isEmpty());
    assertTrue(3 >= books.size());
  }

  @Test
  void getByIsbn() throws Exception {
    MvcResult result = mockMvc.perform(get("/api/isbn/1234567890"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
    Book book = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Book.class);
    assertEquals(1234567890l, book.getIsbn());
  }

  @Test
  void deleteBook() throws Exception {
    MvcResult result = mockMvc.perform(delete("/api/isbn/3213213210"))
        .andExpect(status().isOk()).andReturn();
    assertEquals("Book Deleted", result.getResponse().getContentAsString());
  }

  @Test
  void getByAuthor() throws Exception {
    MvcResult result = mockMvc.perform(get("/api/author/Erica Jong"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
    Book book = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Book.class);
    assertEquals(1234567890l, book.getIsbn());
  }

  @Test
  void getByTags() throws Exception {
    MvcResult result = mockMvc.perform(get("/api/tag/history"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
    List<Book> books = Arrays.asList(new ObjectMapper().readValue(result.getResponse().getContentAsString(), Book[].class));
    assertFalse(books.isEmpty());
  }

  @Test
  void updateBook() throws Exception {
    MvcResult result = mockMvc.perform(get("/api/isbn/1234567890"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
    Book book = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Book.class);
    Assert.assertEquals("Fear of Flying", book.getTitle());
    book.setTitle("Fear of Flying-v2");
    String content = (new ObjectMapper()).writeValueAsString(book);
    MvcResult resultUpdate = mockMvc.perform(put("/api/book").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
    Book bookUpdated = new ObjectMapper().readValue(resultUpdate.getResponse().getContentAsString(), Book.class);
    Assert.assertEquals("Fear of Flying-v2", bookUpdated.getTitle());
  }

  @Test
  void createBook() throws Exception {
    Book book = new Book();
    book.setIsbn(98989898989l);
    book.setTitle("New BOOK");
    book.setAuthor("Bhagya");
    book.setTags(Arrays.asList("test"));
    String content = (new ObjectMapper()).writeValueAsString(book);
    MvcResult resultUpdate = mockMvc.perform(post("/api/book").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
    Book bookUpdated = new ObjectMapper().readValue(resultUpdate.getResponse().getContentAsString(), Book.class);
    Assert.assertEquals("New BOOK", bookUpdated.getTitle());
  }

}
