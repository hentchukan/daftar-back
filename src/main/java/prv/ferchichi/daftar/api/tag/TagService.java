package prv.ferchichi.daftar.api.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TagService {
	
	private final TagRepository repository;

	public Mono<TagDTO> getTagById(String id) {
		return repository.findById(id)
				.map(TagDocument::toDTO);
	}

	public Flux<TagDTO> getAllTagsByType(String type) {
		return repository.findAllByTypeIgnoreCase(type)
				.map(TagDocument::toDTO);
	}

	public Flux<TagDocument> updateTags(List<TagDocument> tags) {
		List<TagDocument> toSave = Collections.synchronizedList(new ArrayList<TagDocument>(tags));
		return repository.findAllById(tags.stream().map(TagDocument::getId).toList())
				.collectList()
				.map(toSave::removeAll)
				.thenMany(repository.saveAll(toSave));
	}
	
}
