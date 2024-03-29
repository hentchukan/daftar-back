package prv.ferchichi.daftar.api.article;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.In;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators.And;
import org.springframework.data.mongodb.core.aggregation.EvaluationOperators.EvaluationOperatorFactory.Expr;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import lombok.RequiredArgsConstructor;
import prv.ferchichi.daftar.api.filminfo.DirectorDTO;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

	private final ReactiveMongoTemplate template;

	public Flux<ArticleDocument> search(String title, String director, Integer year, String category, String country,
			String starring) {
		List<AggregationOperation> operations = new ArrayList<>();

		Criteria criteria = new Criteria();
		// Title
		criteria = conditionalAlike(criteria, "filmInfos.title", title);
		// Director
		criteria = conditionalAlike(criteria, "filmInfos.director", director);
		// Year
		criteria = conditionalEqual(criteria, "filmInfos.year", year);
		// Starring
		criteria = conditionalIn(criteria, "filmInfos.stars", starring);

		List<In> ins = new ArrayList<>();
		// Country
		if (country != null && !"".equals(country)) {
			ins.add(In.arrayOf("tags").containsValue(country));
		}
		
		// Category
		if (category != null && !"".equals(category)) {
			ins.add(In.arrayOf("tags").containsValue(category));
		}
		
		Expr matchTags = Expr.valueOf(And.and(ins.toArray()));
		
		operations.addAll(List.of(new MatchOperation(criteria), new MatchOperation(matchTags)));
		return aggregate(operations);
	}

	public Flux<DirectorDTO> findAllDirectors() {
		List<AggregationOperation> operations = new ArrayList<>();
		UnwindOperation unwind = new UnwindOperation(Fields.field("filmInfos.director"));
		GroupOperation group = new GroupOperation(Fields.from(Fields.field("name", "filmInfos.director")));
		ProjectionOperation project = new ProjectionOperation(Fields.from(Fields.field("name", "_id")));
		operations.addAll(List.of(unwind, group, project));
		return aggregate(operations, DirectorDTO.class);
	}
	
	public Flux<FilmInfoDTO> findFilmInfos() {
		List<AggregationOperation> operations = new ArrayList<>();
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
