package com.example.storyloom_catalog_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.lang.reflect.Type;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String authorKey; //    "OL2653686A"

   private String authorName; // "Robert C. Martin"

    //"cover_i": 8065615,
   private String firstPublishYear; // : 2008,

   @Column(columnDefinition = "TEXT")
   private String subtitle; //: "A Handbook of Agile Software Craftsmanship",

   private String title; // : "Clean Code"

   private String key; // : "/works/OL17618370W",

   private String cover; // https://covers.openlibrary.org/b/id/8065615-L.jpg

}
