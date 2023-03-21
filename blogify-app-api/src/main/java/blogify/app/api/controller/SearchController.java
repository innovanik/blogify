package blogify.app.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import blogify.app.api.data.SearchBlogData;
import blogify.app.api.data.SearchBlogResult;
import blogify.app.api.data.SearchWordPopularResult;
import blogify.app.api.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private SearchService searchService;

	@GetMapping("/blog")
	@ResponseBody
	public Mono<SearchBlogResult> blog(ServerHttpRequest request, @Valid SearchBlogData data) throws Exception {
		log.info("[{}] {} - {}", request.getId(), request.getPath(), data);
		return searchService.blog(data);
	}

	@GetMapping("/word/popular")
	public Mono<SearchWordPopularResult> wordPopular(ServerHttpRequest request) throws Exception {
		log.info("[{}] {}", request.getId(), request.getPath());
		return searchService.wordPopular();
	}
}
