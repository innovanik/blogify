package blogify.ext.naver.api;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;

import blogify.ext.naver.NaverClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component(SearchBlogApi.BEAN_NAME)
public class SearchBlogApi {

	public static final String BEAN_NAME = "naverSearchBlogApi";

	private static final String BLOG = "/v1/search/blogg.json";

	@Autowired
	private NaverClient client;

	public <T> Mono<T> exchange(String query, Pageable pageable, Function<ClientResponse, ? extends Mono<T>> responseHandler) {
		log.debug("[Naver] blog - Search: {}, Paging: {}", query, pageable);
		return client.get()
						.uri(BLOG, (uriBuilder) -> {
							return uriBuilder.queryParam("query", query)
												.queryParam("sort", getSort(pageable.getSort()))
												.queryParam("start", getStart(pageable))
												.queryParam("display", pageable.getPageSize())
												.build();
						})
						.exchangeToMono(responseHandler);
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
