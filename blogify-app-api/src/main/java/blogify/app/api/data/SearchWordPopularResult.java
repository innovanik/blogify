package blogify.app.api.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import blogify.core.context.AbstractResult;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonPropertyOrder({"result"})
public class SearchWordPopularResult extends AbstractResult {

	private static final long serialVersionUID = -6715585737579106911L;

	private List<Result> results = new ArrayList<>();

	@Builder
	@Data
	public static class Result {

		private String keyword;

		private Long count;
	}
}
