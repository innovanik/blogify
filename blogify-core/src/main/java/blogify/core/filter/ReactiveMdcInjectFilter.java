package blogify.core.filter;

import org.slf4j.MDC;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

public class ReactiveMdcInjectFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		MDC.clear();
		MDC.put("id", exchange.getRequest().getId());
		return chain.filter(exchange);
	}

}
