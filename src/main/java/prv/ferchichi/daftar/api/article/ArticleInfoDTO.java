package prv.ferchichi.daftar.api.article;

import java.util.List;
import java.util.Set;

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
}
