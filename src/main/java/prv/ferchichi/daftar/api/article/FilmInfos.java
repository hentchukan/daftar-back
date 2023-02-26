package prv.ferchichi.daftar.api.article;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FilmInfos {
	private List<String> title;
	private Short year;
	private List<String> director;
	private List<String> stars;
}
