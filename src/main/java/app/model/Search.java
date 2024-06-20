package app.model;

public class Search {
	
	private String searchString;
	private SearchType searchType;
	private String message;

	
	public enum SearchType {
        EXACT, APPROXIMATE
    }
	
	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public SearchType getSearchType() {
		return searchType;
	}

	public void setSearchType(SearchType searchType) {
		this.searchType = searchType;
	}
	
	public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
