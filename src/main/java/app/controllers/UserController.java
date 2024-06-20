package app.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.mappers.BookAuthorMapper;
import app.mappers.BookCategoryMapper;
import app.mappers.BookMapper;
import app.mappers.UserProfileMapper;
import app.model.Book;
import app.model.BookAuthor;
import app.model.BookCategory;
import app.model.Recommendations;
import app.model.Search;
import app.model.UserProfile;
import app.services.SearchFactory;
import app.services.SearchStrategy;
import app.services.UserProfileService;
import app.services.UserService;

@Controller
public class UserController {
	@Autowired
	UserService userService;
	
	@Autowired
	UserProfileService userProfileService;
	
	@Autowired
	BookMapper bookMapper;
	
	@Autowired
	BookAuthorMapper bookAuthorMapper;
	
	@Autowired
	BookCategoryMapper bookCategoryMapper;
	
	@Autowired
	UserProfileMapper userProfileMapper;
	
	
	@Autowired
    private SearchFactory searchFactory;
	
	@RequestMapping("/user/dashboard")
	public String getUserHome(){	
	    return "user/main-menu";
	}
	
	@RequestMapping("/profile")
	public String showProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfile userProfile = userProfileService.retrieveProfile(username);
        if (userProfile == null) {          
        	userProfile = new UserProfile(); 
            userProfile.setUsername(username);
            userProfileService.save(userProfile); 
        }   
        List<BookAuthor> allAuthors = bookAuthorMapper.findAll();
        List<BookCategory> allCategories = bookCategoryMapper.findAll();
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("allAuthors", allAuthors);
        model.addAttribute("allCategories", allCategories);           
        return "user/profile"; 
	}

	@PostMapping("/user/profile/save")
	public String saveProfile(@ModelAttribute("userProfile") UserProfile userProfile, BindingResult bindingResult, Model model,
	                          @RequestParam(required = false) String favouriteAuthors,
	                          @RequestParam(required = false) List<Integer> categoryIds) { 
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    if (userProfile.getUsername() == null || userProfile.getUsername().isEmpty()) {
	        userProfile.setUsername(username);
	    }
	    UserProfile existingProfile = userProfileService.retrieveProfile(userProfile.getUsername());
	    if (existingProfile != null) {
	        existingProfile.setFullName(userProfile.getFullName());
	        existingProfile.setAge(userProfile.getAge());
	        existingProfile.setPhoneNumber(userProfile.getPhoneNumber());
	        existingProfile.setAddress(userProfile.getAddress());
	        if (favouriteAuthors != null && !favouriteAuthors.trim().isEmpty()) {
	            String[] authorNames = favouriteAuthors.split(",");
	            List<BookAuthor> updatedAuthors = new ArrayList<>();
	            for (String name : authorNames) {
	                name = name.trim();
	                BookAuthor author = bookAuthorMapper.findOneByName(name);
	                if (author == null) {
	                    author = new BookAuthor();
	                    author.setName(name);
	                    bookAuthorMapper.save(author);
	                }
	                updatedAuthors.add(author);
	            }
	            existingProfile.setFavouriteBookAuthors(updatedAuthors);
	        } else {
	            existingProfile.setFavouriteBookAuthors(new ArrayList<>());
	        }
	        List<BookCategory> updatedCategories = new ArrayList<>();
	        if (categoryIds != null) {
	            for (Integer categoryId : categoryIds) {
	                bookCategoryMapper.findById(categoryId).ifPresent(updatedCategories::add);
	            }
	        }
	        existingProfile.setFavouriteBookCategories(updatedCategories);
	        userProfileService.save(existingProfile);
	    } else {
	        userProfileService.save(userProfile);
	    }
	    return "redirect:/user/main-menu";
	}

	@RequestMapping("/book/offers")
	public String showBookOffers(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    UserProfile userProfile = userProfileService.retrieveProfile(username);
	    if (userProfile == null) {
	        return "redirect:/user/error"; 
	    }
	    model.addAttribute("bookOffers", userProfile.getBookOffers()); 
	    return "user/book-offers"; 
	}
	
	@RequestMapping("/user/book-offers/new")
	public String addNewBookOffer(Model model) {
		 List<BookCategory> allCategories = bookCategoryMapper.findAll();
		 model.addAttribute("allCategories", allCategories);
		return "/user/add-book-offer";
	}
	
	@PostMapping("/user/book-offers/save")
    public String saveNewBookOffer(@RequestParam("title") String title,
                                   @RequestParam("author") String author,
                                   @RequestParam("categoryId") Integer categoryId, 
                                   Model model) {
        if (categoryId == null) {
            model.addAttribute("error", "Please select a category.");
            return "/user/add-book-offer"; 
        }

        Book book = new Book();
        book.setTitle(title);
        BookAuthor bookAuthor = bookAuthorMapper.findOneByName(author);
        if (bookAuthor == null) {
            bookAuthor = new BookAuthor();
            bookAuthor.setName(author);
            bookAuthorMapper.save(bookAuthor);
        }
        book.setAuthor(bookAuthor);
        BookCategory category = bookCategoryMapper.findById(categoryId).orElse(null);
        if (category != null) {
            book.setBookCategory(category);
        } else {
            model.addAttribute("error", "No category.");
            return "/user/add-book-offer"; 
        }

        bookMapper.save(book);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfile userProfile = userProfileService.retrieveProfile(username);
        if (userProfile != null) {
            userProfile.getBookOffers().add(book);
            userProfileService.save(userProfile);
        }
        return "redirect:/book/offers";
    } 

	@PostMapping("/user/book-offers/delete")
    public String deleteBookOffer(@RequestParam("bookId") Integer bookId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfile userProfile = userProfileService.retrieveProfile(username);
        Book book = bookMapper.findByBookId(bookId);
        if (userProfile != null && userProfile.getBookOffers() != null) {
            userProfileService.deleteBookOffers(userProfile.getUsername(), bookId);
            for (UserProfile up : userProfileMapper.findAll()) {
                if (up.getBookRequests().contains(book)) {        //removing the book from all requesting lists
                    userProfileService.deleteBookRequest(up.getUsername(), bookId);
                }
            }            
            bookMapper.delete(book);
            userProfileService.save(userProfile);
        }
        return "redirect:/book/offers";
    }

	@RequestMapping("/book/requests")
	public String showBookRequests(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    UserProfile userProfile = userProfileService.retrieveProfile(username);
	    if (userProfile == null) {	        
	        return "redirect:/user/test";
	    }
	    List<Book> offeredBooks = userProfile.getBookOffers();
	    List<List<String>> bookRequestDetails = new ArrayList<>();
	    for (Book book : offeredBooks) {
	        List<UserProfile> requestingUsers = book.getRequestingUsers();     
	        for (UserProfile requester : requestingUsers) {
	            if (!requester.getUsername().equals(username)) {
	                List<String> detail = new ArrayList<>();
	                detail.add(String.valueOf(book.getBookId()));
	                detail.add(book.getTitle());
	                detail.add(book.getAuthor() != null ? book.getAuthor().getName() : "Unknown Author");
	                detail.add(requester.getUsername());
	                bookRequestDetails.add(detail);
	            }
	        }
	    }
	    model.addAttribute("bookRequestDetails", bookRequestDetails);
	    return "user/book-requests";
	}
	
	@PostMapping("/user/book-requests/more-info")
	public String moreInfoBookRequest(@RequestParam("bookId") String bookId, @RequestParam("requesterUsername") String requesterUsername, Model model) {
		System.out.println("Received bookId: " + bookId);			   
	    UserProfile requesterProfile = userProfileService.retrieveProfile(requesterUsername);
	    if (requesterProfile == null) {
	        return "redirect:/user/book-requests";
	    }
	    model.addAttribute("requesterProfile", requesterProfile);
	    model.addAttribute("bookId", bookId);
	    return "user/requester-profile";
	}
	
	@PostMapping("/user/book-requests/accept")
	public String acceptBookRequest(@RequestParam("bookId") Integer bookId, Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    UserProfile userProfile = userProfileService.retrieveProfile(username);
	    Book book = bookMapper.findByBookId(bookId);
	    if (userProfile != null && userProfile.getBookRequests() != null) {
	        List<Book> updatedBookRequests = new ArrayList<>();
	        List<Book> updatedBookOffers = new ArrayList<>();
	        for (Book bookt : userProfile.getBookRequests()) {
	            if (bookt.getBookId() != bookId) {
	            	updatedBookRequests.add(bookt);	            	
	            }
	        }
	        for (Book bookt : userProfile.getBookOffers()) {
	            if (bookt.getBookId() != bookId) {
	            	 updatedBookOffers.add(bookt);
	            }       
	        }
	        for (UserProfile up : userProfileMapper.findAll()) {
	        	if(up.getBookRequests().contains(book)) {		//removing the book from all requesting lists
	        		userProfileService.deleteBookRequest(up.getUsername(), bookId);
	        	}
	        }
	        userProfile.setBookRequests(updatedBookRequests);
	        userProfile.setBookOffers(updatedBookOffers);
	        userProfileService.save(userProfile);
	    }
	    return "user/book-requests";
	}

	@RequestMapping("/search")
	public String showSearchForm(Model model) {
	    Search search = new Search();
	    model.addAttribute("search", search); 
	    return "user/search-form";
	}
	
	@PostMapping("/user/search")
    public String performSearch(@ModelAttribute("search") Search search, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfile userProfile = userProfileService.retrieveProfile(username);

        SearchStrategy strategy = searchFactory.getStrategy(search);
        List<Book> searchResults = strategy.search(search, bookMapper);
        List<Book> finalResults = new ArrayList<>();
        List<UserProfile> allUsers = userProfileMapper.findAll();

        for (Book book : searchResults) {
            for (UserProfile user : allUsers) {
                if (!user.equals(userProfile) && user.getBookOffers().contains(book) && !userProfile.getBookRequests().contains(book)) {
                    finalResults.add(book);
                }
            }
        }

        model.addAttribute("search", search);
        model.addAttribute("results", finalResults);
        return "user/search-results";
    }
	
	@RequestMapping("/user/my-requests")
	public String showMyRequests(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); 
        UserProfile userProfile = userProfileService.retrieveProfile(username); 
        List<Book> bookRequests = userProfile.getBookRequests();
        model.addAttribute("bookRequests", bookRequests);
	    return "user/my-requests";
	}
	
	@PostMapping("/user/book-requests/request")
	public String requestBook(@RequestParam("bookId") int bookId, Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    UserProfile userProfile = userProfileService.retrieveProfile(username);
	    if (userProfile != null) {
	        Book book = bookMapper.findByBookId(bookId);
	        if (book != null) {        	
	        	userProfileService.requestBook(book.getBookId(), userProfile.getUsername());
        		userProfileService.save(userProfile);               
                model.addAttribute("message", "Book request submitted successfully!");                        
	        }
	    }
	    return "redirect:/search";
	}

	@PostMapping("/user/book-requests/cancel")
	public String cancelBookRequest(@RequestParam("bookId") int bookId, Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName(); 
	    UserProfile userProfile = userProfileService.retrieveProfile(username);

	    if (userProfile != null && userProfile.getBookRequests() != null) {
	        userProfileService.deleteBookRequest(userProfile.getUsername(), bookId);
	        userProfileService.save(userProfile);	        
	    }
	    return "redirect:/user/my-requests"; 
	}
	
	@RequestMapping("/user/show-recommendations")
    public String showRecommendations(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Recommendations recommendations = userProfileService.getRecommendations(username);
        model.addAttribute("recommendedBooks", recommendations.getRecomendedBooks());
        return "user/recommendations-results";
    }
}




