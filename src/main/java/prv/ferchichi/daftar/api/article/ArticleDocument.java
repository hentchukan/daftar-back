package prv.ferchichi.daftar.api.article;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import prv.ferchichi.daftar.api.filminfo.FilmInfos;

@Document(collection = "Articles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ArticleDocument {

	@Id()
	private UUID id;
	private FilmInfos filmInfos;
	private String filmSummary;
	private String articleDate;
	private Set<String> tags;
	private List<String> text;
	private String articleTitle;
	private String cover;
	private String poster;
	private float rating;
	
	public ArticleDocument(ArticleDTO dto) {
		
		this.id = UUID.randomUUID();
		
		this.tags = Set.copyOf( Stream.concat(dto.filmInfo().getGenres().stream(), dto.filmInfo().getCountries().stream()).toList());
		
		this.text = Arrays.stream(dto.text().split("\n")).filter(line-> !line.isEmpty()).toList();
		this.articleTitle = dto.title();
		this.filmInfos = new FilmInfos(dto.filmInfo().getTitle(), dto.filmInfo().getYear(), dto.filmInfo().getDirectors(), dto.filmInfo().getStars());
		
		// TODO articleDate + filmSummary
		this.filmSummary = dto.filmInfo().getFilmSummary();
		this.articleDate = dto.articleDate();
		this.rating = dto.rating();

		// TODO cover + poster
		this.cover = dto.filmInfo().getCover();
		this.poster = dto.filmInfo().getPoster();
	}
}
