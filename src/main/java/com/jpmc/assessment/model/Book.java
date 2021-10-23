package com.jpmc.assessment.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Book {
  @Id
  @Column(name = "ID", unique = true, nullable = false)
  private Long isbn;
  @Column
  private String author;
  @Column
  private String title;
  @ElementCollection
  @CollectionTable(name="Tags", joinColumns=@JoinColumn(name="isbn"))
  @Column
  private List<String> tags;
}
