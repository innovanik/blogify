package blogify.ext.kakao.api;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;

import blogify.ext.kakao.KakaoClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component(SearchBlogApi.BEAN_NAME)
public class SearchBlogApi {

	public static final String BEAN_NAME = "kakaoSearchBlogApi";

	private static final String BLOG = "/v2/search/blog";

	@Autowired
	private KakaoClient client;

	public <T> Mono<T> exchange(String query, Pageable pageable, Function<ClientResponse, ? extends Mono<T>> responseHandler) {
		log.debug("[Kakao] blog - Search: {}, Paging: {}", query, pageable);
		return client.get()
						.uri(BLOG, (uriBuilder) -> uriBuilder.queryParam("query", query)
																.queryParam("sort", pageable.getSort().stream().findFirst().get().getProperty())
																.queryParam("page", pageable.getPageNumber())
																.queryParam("size", pageable.getPageSize())
																.build())
						.exchangeToMono(responseHandler);
	}
}
