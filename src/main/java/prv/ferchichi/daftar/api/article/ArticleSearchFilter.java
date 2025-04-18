package prv.ferchichi.daftar.api.article;

import lombok.Value;

@Value
public class ArticleSearchFilter {
	Integer year;
	String category;
	String country;
	String filmTitle;
	String director;
	String starring;
	RatingRange rating;
}
