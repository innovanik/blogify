package blogify.ext.kakao.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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

	public Mono<String> get(String query, Pageable pageable) {
		log.debug("[Kakao] blog - Search: {}, Paging: {}", query, pageable);
		return client.get()
						.uri(BLOG, (uriBuilder) -> {
							return uriBuilder.queryParam("query", query)
												.queryParam("sort", pageable.getSort().stream().findFirst().get().getProperty())
												.queryParam("page", pageable.getPageNumber())
												.queryParam("size", pageable.getPageSize())
												.build();
						})
						.retrieve()
						.bodyToMono(String.class);
	}
}
