package com.example.storyloom_catalog_service.controller;

import com.example.storyloom_catalog_service.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private BooksService booksService;



//    @GetMapping("/getBooks")
//    public ResponseEntity<?> getBooks(@RequestBody List<Long> ids){
//        try{
//
//            return ResponseEntity.ok(booksService.getBooks(ids));
//
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
//        }
//    }

//    @PostMapping()
//    public ResponseEntity<?> addBooks(@RequestBody List<String> bookTitles){
//        try{
//
//            return ResponseEntity.ok(booksService.addBooks(bookTitles));
//
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
//        }
//    }

    @GetMapping("/getBook")
    public ResponseEntity<?> getBook(@RequestBody String bookTitle){
        try{

            return ResponseEntity.ok(booksService.getBook(bookTitle));

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
        }
    }

    @GetMapping("/getBooks")
    public ResponseEntity<?> getBooks(@RequestBody List<String> bookTitles){
        try{

            return ResponseEntity.ok(booksService.getBooks(bookTitles));

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
        }
    }



}
