package prv.ferchichi.daftar.api.article;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import prv.ferchichi.daftar.api.tag.TagDocument;
import prv.ferchichi.daftar.api.tag.TagService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleRepository repository;
	private final TagService tagService;
	
	List<ArticleOverviewDTO> articles = List.of(
			new ArticleOverviewDTO(UUID.fromString("dfaca0fe-a594-44d0-8b37-e571adaee1ba"), "من فضلك اِلمس غدي!", "Lunana, A Yak in the classroom", "https://storage.cloud.google.com/daftar-articles/lunana.jpg"),
			new ArticleOverviewDTO(UUID.fromString("35822b96-69c9-4e9d-b6a1-ded629b940b7"), "اِقطع، لا تقطع!", "Coupez!", "https://storage.googleapis.com/daftar-articles/coupez.webp"),
			new ArticleOverviewDTO(UUID.fromString("4805d3bc-20dd-487d-9524-0a8891385e75"), "حفلة الذكورة", "Top Gun: Maverick", "https://storage.googleapis.com/daftar-articles/topgunmaverick.jpg"),
			new ArticleOverviewDTO(UUID.fromString("155bfe14-d248-468b-98c7-8bbf8be54526"), "الذّات والقربان", "قربان", "https://storage.googleapis.com/daftar-articles/communion.jpg"),
			new ArticleOverviewDTO(UUID.fromString("faff1a3f-a3eb-427e-85e4-83dce4188dd8"), "الخوف من الفانتازيا", "فرططّو الذّهب", "https://storage.googleapis.com/daftar-articles/golden-butterfly.jpeg"),
			new ArticleOverviewDTO(UUID.fromString("76162b65-c7bb-4eb1-9071-0ca5cd2b946a"), "غدوة، أملا في الأمس", "غدوة", "https://storage.googleapis.com/daftar-articles/ghodwa.jpg")
			);
	
	public Flux<ArticleOverviewDTO> getArticleOverviews(String genre) {
		return ((genre == null ||  "".equals(genre)) ? 
				repository.findAll() :
				repository.findAllByTagsContainsIgnoreCase(genre))
					.map(ArticleOverviewDTO::new);
	}
	
	public Mono<ArticleInfoDTO> getArticleById(String id) {
		return repository
				.findById(id)
				.map(ArticleInfoDTO::new);
	}

	public Flux<ArticleOverviewDTO> getArticleOverviews(ArticleSearchFilter filter) {
		return repository.search(filter.getFilmTitle(), filter.getDirector(), filter.getYear(), filter.getCategory(), filter.getCountry(), filter.getStarring())
				.map(ArticleOverviewDTO::new);
	}

	public Mono<Void> createArticle(ArticleDTO article) {
		List<TagDocument> tags = new ArrayList<>(); 
				
		article.getFilmInfo()
			.getGenres()
			.stream()
			.map(genre -> new TagDocument(genre, "Genre", new ArrayList<String>(List.of(genre))))
			.forEach(tag -> tags.add(tag));
		
		article.getFilmInfo()
			.getCountries()
			.stream()
			.map(country -> new TagDocument(country, "Country", List.of(country)))
			.forEach(tag -> tags.add(tag));
		
		return tagService
				.updateTags(tags)
				.then(repository.save(new ArticleDocument(article)))
				.then();
	}
	
}
