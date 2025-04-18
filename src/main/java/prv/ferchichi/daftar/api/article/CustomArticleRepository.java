package prv.ferchichi.daftar.api.article;

import prv.ferchichi.daftar.api.filminfo.DirectorDTO;
import prv.ferchichi.daftar.api.filminfo.FilmInfoDTO;
import prv.ferchichi.daftar.api.filminfo.StarDTO;
import reactor.core.publisher.Flux;

public interface CustomArticleRepository {

	Flux<ArticleDocument> search(String title, String director, Integer year, String category, String country, String starring, RatingRange rating);
	Flux<DirectorDTO> findAllDirectors();
	Flux<FilmInfoDTO> findFilmInfos();
	Flux<StarDTO> findAllStars();
}
