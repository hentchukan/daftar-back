package prv.ferchichi.daftar.api.article;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

	private final ArticleService articleService;
	
	// @CrossOrigin(origins = "http://localhost:8080")
	@GetMapping("/{articleId}")
	public Mono<ArticleInfoDTO> getArticle(@PathVariable(name = "articleId", required = true) String id) {
		return articleService.getArticleById(id);
	}
	
	// @CrossOrigin(origins = "http://localhost:8080")
	@GetMapping("/overviews")
	public Flux<ArticleOverviewDTO> getArticleOverviews(@RequestParam(name = "genre", required = false) String genre) {
		return articleService.getArticleOverviews(genre);
	}
	
	// @CrossOrigin(origins = "http://localhost:8080")
	@PostMapping(path = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public Mono<Void> setArticles(
			@RequestPart("article") ArticleDTO article,
			@RequestPart(name = "posterImage", required = false) FilePart posterImage,
			@RequestPart(name = "coverImage", required = false) FilePart coverImage) {
		return articleService.upload(posterImage, article.getFilmInfo().getPoster(), 0)
				.zipWith(articleService.upload(coverImage, article.getFilmInfo().getCover(), 1))
				.flatMap(files -> articleService.createArticle(article));
	}
	
	// @CrossOrigin(origins = "http://localhost:8080")
	@PostMapping("/overviews")
	public Flux<ArticleOverviewDTO> getArticles(@RequestBody ArticleSearchFilter filter) {
   		return articleService.getArticleOverviews(filter);
	}
}
