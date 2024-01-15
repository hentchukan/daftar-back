package prv.ferchichi.daftar.api.tag;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Value;

@Document(collection = "Tags")
@Value
public class TagDocument {
	String id;
	String type;
	List<String> label;
	
	public TagDTO toDTO() {
		return new TagDTO(getId(), getType(), getLabel().get(0));
	}
}
