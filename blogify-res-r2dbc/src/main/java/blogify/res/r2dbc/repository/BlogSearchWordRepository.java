package blogify.res.r2dbc.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import blogify.res.r2dbc.entity.BlogSearchWordEntity;
import reactor.core.publisher.Flux;

public interface BlogSearchWordRepository extends R2dbcRepository<BlogSearchWordEntity, Long> {

	Flux<BlogSearchWordEntity> findByWord(String word);

	Flux<BlogSearchWordEntity> findTop10ByOrderByCntDesc();
}
