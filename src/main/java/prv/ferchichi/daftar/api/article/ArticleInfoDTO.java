package prv.ferchichi.daftar.api.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;
import org.springframework.lang.NonNull;
import prv.ferchichi.daftar.api.filminfo.FilmInfoDTO;
import prv.ferchichi.daftar.api.tag.TagDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Value
public class ArticleInfoDTO {

    @JsonIgnore
    String articleId;
    FilmInfoDTO filmInfos;
    String summary;
    String date;
    Set<TagDTO> tags;
    List<String> text;
    Float rating;
    String articleTitle;
    String cover;

    public ArticleInfoDTO(@NonNull ArticleDocument document) {
        this(document, null);
    }

    public ArticleInfoDTO(@NonNull ArticleDocument document, List<TagDTO> tags) {
        this.articleId = document.getId().toString();
        this.filmInfos = new FilmInfoDTO(
                document.getFilmInfos().getTitle(),
                document.getFilmInfos().getYear(),
                document.getFilmInfos().getDirector(),
                document.getFilmInfos().getStars(),
                extractGenres(document.getTags()),
                extractCountries(document.getTags()));
        this.summary = document.getFilmSummary();
        this.date = document.getArticleDate();
        this.tags = tags == null ? document.getTags().stream().map(label -> new TagDTO(label, label, label)).collect(Collectors.toSet()) : Set.copyOf(tags);
        this.text = document.getText();
        this.rating = document.getRating();
        this.articleTitle = document.getArticleTitle();
        this.cover = document.getCover();
    }

    private List<String> extractCountries(Set<String> tags) {
        return tags.stream().toList();
    }

    private List<String> extractGenres(Set<String> tags) {
        // TODO Auto-generated method stub
        return null;
    }
}
