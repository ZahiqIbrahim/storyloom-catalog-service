package com.example.storyloom_catalog_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "Movies")
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     private String title;

     @Column(columnDefinition = "TEXT")
     private String overview ;    //"In his second year of fighting crime, Batman uncovers corruption in Gotham City that connects to his own family while facing a serial killer known as the Riddler.",

     private String posterPath;   //"/74xTEgt7R36Fpooo50r9T25onhq.jpg"

     private String releaseDate; //"2022-03-01",

     private String voteAverage; // 7.661,
}
