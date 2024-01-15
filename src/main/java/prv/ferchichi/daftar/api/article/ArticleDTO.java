package prv.ferchichi.daftar.api.article;

import lombok.Value;

@Value
public class ArticleDTO {
	
	ArticleFilmInfoDTO filmInfo;
	String text;
	String title;
	String articleDate;
}
