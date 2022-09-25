package prv.ferchichi.daftar.api.article;

import java.util.List;

import lombok.Value;

@Value
public class FilmInfoDTO {
	String title;
	String director;
	Short year;
	List<String> stars;
}
