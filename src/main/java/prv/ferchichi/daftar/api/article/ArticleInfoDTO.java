package prv.ferchichi.daftar.api.article;

import java.util.List;
import java.util.Set;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Value;

@Value
public class ArticleInfoDTO {
	
	@JsonIgnore
	String articleId;
	FilmInfoDTO filmInfos;
	String summary;
	String date;
	Set<String> tags;
	List<String> text;
	String articleTitle;
	String cover;
	
	public ArticleInfoDTO(@NonNull ArticleDocument document) {
		this.articleId = document.getId().toString();
		this.filmInfos = new FilmInfoDTO(
				document.getFilmInfos().getTitle(), 
				document.getFilmInfos().getYear(), 
				document.getFilmInfos().getDirector(), 
				document.getFilmInfos().getStars(),
				null);
		this.summary = document.getFilmSummary(); 
		this.date = document.getArticleDate(); 
		this.tags = document.getTags();
		this.text = document.getText(); 
		this.articleTitle = document.getArticleTitle(); 
		this.cover = document.getCover();
	}
}
