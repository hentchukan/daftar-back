package prv.ferchichi.daftar.api.article;

public record ArticleDTO(
		ArticleFilmInfoDTO filmInfo,
		String text,
		String title,
		String articleDate,
		float rating) {
}
