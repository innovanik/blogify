package blogify.app.api.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import blogify.app.api.data.SearchBlogData;
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

	public Mono<String> get(@Valid @NotNull SearchBlogData data) {
		log.debug("[Search] blog - {}", data);
		return kakaoSearchBlogApi.get(data.getQuery(), PageRequest.of(data.getPage(), data.getSize(), Sort.by(Order.desc(data.getSort()))));
//		return naverSearchBlogApi.get(data.getQuery(), PageRequest.of(data.getPage(), data.getSize(), Sort.by(Order.desc(data.getSort()))));
	}
}
