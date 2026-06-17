package com.example.storyloom_catalog_service.service;

import com.example.storyloom_catalog_service.external_clients.OpenLibraryClient;
import com.example.storyloom_catalog_service.model.Book;
import com.example.storyloom_catalog_service.repo.BooksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class BooksService {

    @Autowired
    private OpenLibraryClient openLibraryClient;

    @Autowired
    private BooksRepo booksRepo;


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


    public void getAndStoreTrendingBooks() {

        booksRepo.deleteAll();

        Map<String, Object> response = openLibraryClient.getTrendingBooks();


        List<Map<String, Object>> works =  (List<Map<String, Object>>) response.get("works");

        if (works == null || works.isEmpty()) {
            throw new RuntimeException("Books not found");
        }
        List<Book> books = new ArrayList<>();

        for(Map<String, Object> doc : works){

            Book book = new Book();
            book.setAuthorName((List<String>) doc.get("author_name"));
            book.setKey((String) doc.get("key"));

            book.setSubtitle((String) doc.get("subtitle"));
            book.setTitle((String) doc.get("title"));
            book.setFirstPublishYear(String.valueOf( doc.get("first_publish_year")));

            String coverId = String.valueOf(doc.get("cover_i"));

            if(coverId != null){
                book.setCover(  "https://covers.openlibrary.org/b/id/"
                        +  coverId
                        + "-L.jpg");
            }
            books.add(book);
        }

        booksRepo.saveAll(books);

    }


    public List<Book> getTrendingBooks() {

        return booksRepo.findAll();
    }
}
