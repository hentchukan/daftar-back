package prv.ferchichi.daftar.api.article;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.In;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.And;
import org.springframework.data.mongodb.core.query.Criteria;

import lombok.RequiredArgsConstructor;
import prv.ferchichi.daftar.api.filminfo.DirectorDTO;
import prv.ferchichi.daftar.api.filminfo.FilmInfoDTO;
import prv.ferchichi.daftar.api.filminfo.StarDTO;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

	private final ReactiveMongoTemplate template;

	public Flux<ArticleDocument> search(String title, String director, Integer year, String category, String country,
			String starring) {
		List<AggregationOperation> operations = new ArrayList<>();

		Criteria criteria = new Criteria();
		// Title
		if (title != null && !title.isBlank()) {
			title = ".*" + title.replaceAll("([\\u064B-\\u0652])", "") + ".*";

			// MongoDB's $regex operator is used here for pattern matching in a case-insensitive manner ('i' flag)
			criteria = criteria.andOperator(Criteria.where("filmInfos.title").regex(title, "i"));
			
			operations.add(projectForTitleSearch());
		}

		// Director
		criteria = conditionalAlike(criteria, "filmInfos.director", director);
		// Year
		criteria = conditionalEqual(criteria, "filmInfos.year", year);
		// Starring
		criteria = conditionalIn(criteria, "filmInfos.stars", starring);

		List<In> ins = new ArrayList<>();
		// Country
		if (country != null && !country.isEmpty()) {
			ins.add(In.arrayOf("tags").containsValue(country));
		}
		
		// Category
		if (category != null && !category.isEmpty()) {
			ins.add(In.arrayOf("tags").containsValue(category));
		}
		
		EvaluationOperators.Expr matchTags = EvaluationOperators.Expr.valueOf(And.and(ins.toArray()));

		operations.addAll(List.of(new MatchOperation(criteria), new MatchOperation(matchTags)));
		return aggregate(operations);
	}

	private AggregationOperation projectForTitleSearch() {
		// Example of a custom aggregation stage using raw BSON, adjust according to actual needs
		Document rawProjectionStage = Document.parse("{"
				+ "  '$project': {"
				+ "'filmInfos.title': {" +
				"    $map: {" +
				"      input: '$filmInfos.title'," +
				"      as: 'item'," +
				"      in: {" +
				"        $reduce: {" +
				"                input: {" +
				"                  $regexFindAll: {" +
				"                    input: '$$item'," +
				"                    regex: '[^\\u064B-\\u065F\\u0670\\u06D6-\\u06DC\\u06DF-\\u06E8\\u06EA-\\u06ED]'," +
				"                    options: ''" +
				"                  }" +
				"                }," +
				"                initialValue: ''," +
				"                in: { '$concat': ['$$value', '$$this.match'] }" +
				"              }" +
				"      }" +
				"    }" +
				"  }"
				+ "    'tags': 1,"
				+ "    '_id': 1,"
				+ "    'filmSummary': 1,"
				+ "    'articleDate': 1,"
				+ "    'text': 1,"
				+ "    'articleTitle': 1,"
				+ "    'cover': 1,"
				+ "    'poster': 1"
				+ "  }"
				+ "}");

		AggregationOperation customProjectionOperation = new AggregationOperation() {
			@Override
			public Document toDocument(AggregationOperationContext context) {
				return rawProjectionStage;
			}
		};

		return customProjectionOperation;
	}

	public Flux<DirectorDTO> findAllDirectors() {
        UnwindOperation unwind = new UnwindOperation(Fields.field("filmInfos.director"));
		GroupOperation group = new GroupOperation(Fields.from(Fields.field("name", "filmInfos.director")));
		ProjectionOperation project = new ProjectionOperation(Fields.from(Fields.field("name", "_id")));
        List<AggregationOperation> operations = new ArrayList<>(List.of(unwind, group, project));
		return aggregate(operations, DirectorDTO.class);
	}
	
	@Override
	public Flux<StarDTO> findAllStars() {
        UnwindOperation unwind = new UnwindOperation(Fields.field("filmInfos.stars"));
		GroupOperation group = new GroupOperation(Fields.from(Fields.field("name", "filmInfos.stars")));
		ProjectionOperation project = new ProjectionOperation(Fields.from(Fields.field("name", "_id")));
        List<AggregationOperation> operations = new ArrayList<>(List.of(unwind, group, project));
		return aggregate(operations, StarDTO.class);
	}
	
	public Flux<FilmInfoDTO> findFilmInfos() {
//		List<AggregationOperation> operations = new ArrayList<>();
		return Flux.empty();
	}
	
	private Criteria conditionalIn(Criteria criteria, String fieldName, String member) {
		if (member != null && !member.isBlank()) {
			return criteria.and(fieldName).regex(".*" + member + ".*", "i");
		} else {
			return criteria;
		}
	}

	private Criteria conditionalAlike(final Criteria criteria, String fieldName, String value) {
		if (value != null && !value.isBlank()) {
			return criteria.and(fieldName).regex(value);
		} else {
			return criteria;
		}
	}

	private Criteria conditionalEqual(final Criteria criteria, String fieldName, Object value) {
		if (value != null) {
			return criteria.and(fieldName).is(value);
		} else {
			return criteria;
		}
	}

	private Flux<ArticleDocument> aggregate(List<AggregationOperation> operations) {
		return aggregate(operations, ArticleDocument.class);
	}

	private <T> Flux<T> aggregate(List<AggregationOperation> operations, Class<T> clazz) {
		return template.aggregate(
				Aggregation.newAggregation(operations)
						.withOptions(AggregationOptions.builder().allowDiskUse(true).build()),
				ArticleDocument.class, clazz);
	}
	
}
