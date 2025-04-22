package prv.ferchichi.daftar.api.share;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prv.ferchichi.daftar.api.article.ArticleService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class SocialCrawlerAccessorController {

    private final ArticleService articleService;

    @GetMapping("/robots.txt")
    public ResponseEntity<String> robots() {
        String content = """
               User-agent: twitterbot
               Allow: /v1/articles/share/
               Allow: /aboutus/share/
    
               User-agent: facebookexternalhit
               Allow: /v1/articles/share/
               Allow: /aboutus/share/

               User-agent: *
               Disallow: /
        """;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/plain")
                .body(content);
    }

    @GetMapping("articles/share/{articleId}")
    public Mono<ResponseEntity<String>> getArticleFacebookShare(@PathVariable(name = "articleId", required = true) final String articleId) {
        return articleService.getArticleById(articleId)
                .map(dto -> {
                    String html = """
						<!DOCTYPE html>
						<html lang="en">
						<head>
						  <meta charset="UTF-8">
						  <title>الـدّفتـــر الأزرق - %s</title>
		
						  <meta property="og:title" content="الـدّفتـــر الأزرق - %s" />
						  <meta property="og:description" content="%s" />
						  <meta property="og:image" content="%s" />
						  <meta property="og:url" content="https://bluedaftar.com/articles/single-article/%s" />
						  <meta property="og:type" content="article" />
						
						  <meta name="twitter:card" content="summary_large_image" />
						  <meta name="twitter:title" content="%s" />
						  <meta name="twitter:description" content="%s" />
						  <meta name="twitter:image" content="%s" />
						
						  <script>window.location.href = "https://bluedaftar.com/articles/single-article/%s"</script>
						</head>
						<body>
						  <p>Redirecting to the article...</p>
						</body>
						</html>
						""".formatted(
                            dto.getArticleTitle(),
                            dto.getArticleTitle(),
                            dto.getSummary(),
                            dto.getCover(),
                            articleId,
                            dto.getArticleTitle(),
                            dto.getSummary(),
                            dto.getCover(),
                            articleId
                    );

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                            .body(html);
                });
    }

	@GetMapping("aboutus/share")
	public ResponseEntity<String> getAboutUsShare() {
		final String description = """
				جاء موقع الدفتر الأزرق لجمع كلّ الكتابات السينمائيّة في فضاءٍ واحدٍ، 
				ولتوفير أشكالِ عرضٍ أكثر ملاءمةً لقراءةِ المقالاتِ النقديّةِ على طولِها، والنصوص عامّةً بمختلفِ أغراضِها. 
				ومادام قد وقع الخيار على فضاءٍ الكترونيٍّ مُعدٍّ بالكاملِ للغرضِ، 
				فلتكن الأحلامُ أكبر، ولتكن الاستفادةُ من السّلاح الرقميِّ أوسعَ.
				""";
		final String url = "https://bluedaftar.com/aboutus";
		final String title = "مَوْقِعُ الـدّفْتَـــرِ الأَزْرَق";
		final String imageUrl = "https://storage.googleapis.com/daftar-articles/daftar-cover.jpg";

		final String html = """
						<!DOCTYPE html>
						<html lang="en">
						<head>
						  <meta charset="UTF-8">
						  <title>%sِ</title>
		
						  <meta property="og:title" content="%s" />
						  <meta property="og:description" content="%s\s" />
						  <meta property="og:image" content="%s" />
						  <meta property="og:url" content="%s" />
						  <meta property="og:type" content="article" />
						
						  <meta name="twitter:card" content="summary_large_image" />
						  <meta name="twitter:title" content="%s" />
						  <meta name="twitter:description" content="%s" />
						  <meta name="twitter:image" content="%s" />
						
						  <script>window.location.href = "%s"</script>
						</head>
						<body>
						  <p>Redirecting to the article...</p>
						</body>
						</html>
						""".formatted(
							title,
							title,
							description,
							imageUrl,
							url,
							title,
							description,
							imageUrl,
							url
					);

					return ResponseEntity.ok()
							.header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
							.body(html);
	}
}
