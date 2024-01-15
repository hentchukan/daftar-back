package prv.ferchichi.daftar.api.tag;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TagRepository extends ReactiveMongoRepository<TagDocument, String> {
	Mono<TagDocument> findById(String id);
	Flux<TagDocument> findAllByType(String type);
}
