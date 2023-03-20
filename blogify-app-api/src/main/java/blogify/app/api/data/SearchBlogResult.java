package blogify.app.api.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import blogify.core.context.AbstractResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonPropertyOrder({"result"})
public class SearchBlogResult extends AbstractResult {

	private static final long serialVersionUID = -7271672732919210695L;

	@JsonIgnore
	private Pageable pageable;

	@JsonAlias({"documents", "items"})
	private List<Result> results = new ArrayList<>();

	private String server;

	private String query;

	@JsonAlias({"total"})
	private Integer total = 0;

	@JsonProperty("page")
	public Optional<Integer> getPage() {
		return Optional.ofNullable(getPageable())
						.map(pageable -> pageable.getPageNumber());
	}

	@JsonProperty("size")
	public Optional<Integer> getSize() {
		return Optional.ofNullable(getPageable())
						.map(pageable -> pageable.getPageSize());
	}

	@JsonProperty("sort")
	public Optional<String> getSort() {
		return Optional.ofNullable(getPageable())
						.flatMap(pageable -> pageable.getSort().stream().findFirst())
						.map(order -> order.getProperty())
						.map(property -> (String) property);
	}

	@SuppressWarnings("unchecked")
	@JsonAnySetter
	public void any(String property, Object value) {
		if ( StringUtils.equals("meta", property) ) {
			Map<String, Object> map = (Map<String, Object>) value;
			this.total = (Integer) map.get("total_count");
		}
	}

	@Data
	public static class Result {

		@JsonAlias({"title"})
		private String title;

		@JsonAlias({"contents", "description"})
		private String content;

		@JsonAlias({"url", "link"})
		private String url;

		@JsonAlias({"blogname", "bloggername"})
		private String name;

		@JsonAlias({"datetime", "postdate"})
		private String date;
	}
}
