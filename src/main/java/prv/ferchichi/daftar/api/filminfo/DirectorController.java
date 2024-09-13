package prv.ferchichi.daftar.api.filminfo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1/directors")
@RequiredArgsConstructor
public class DirectorController {

	private final DirectorService directorService;
	
	// @CrossOrigin(origins = "http://localhost:8080")
	@GetMapping()
	public Flux<DirectorDTO> getDirectors() {
		return directorService.getDirectors();
	}
	
}
