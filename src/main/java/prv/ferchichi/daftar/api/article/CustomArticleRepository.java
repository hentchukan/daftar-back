package prv.ferchichi.daftar.api.article;

import reactor.core.publisher.Flux;

public interface CustomArticleRepository {

	Flux<ArticleDocument> search(String title, String director, Integer year, String category, String country, String starring);
}
