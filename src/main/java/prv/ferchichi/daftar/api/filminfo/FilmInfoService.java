package prv.ferchichi.daftar.api.filminfo;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import prv.ferchichi.daftar.api.article.ArticleRepository;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class FilmInfoService {

	private final ArticleRepository repository;
	
	public Flux<FilmInfoDTO> getAll() {
		return repository.findFilmInfos();
	}
	
	public Flux<DirectorDTO> getDirectors() {
		return repository.findAllDirectors();
	}
	
	public Flux<StarDTO> getStars() {
		return repository.findAllStars();
	}
}
