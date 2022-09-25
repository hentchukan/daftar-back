package prv.ferchichi.daftar.api.tag;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TagService {
	
	private final TagRepository tagRepository;

	public Mono<TagDTO> getTagById(String id) {
		return tagRepository.findById(id)
				.map(TagDocument::toDTO);
	}

	public Flux<TagDTO> getAllTagsByType(String type) {
		return tagRepository.findAllByType(type)
				.map(TagDocument::toDTO);
	}
	
}
