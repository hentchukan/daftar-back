package prv.ferchichi.daftar.api.article;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
}
