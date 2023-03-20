package blogify.ext.naver.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import blogify.ext.naver.NaverClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component(SearchBlogApi.BEAN_NAME)
public class SearchBlogApi {

	public static final String BEAN_NAME = "naverSearchBlogApi";

	private static final String BLOG = "/v1/search/blog.json";

	@Autowired
	private NaverClient client;

	public Mono<String> get(String query, Pageable pageable) {
		log.debug("[Naver] blog - Search: {}, Paging: {}", query, pageable);
		return client.get()
						.uri(BLOG, (uriBuilder) -> {
							uriBuilder.queryParam("query", query);
							if ( pageable != null ) {
								uriBuilder
									.queryParam("sort", getSort(pageable.getSort()))
									.queryParam("start", getStart(pageable))
									.queryParam("display", pageable.getPageSize());
							}
							return uriBuilder.build();
						})
						.retrieve()
						.bodyToMono(String.class);
	}

	private String getSort(Sort sort) {
		String property = sort.stream().findFirst().get().getProperty();
		switch ( property ) {
			case "recency":
				return "date";
			default:
				return "sim";
		}
	}

	private int getStart(Pageable pageable) {
		return ((pageable.getPageNumber() - 1) * pageable.getPageSize()) + 1;
	}
}
