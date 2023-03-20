package blogify.ext.naver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import blogify.ext.AbstractClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableConfigurationProperties(NaverProperties.class)
public class NaverClient extends AbstractClient {

	@Autowired
	private NaverProperties properties;

	@Override
	protected void init(Builder builder) {
		log.info("[Naver] url           - {}", properties.getUrl());
		log.info("[Naver] authorization - {}", properties.getAuthorization());
		builder.baseUrl(properties.getUrl())
				.clientConnector(new ReactorClientHttpConnector(createHttpClient("Naver", properties.getTimeout())));
		if ( properties.getAuthorization() != null ) {
			properties.getAuthorization()
						.stream()
						.forEach(map -> builder.defaultHeader(map.get("name"), map.get("value")));
		}
	}
}
