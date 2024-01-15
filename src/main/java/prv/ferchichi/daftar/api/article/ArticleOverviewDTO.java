package prv.ferchichi.daftar.api.article;

import java.util.UUID;

import org.springframework.lang.NonNull;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class ArticleOverviewDTO {
	UUID id;
	String title;
	String film;
	String img;
	
	public ArticleOverviewDTO(@NonNull ArticleDocument document) {
		this.id = document.getId();
		this.title = document.getArticleTitle();
		this.film = (document.getFilmInfos() != null && document.getFilmInfos().getTitle() != null) ? 
				document.getFilmInfos().getTitle().get(0) : null;
		this.img = document.getCover();
	}
}
