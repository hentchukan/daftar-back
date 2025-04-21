package prv.ferchichi.daftar.api.share;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class FacebookCrawlerAccessor {

    @GetMapping("/robots.txt")
    public ResponseEntity<String> robots() {
        String content = """
        User-agent: facebookexternalhit
        Allow: /v1/articles/share/

        User-agent: *
        Disallow: /
        """;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/plain")
                .body(content);
    }
}
