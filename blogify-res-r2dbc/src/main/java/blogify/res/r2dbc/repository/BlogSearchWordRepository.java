package blogify.res.r2dbc.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import blogify.res.r2dbc.entity.BlogSearchWordEntity;
import reactor.core.publisher.Flux;

public interface BlogSearchWordRepository extends R2dbcRepository<BlogSearchWordEntity, Long> {

	Flux<BlogSearchWordEntity> findByWord(String word);

    @Query(value = "select WORD, sum(CNT) as CNT from BLOG_SEARCH_WORD BSW group by WORD order by CNT desc limit 10")
	Flux<BlogSearchWordEntity> findGroupByTop10ByOrderByCntDesc();
}
