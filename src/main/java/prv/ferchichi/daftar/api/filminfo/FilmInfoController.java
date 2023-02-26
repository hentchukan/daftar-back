package prv.ferchichi.daftar.api.filminfo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import prv.ferchichi.daftar.api.article.FilmInfoDTO;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/filminfos")
@RequiredArgsConstructor
public class FilmInfoController {

	private final FilmInfoService filmInfoService;
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping()
	public Flux<FilmInfoDTO> getFilmInfos() {
		return filmInfoService.getAll();
	}
}
