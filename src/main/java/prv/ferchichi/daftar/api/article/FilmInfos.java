package prv.ferchichi.daftar.api.article;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FilmInfos {
	private String title;
	private String director;
	private Short year;
	private List<String> stars;
}
