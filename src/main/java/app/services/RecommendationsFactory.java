package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.model.UserProfile;

@Component
public class RecommendationsFactory {

    @Autowired
    private AuthorRecommendationStrategy authorRecommendationStrategy;
    @Autowired
    private CategoryRecommendationStrategy categoryRecommendationStrategy;

    public RecommendationsStrategy getStrategy(UserProfile userProfile) {
    	if (!userProfile.getFavouriteBookAuthors().isEmpty()) {
            return authorRecommendationStrategy;
        }
    	else if (!userProfile.getFavouriteBookCategories().isEmpty()){
            return categoryRecommendationStrategy;
        }
    	else {
            throw new IllegalArgumentException("No valid criteria found for recommendations");
        }
    }
}
