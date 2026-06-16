package com.example.storyloom_catalog_service.service;

import com.example.storyloom_catalog_service.OpenLibraryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksService {

    @Autowired
    private OpenLibraryClient openLibraryClient;

    public ResponseEntity<?> getBook(String bookTitle) {
        return ResponseEntity.ok(openLibraryClient.searchBook(bookTitle));
    }


//    public Object getBooks(List<Long> ids) {
//
//
//    }


}
