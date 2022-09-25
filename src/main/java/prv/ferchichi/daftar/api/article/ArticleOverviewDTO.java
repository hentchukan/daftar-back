package prv.ferchichi.daftar.api.article;

import java.util.UUID;

import lombok.Value;

@Value
public class ArticleOverviewDTO {
	UUID id;
	String title;
	String film;
	String img;
}
