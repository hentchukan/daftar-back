package prv.ferchichi.daftar.api.tag;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
public class TagController {

private final TagService tagService;
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping("/{tagId}")
	public Mono<TagDTO> getTag(@PathVariable(name = "tagId", required = true) String id) {
		return tagService.getTagById(id);
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping("/bytype/{type}")
	public Flux<TagDTO> getTagsByType(@PathVariable(required = true) String type) {
		return tagService.getAllTagsByType(type);
	}
}
