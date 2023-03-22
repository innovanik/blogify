package blogify.app.api.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import blogify.app.api.data.SearchBlogData;
import blogify.app.api.data.SearchBlogResult;
import blogify.app.api.data.SearchWordPopularResult;
import blogify.core.exception.WebClientExternalResponseException;
import blogify.res.r2dbc.entity.BlogSearchWordEntity;
import blogify.res.r2dbc.repository.BlogSearchWordRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Validated
public class SearchService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	@Qualifier(blogify.ext.kakao.api.SearchBlogApi.BEAN_NAME)
	private blogify.ext.kakao.api.SearchBlogApi kakaoSearchBlogApi;

	@Autowired
	@Qualifier(blogify.ext.naver.api.SearchBlogApi.BEAN_NAME)
	private blogify.ext.naver.api.SearchBlogApi naverSearchBlogApi;

	@Autowired
	private BlogSearchWordRepository blogSearchWordRepository;

	public Mono<SearchBlogResult> blog(@Valid @NotNull SearchBlogData data) {
		log.debug("[Search] blog - data: {}", data);
		return kakaoSearchBlogApi
				.exchange(data.getQuery(), data.getPageable(), response -> handler("kakao", data, response))
				.onErrorResume(error -> {
					log.warn("[Search] blog - error: {}", error.getMessage());
					return naverSearchBlogApi.exchange(data.getQuery(), data.getPageable(), response -> handler("naver", data, response));
				})
				.doOnError(error -> {
					log.error("[Search] blog - error: {}", error.getMessage());
				})
				.doFinally(signal -> {
					switch ( signal ) {
						case ON_COMPLETE:
							saveBlogSearchWord(data).subscribe();
							break;
						default:
							break;
					}
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

	@Transactional
	public Mono<Void> saveBlogSearchWord(SearchBlogData data) {
		return blogSearchWordRepository
				.findByWord(data.getQuery())
				.defaultIfEmpty(BlogSearchWordEntity.builder()
													.word(data.getQuery())
													.cnt(0L)
													.build())
				.last()
				.doOnNext(entity -> {
					entity.setCnt(entity.getCnt() + 1L);
				})
				.flatMap(blogSearchWordRepository::save)
				.then();
	}

	public Mono<SearchWordPopularResult> wordPopular() {
		return blogSearchWordRepository
				.findGroupByTop10ByOrderByCntDesc()
				.map(entity -> SearchWordPopularResult.Result.builder().keyword(entity.getWord()).count(entity.getCnt()).build())
				.collectList()
				.map(list -> {
					SearchWordPopularResult result = new SearchWordPopularResult();
					result.setResults(list);
					return result;
				});
	}
}
