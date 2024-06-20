package app.services;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.mappers.*;
import app.model.Book;
import app.model.Recommendations;
import app.model.Search;
import app.model.UserProfile;


@Service
public class UserProfileServiceImpl implements UserProfileService {

	
	@Autowired
	private UserProfileMapper userProfileMapper;
	
	@Autowired
	private BookMapper bookMapper;
	
	@Autowired
	private RecommendationsFactory recommendationsFactory;
	
	
	@Override
	public UserProfile retrieveProfile(String username) {		
		UserProfile userProfile = userProfileMapper.findByUsername(username);
		if (userProfile == null) {
            throw new IllegalArgumentException("User not found");
        }
        return userProfile;
	}

	@Override
	@Transactional
	public void save(UserProfile userProfile) {
		userProfileMapper.save(userProfile);
	}

	@Override
	public List<Book> retrieveBookOffers(String username) {
		UserProfile userProfile = userProfileMapper.findByUsername(username);
		if (userProfile.getBookOffers() == null) {
            throw new IllegalArgumentException("No Offers found");
        }
		return userProfile.getBookOffers();
	}

	@Override
	public void addBookOffer(String username,Book book) {
		UserProfile userProfile = userProfileMapper.findByUsername(username);
		userProfile.getBookOffers().add(book);	
		bookMapper.save(book);       
	}

	@Override
	public List<Book> searchBooks(Search search) {
		List <Book> bookList = bookMapper.findByTitle(search.getSearchString());
		if (bookList == null || bookList.isEmpty()) {
            throw new IllegalArgumentException("No books with this title found");	
		}
		return bookList;
	}

	@Override
	public List<Book> recommendBooks(String username) {
		UserProfile userProfile  = userProfileMapper.findByUsername(username);
		RecommendationsStrategy strategy = recommendationsFactory.getStrategy(userProfile);
		List<Book> recommendedBooks = strategy.recommendBooks(userProfile);
		return recommendedBooks;
	}

	@Override
	public void requestBook(int bookId, String username) {
		UserProfile userProfile = userProfileMapper.findByUsername(username);
		Book book = bookMapper.findById(bookId).get();  
		if (!userProfile.getBookRequests().contains(book)) {
			userProfile.getBookRequests().add(book);
			book.getRequestingUsers().add(userProfile);
		}
		
	}

	@Override
	public List<Book> retrieveBookRequests(String username) {
		UserProfile userProfile = userProfileMapper.findByUsername(username);		
		if (userProfile.getBookRequests() == null) {
            throw new IllegalArgumentException("No requests found");
        }
		return userProfile.getBookRequests();
	}

	@Override
	public List<UserProfile> retrieveRequestingUsers(int bookId) {
		Book book = bookMapper.findByBookId(bookId);
		if (book.getRequestingUsers() == null) {
            throw new IllegalArgumentException("No requestsing Users found");
        }
		return book.getRequestingUsers();
	}

	@Override
	public void deleteBookOffers(String username, int bookId) {
		UserProfile userProfile = userProfileMapper.findByUsername(username);
		Book book = bookMapper.findByBookId(bookId);
		if (userProfile.getBookOffers().contains(book)) {
			userProfile.getBookOffers().remove(book);
			userProfileMapper.save(userProfile);
        }
		else{
			throw new IllegalArgumentException("Book not found in users book offers");
		}
	}

	@Override
	public void deleteBookRequest(String username, int bookId) {
	    UserProfile userProfile = userProfileMapper.findByUsername(username);
	    Book book = bookMapper.findByBookId(bookId);
	    if (userProfile.getBookRequests().contains(book)) {
	        userProfile.getBookRequests().remove(book);
	        if (book.getRequestingUsers() == null) {
	            book.setRequestingUsers(new ArrayList<>());
	        }
	        book.getRequestingUsers().remove(userProfile); // Remove user from book's requesting users list
	        userProfileMapper.save(userProfile); // Save the updated user profile
	        bookMapper.save(book); // Save the updated book
	    } else {
	        throw new IllegalArgumentException("Book not found in user's book requests");
	    }
	}

	
	public Recommendations getRecommendations(String username) {
        // Fetch user profile
        UserProfile userProfile = userProfileMapper.findByUsername(username);

        if (userProfile == null) {
            throw new RuntimeException("User not found");
        }

        
        List<Book> booksByFavoriteAuthors = bookMapper.findByAuthorIn(userProfile.getFavouriteBookAuthors());
        List<Book> booksByFavoriteCategories = bookMapper.findByBookCategoryIn(userProfile.getFavouriteBookCategories());

     // Exclude books that are currently in the user's book offers
        List<Book> currentBookOffers = userProfile.getBookOffers();

        List<Book> recommendedBooks = booksByFavoriteAuthors.stream()
                .filter(book -> !currentBookOffers.contains(book) && !userProfile.getBookRequests().contains(book))
                .collect(Collectors.toList());

        recommendedBooks.addAll(booksByFavoriteCategories.stream()
                .filter(book -> !recommendedBooks.contains(book))
                .filter(book -> !currentBookOffers.contains(book) && !userProfile.getBookRequests().contains(book))
                .collect(Collectors.toList()));

        Recommendations recommendations = new Recommendations();
        recommendations.setRecomendedBooks(recommendedBooks);

        return recommendations;
    }
	
}