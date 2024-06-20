package app.services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import app.mappers.BookMapper;
import app.model.Book;
import app.model.Search;

public abstract class TemplateSearchStrategy implements SearchStrategy{
	
	@Autowired
    protected BookMapper bookMapper;

    public abstract List<Book> search(Search search, BookMapper bookMapper);
    protected abstract List<Book> makeInitialListOfBooks(Search search);
    protected abstract boolean checkIfAuthorsMatch(Search search, Book book);

	
}

