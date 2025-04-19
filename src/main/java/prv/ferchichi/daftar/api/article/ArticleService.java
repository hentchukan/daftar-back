package prv.ferchichi.daftar.api.article;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import prv.ferchichi.daftar.api.media.BucketService;
import prv.ferchichi.daftar.api.tag.TagDTO;
import prv.ferchichi.daftar.api.tag.TagDocument;
import prv.ferchichi.daftar.api.tag.TagService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

	private final ArticleRepository repository;
	private final TagService tagService;
	private final BucketService bucketService;
	
	public Flux<ArticleOverviewDTO> getArticleOverviews(String genre) {
		return ((genre == null || genre.isEmpty()) ?
				repository.findAll() :
				repository.findAllByTagsContainsIgnoreCase(genre))
					.map(ArticleOverviewDTO::new)
				;
	}
	
	public Mono<ArticleInfoDTO> getArticleById(String id) {
		return repository
				.findById(UUID.fromString(id))
				.flatMap(articleDocument ->
					tagService.getTagsByIds(articleDocument.getTags())
							.map(TagDTO::getLabel)
							.collectList()
							.map(tags -> {
								articleDocument.setTags(new HashSet<>(tags));
								return articleDocument;
							})
				)
				.map(ArticleInfoDTO::new);
	}

	public Flux<ArticleOverviewDTO> getArticleOverviews(ArticleSearchFilter filter) {
		return repository.search(filter.getFilmTitle(), filter.getDirector(), filter.getYear(), filter.getCategory(), filter.getCountry(), filter.getStarring(), filter.getRating())
				.map(ArticleOverviewDTO::new);
	}

	public Mono<Void> createArticle(ArticleDTO article) {
		List<TagDocument> tags = new ArrayList<>(); 
				
		article.filmInfo()
			.getGenres()
			.stream()
			.map(genre -> new TagDocument(genre, "Genre", new ArrayList<String>(List.of(genre))))
			.forEach(tags::add);
		
		article.filmInfo()
			.getCountries()
			.stream()
			.map(country -> new TagDocument(country, "Country", List.of(country)))
			.forEach(tags::add);

		return tagService
				.updateTags(tags)
				.then(Mono.just(new ArticleDocument(article)))
				.doOnNext(document -> document.setPoster("https://storage.googleapis.com/"+bucketService.getBucketName()+"/"+document.getPoster()))
				.doOnNext(document -> document.setCover("https://storage.googleapis.com/"+bucketService.getBucketName()+"/"+document.getCover()))
				.flatMap(repository::save)
				.then();
	}

	public Mono<String> upload(FilePart coverImage, String name) {
		return bucketService
				.uploadImage(coverImage, name)
				.doOnNext(log::info);
	}
	
}
