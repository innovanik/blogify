package blogify.app.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StopWatch;

import blogify.app.api.data.SearchBlogResult;
import blogify.app.api.data.SearchWordPopularResult;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
@ActiveProfiles(profiles = {"local"})
@SpringBootTest(classes = {ApiApplication.class})
@AutoConfigureWebTestClient
@ImportAutoConfiguration
public class SearchTest {

	static final String TEST_KEYWORD = "블로그";

	static final String[] TEST_KEYWORDS = {
			"Audi", "BMW", "Bentley", "Cadillac", "Chevrolet", "Citroen", "DS",
			"Ford", "GMC", "Honda", "Jaguar", "Jeep", "Lamborghini", "Land Rover",
			"Lexus", "Lincoln", "MINI", "Maserati", "Mercedes-Benz", "Peugeot",
			"Polestar", "Porsche", "Rolls-Royce", "Toyota", "Volkswagen", "Volvo"
	};

	static final String BLOG_URL = "/search/blog";

	static final String WORD_POPULAR_URL = "/search/word/popular";

	@Autowired(required = false)
	private WebTestClient client;

	@Test
	void blogApiCallTest() {
		Flux<SearchBlogResult> flux = client.get()
											.uri(BLOG_URL + "?query={query}", TEST_KEYWORD)
											.exchange()
											.expectStatus()
											.isOk()
											.returnResult(SearchBlogResult.class)
											.getResponseBody();
		Duration duration =
				StepVerifier.create(flux)
							.assertNext(result -> {
								assertThat(result).isNotNull();
								assertThat(result.getResults()).isNotEmpty();
							})
							.verifyComplete();
		log.info("블로그 검색 조회 수행 시간(ms): {}", duration.toMillis());
	}

	@Test
	void wordPopularApiCallTest() {
		Flux<SearchWordPopularResult> flux = client.get()
													.uri(WORD_POPULAR_URL)
													.exchange()
													.expectStatus()
													.isOk()
													.returnResult(SearchWordPopularResult.class)
													.getResponseBody();
		Duration duration =
				StepVerifier.create(flux)
							.assertNext(result -> {
								assertThat(result).isNotNull();
								assertThat(result.getResults()).isNotEmpty();
							})
							.verifyComplete();
		log.info("인기 검색어 조회 수행 시간(ms): {}", duration.toMillis());
	}

	@Test
	void blogApiCallKeywoardsTest() {
		StopWatch watch = new StopWatch();
		watch.start();
		Flux<SearchBlogResult> flux = Flux.fromIterable(Arrays.stream(TEST_KEYWORDS)
																.map(keyword -> client.get()
																						.uri(BLOG_URL + "?query={keyword}", keyword)
																						.exchange()
																						.expectStatus()
																						.isOk()
																						.returnResult(SearchBlogResult.class))
																.collect(Collectors.toList()))
											.flatMap(fer -> fer.getResponseBody());
		AtomicInteger succeed = new AtomicInteger();
		flux.doOnNext(result -> {
			try {
				assertThat(result).isNotNull();
				assertThat(result.getResults()).isNotEmpty();
				succeed.incrementAndGet();
			} catch (Exception e) {
				// ignore
			}
		}).subscribe();
		watch.stop();
		assertThat(TEST_KEYWORDS.length).isEqualTo(succeed.get());
		log.info("블로그 검색 {}개 키워드 조회 수행 시간(ms): {}", TEST_KEYWORDS.length, watch.getTotalTimeMillis());
	}

	@Test
	void blogApiCall10000Test() {
		final int size = 10000;
		StopWatch watch = new StopWatch();
		watch.start();
		Flux<SearchBlogResult> flux = Flux.fromIterable(IntStream.range(0, size)
																	.mapToObj(i -> TEST_KEYWORD + i)
																	.map(keyword -> client.get()
																							.uri(BLOG_URL + "?query={keyword}", keyword)
																							.exchange()
																							.expectStatus()
																							.isOk()
																							.returnResult(SearchBlogResult.class))
																	.collect(Collectors.toList()))
											.flatMap(fer -> fer.getResponseBody());
		AtomicInteger succeed = new AtomicInteger();
		flux.doOnNext(result -> {
			try {
				assertThat(result).isNotNull();
				assertThat(result.getResults()).isNotEmpty();
				succeed.incrementAndGet();
			} catch (Exception e) {
				// ignore
			}
		}).subscribe();
		watch.stop();
		assertThat(size).isEqualTo(succeed.get());
		log.info("블로그 검색 {}번 조회 수행 시간(ms): {}", size, watch.getTotalTimeMillis());
	}
}
