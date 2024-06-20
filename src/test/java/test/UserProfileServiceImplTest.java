package test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import app.mappers.BookMapper;
import app.mappers.UserProfileMapper;
import app.model.Book;
import app.model.Recommendations;
import app.model.Search;
import app.model.UserProfile;
import app.services.RecommendationsFactory;
import app.services.RecommendationsStrategy;
import app.services.UserProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserProfileServiceImplTest {

    @Mock
    private UserProfileMapper userProfileMapper;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private RecommendationsFactory recommendationsFactory;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveProfile() {
        String username = "testUser";
        UserProfile userProfile = new UserProfile();
        when(userProfileMapper.findByUsername(username)).thenReturn(userProfile);

        UserProfile retrievedProfile = userProfileService.retrieveProfile(username);

        assertNotNull(retrievedProfile);
        assertEquals(userProfile, retrievedProfile);
    }

    @Test
    public void testRetrieveProfile_UserNotFound() {
        String username = "testUser";
        when(userProfileMapper.findByUsername(username)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userProfileService.retrieveProfile(username));
    }

    @Test
    public void testSave() {
        UserProfile userProfile = new UserProfile();
        userProfileService.save(userProfile);

        verify(userProfileMapper, times(1)).save(userProfile);
    }

    @Test
    public void testRetrieveBookOffers() {
        String username = "testUser";
        UserProfile userProfile = new UserProfile();
        List<Book> bookOffers = new ArrayList<>();
        userProfile.setBookOffers(bookOffers);
        when(userProfileMapper.findByUsername(username)).thenReturn(userProfile);

        List<Book> retrievedBookOffers = userProfileService.retrieveBookOffers(username);

        assertEquals(bookOffers, retrievedBookOffers);
    }

    @Test
    public void testRetrieveBookOffers_NoOffers() {
        String username = "testUser";
        UserProfile userProfile = new UserProfile();
        userProfile.setBookOffers(null);
        when(userProfileMapper.findByUsername(username)).thenReturn(userProfile);

        assertThrows(IllegalArgumentException.class, () -> userProfileService.retrieveBookOffers(username));
    }

    @Test
    public void testAddBookOffer() {
        String username = "testUser";
        UserProfile userProfile = new UserProfile();
        List<Book> bookOffers = new ArrayList<>();
        userProfile.setBookOffers(bookOffers);
        Book book = new Book();
        when(userProfileMapper.findByUsername(username)).thenReturn(userProfile);

        userProfileService.addBookOffer(username, book);

        assertTrue(userProfile.getBookOffers().contains(book));
        verify(bookMapper, times(1)).save(book);
    }

    @Test
    public void testSearchBooks() {
        String searchString = "testTitle";
        Search search = new Search();
        search.setSearchString(searchString);
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book()); // Add a book to the list to avoid empty list exception

        when(bookMapper.findByTitle(searchString)).thenReturn(bookList);

        List<Book> result = userProfileService.searchBooks(search);

        assertNotNull(result);
        assertEquals(bookList, result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testSearchBooks_NoBooksFound() {
        String searchString = "testTitle";
        Search search = new Search();
        search.setSearchString(searchString);

        when(bookMapper.findByTitle(searchString)).thenReturn(new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> userProfileService.searchBooks(search));
    }

    @Test
    public void testRecommendBooks() {
        String username = "testUser";
        UserProfile userProfile = new UserProfile();
        RecommendationsStrategy strategy = mock(RecommendationsStrategy.class);
        List<Book> recommendedBooks = new ArrayList<>();
        when(userProfileMapper.findByUsername(username)).thenReturn(userProfile);
        when(recommendationsFactory.getStrategy(userProfile)).thenReturn(strategy);
        when(strategy.recommendBooks(userProfile)).thenReturn(recommendedBooks);

        List<Book> result = userProfileService.recommendBooks(username);

        assertEquals(recommendedBooks, result);
    }

    @Test
    public void testRequestBook() {
        String username = "testUser";
        int bookId = 1;
        UserProfile userProfile = new UserProfile();
        List<Book> bookRequests = new ArrayList<>();
        userProfile.setBookRequests(bookRequests);
        Book book = new Book();
        List<UserProfile> requestingUsers = new ArrayList<>();
        book.setRequestingUsers(requestingUsers);
        when(userProfileMapper.findByUsername(username)).thenReturn(userProfile);
        when(bookMapper.findById(bookId)).thenReturn(Optional.of(book));

        userProfileService.requestBook(bookId, username);

        assertTrue(userProfile.getBookRequests().contains(book));
        assertTrue(book.getRequestingUsers().contains(userProfile));
    }

    @Test
    public void testRetrieveBookRequests() {
        String username = "testUser";
        UserProfile userProfile = new UserProfile();
        List<Book> bookRequests = new ArrayList<>();
        userProfile.setBookRequests(bookRequests);
        when(userProfileMapper.findByUsername(username)).thenReturn(userProfile);

        List<Book> retrievedBookRequests = userProfileService.retrieveBookRequests(username);

        assertEquals(bookRequests, retrievedBookRequests);
    }

    @Test
    public void testRetrieveBookRequests_NoRequests() {
        String username = "testUser";
        UserProfile userProfile = new UserProfile();
        userProfile.setBookRequests(null);
        when(userProfileMapper.findByUsername(username)).thenReturn(userProfile);

        assertThrows(IllegalArgumentException.class, () -> userProfileService.retrieveBookRequests(username));
    }

    @Test
    public void testRetrieveRequestingUsers() {
        int bookId = 1;
        Book book = new Book();
        List<UserProfile> requestingUsers = new ArrayList<>();
        book.setRequestingUsers(requestingUsers);
        when(bookMapper.findByBookId(bookId)).thenReturn(book);

        List<UserProfile> result = userProfileService.retrieveRequestingUsers(bookId);

        assertEquals(requestingUsers, result);
    }

    @Test
    public void testRetrieveRequestingUsers_NoRequestingUsers() {
        int bookId = 1;
        Book book = new Book();
        book.setRequestingUsers(null);
        when(bookMapper.findByBookId(bookId)).thenReturn(book);

        assertThrows(IllegalArgumentException.class, () -> userProfileService.retrieveRequestingUsers(bookId));
    }

    @Test
    public void testDeleteBookOffers() {
        String username = "testUser";
        int bookId = 1;

        // Create a UserProfile and Book, set their properties
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(username);
        Book book = new Book();
        book.setBookId(bookId);

        // Add the book to the user's book offers
        List<Book> bookOffers = new ArrayList<>();
        bookOffers.add(book);
        userProfile.setBookOffers(bookOffers);

        // Mock the behavior of the mappers
        when(userProfileMapper.findByUsername(username)).thenReturn(userProfile);
        when(bookMapper.findByBookId(bookId)).thenReturn(book);

        // Call the method to be tested
        userProfileService.deleteBookOffers(username, bookId);

        // Verify that the book was removed from the user's book offers
        assertFalse(userProfile.getBookOffers().contains(book));
        // Verify that the save method was called on the userProfileMapper
        verify(userProfileMapper, times(1)).save(userProfile);
    }



    @Test
    public void testDeleteBookRequest() {
        String username = "testUser";
        int bookId = 1;

        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(username);
        userProfile.setBookRequests(new ArrayList<>()); // Ensure the list is initialized
        Book book = new Book();
        book.setBookId(bookId);
        book.setRequestingUsers(new ArrayList<>()); // Ensure the list is initialized

        userProfile.getBookRequests().add(book);
        book.getRequestingUsers().add(userProfile);

        when(userProfileMapper.findByUsername(username)).thenReturn(userProfile);
        when(bookMapper.findByBookId(bookId)).thenReturn(book);

        userProfileService.deleteBookRequest(username, bookId);

        assertFalse(userProfile.getBookRequests().contains(book));
        assertFalse(book.getRequestingUsers().contains(userProfile));
        verify(userProfileMapper, times(1)).save(userProfile);
        verify(bookMapper, times(1)).save(book);
    }


    @Test
    public void testGetRecommendations() {
        String username = "testUser";
        UserProfile userProfile = new UserProfile();
        List<Book> favoriteAuthorsBooks = new ArrayList<>();
        List<Book> favoriteCategoriesBooks = new ArrayList<>();
        when(userProfileMapper.findByUsername(username)).thenReturn(userProfile);
        when(bookMapper.findByAuthorIn(userProfile.getFavouriteBookAuthors())).thenReturn(favoriteAuthorsBooks);
        when(bookMapper.findByBookCategoryIn(userProfile.getFavouriteBookCategories())).thenReturn(favoriteCategoriesBooks);

        Recommendations recommendations = userProfileService.getRecommendations(username);

        assertNotNull(recommendations);
        
    }
}
