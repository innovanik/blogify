package blogify.app.api.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import blogify.app.api.data.SearchBlogData;
import blogify.app.api.data.SearchBlogResult;
import blogify.app.api.exception.WebClientExternalResponseException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Validated
public class SearchBlogService {

	@Autowired
	@Qualifier(blogify.ext.kakao.api.SearchBlogApi.BEAN_NAME)
	private blogify.ext.kakao.api.SearchBlogApi kakaoSearchBlogApi;

	@Autowired
	@Qualifier(blogify.ext.naver.api.SearchBlogApi.BEAN_NAME)
	private blogify.ext.naver.api.SearchBlogApi naverSearchBlogApi;

	@Autowired
	private ObjectMapper mapper;

	public Mono<SearchBlogResult> get(@Valid @NotNull SearchBlogData data) {
		log.debug("[Search] blog - data: {}", data);
		return kakaoSearchBlogApi
				.exchange(data.getQuery(), data.getPageable(), response -> handler("kakao", data, response))
				.onErrorResume(error -> {
					log.warn("[Search] blog - error: {}", error.getMessage());
					return naverSearchBlogApi.exchange(data.getQuery(), data.getPageable(), response -> handler("naver", data, response));
				})
				.doOnSuccess(result -> {
					// TODO DB 저장
				})
				.doOnError(error -> {
					log.error("[Search] blog - error: {}", error.getMessage());
				});
	}

	private Mono<SearchBlogResult> handler(String server, SearchBlogData data, ClientResponse response) {
		return response.bodyToMono(String.class).map(body -> {
			SearchBlogResult result;
			try {
				result = mapper.readValue(body, SearchBlogResult.class);
			} catch (Exception e) {
				throw new WebClientExternalResponseException(e, response, body);
			}
			if ( response.statusCode().isError() ) {
				throw new WebClientExternalResponseException(result.getMessage(), response, body);
			}
			result.setServer(server);
			result.setQuery(data.getQuery());
			result.setPageable(data.getPageable());
			return result;
		});
	}
}
