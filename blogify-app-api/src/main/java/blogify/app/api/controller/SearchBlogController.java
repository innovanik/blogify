package blogify.app.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import blogify.app.api.data.SearchBlogData;
import blogify.app.api.service.SearchBlogService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class SearchBlogController {

	@Autowired
	private SearchBlogService searchBlogService;

	@GetMapping("/search/blog")
	@ResponseBody
	public Mono<String> get(ServerHttpRequest request, @Valid SearchBlogData data) throws Exception {
		log.info("[{}] {} - {}", request.getId(), request.getPath(), data);
		return searchBlogService.get(data);
	}
}
