package prv.ferchichi.daftar.api.filminfo;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import prv.ferchichi.daftar.api.article.ArticleRepository;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class DirectorService {

	private final ArticleRepository repository;
	
	public Flux<DirectorDTO> getDirectors() {
		return repository.findAllDirectors();
	}

}
