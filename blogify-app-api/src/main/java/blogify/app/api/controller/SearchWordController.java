package blogify.app.api.controller;

import java.util.Random;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class SearchWordController {

	@GetMapping("/search/word/popular")
	public Mono<String> getPopular(ServerHttpRequest request) throws Exception {
		log.info("[{}] {}", request.getId(), request.getPath());
		try {
			int delay = new Random().nextInt(500);
			log.info("delay: {}", delay);
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
		return Mono.just("hello world - " + request.getId());
	}
}
