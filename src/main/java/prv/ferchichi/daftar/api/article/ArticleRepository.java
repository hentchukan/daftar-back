package prv.ferchichi.daftar.api.article;

import java.util.UUID;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArticleRepository extends ReactiveMongoRepository<ArticleDocument, UUID>, CustomArticleRepository {
	
	Flux<ArticleDocument> findAll();
	Mono<ArticleDocument> findById(String id);
	Flux<ArticleDocument> findAllByTagsContainsIgnoreCase(String genre);
	
}
