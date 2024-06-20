package app.services;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import app.model.Book;
import app.model.BookCategory;
import app.model.UserProfile;

@Service
public class CategoryRecommendationStrategy implements RecommendationsStrategy {

    @Override
    public List<Book> recommendBooks(UserProfile userProfile) {
    	List<Book> recommendedBookList = new ArrayList<Book>();
        for(BookCategory bookCategory:userProfile.getFavouriteBookCategories()) {
        	if(!bookCategory.getBooks().isEmpty()) {
	        	for(Book book:bookCategory.getBooks()){
	        		recommendedBookList.add(book);
	        	}
        	}else {
        		throw new IllegalArgumentException("Book category has no books");
        	}
        }
        return recommendedBookList;
    }
}
