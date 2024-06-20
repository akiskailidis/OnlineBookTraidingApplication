package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.model.Search;

@Component
public class SearchFactory {
	
	@Autowired
    private ExactSearchStrategy exactSearchStrategy;
    @Autowired
    private ApproximateSearchStrategy approxiamteSearchStrategy;

    
    public SearchStrategy getStrategy(Search search) {
        if (search.getSearchType() == Search.SearchType.EXACT) {
            return exactSearchStrategy;
            
        } else if (search.getSearchType() == Search.SearchType.APPROXIMATE) {
            return approxiamteSearchStrategy;
            
        } else {
            throw new IllegalArgumentException("Invalid search type specified");
        }
    }

}


