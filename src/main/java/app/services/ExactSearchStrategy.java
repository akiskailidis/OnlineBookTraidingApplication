package app.services;

import app.mappers.BookMapper;
import app.mappers.BookAuthorMapper;
import app.model.Book;
import app.model.BookAuthor;
import app.model.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExactSearchStrategy extends TemplateSearchStrategy {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookAuthorMapper bookAuthorMapper;

    @Override
    public List<Book> search(Search search, BookMapper bookMapper) {
        List<Book> books = makeInitialListOfBooks(search);
        return books;
    }

    @Override
    protected List<Book> makeInitialListOfBooks(Search search) {
        List<Book> initialListOfBooks = new ArrayList<>();

        List<Book> foundBooks = bookMapper.findByTitle(search.getSearchString());
        if (foundBooks.isEmpty()) {
            List<BookAuthor> authors = bookAuthorMapper.findByName(search.getSearchString());
            for (BookAuthor author : authors) {
                foundBooks.addAll(bookMapper.findByAuthor(author));
            }
        }

        if (foundBooks.isEmpty()) {
            search.setMessage("No books found. Try refining your search criteria.");
            return initialListOfBooks;
        }

        initialListOfBooks.addAll(foundBooks);
        return initialListOfBooks;
    }

    @Override
    protected boolean checkIfAuthorsMatch(Search search, Book book) {
        return book.getAuthor().getName().equals(search.getSearchString());
    }
}
