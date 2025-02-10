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
	
	public ArticleDocument(ArticleDTO dto) {
		
		this.id = UUID.randomUUID();
		
		this.tags = Set.copyOf( Stream.concat(dto.getFilmInfo().getGenres().stream(), dto.getFilmInfo().getCountries().stream()).toList());
		
		this.text = Arrays.asList(dto.getText().split("\n")).stream().filter(line->!"".equals(line)).toList();
		this.articleTitle = dto.getTitle();
		this.filmInfos = new FilmInfos(dto.getFilmInfo().getTitle(), dto.getFilmInfo().getYear(), dto.getFilmInfo().getDirectors(), dto.getFilmInfo().getStars());
		
		// TODO articleDate + filmSummary
		this.filmSummary = dto.getFilmInfo().getFilmSummary();
		this.articleDate = dto.getArticleDate();
		
		// TODO cover + poster
		this.cover = dto.getFilmInfo().getCover();
		this.poster = dto.getFilmInfo().getPoster();
	}
}
