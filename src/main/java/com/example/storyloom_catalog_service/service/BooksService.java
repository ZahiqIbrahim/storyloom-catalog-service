package com.example.storyloom_catalog_service.service;

import com.example.storyloom_catalog_service.OpenLibraryClient;
import com.example.storyloom_catalog_service.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class BooksService {

    @Autowired
    private OpenLibraryClient openLibraryClient;


    public Book getBook(String bookTitle) {
        Book book = new Book();

        Map<String, Object> response = openLibraryClient.searchBook(bookTitle , 1);

        List<Map<String, Object>> docs =  (List<Map<String, Object>>) response.get("docs");

        if (docs.isEmpty()) {
            throw new RuntimeException("Book not found");
        }

        Map<String, Object> firstBook = docs.get(0);

        book.setAuthorName((List<String>) firstBook.get("author_name"));
        book.setKey((String) firstBook.get("key"));

        book.setSubtitle((String) firstBook.get("subtitle"));
        book.setTitle((String) firstBook.get("title"));
        book.setFirstPublishYear(String.valueOf( firstBook.get("first_publish_year")));

        String coverId = String.valueOf(firstBook.get("cover_i"));

        if(coverId != null){
            book.setCover(  "https://covers.openlibrary.org/b/id/"
                            +  coverId
                            + "-L.jpg");
        }

        return book;
    }

    public List<Book> getBooks(List<String> bookTitles) {

        List<Book> books = new ArrayList<>();
        for(String bookTitle : bookTitles){
            Book book = getBook(bookTitle);
            books.add(book);
        }
        return books;
    }


//    public Object getBooks(List<Long> ids) {
//
//
//    }


}
