package prv.ferchichi.daftar.api.article;

import java.util.List;

import lombok.Value;

@Value
public class ArticleFilmInfoDTO {
	List<String> title;
	Short year;
	List<String> directors;
	List<String> stars;
	List<String> genres;
	List<String> countries;
	String filmSummary;
	String cover;
	String poster;
}
