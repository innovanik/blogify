package blogify.ext.kakao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import blogify.ext.AbstractClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableConfigurationProperties(KakaoProperties.class)
public class KakaoClient extends AbstractClient {

	@Autowired
	private KakaoProperties properties;

	@Override
	protected void init(Builder builder) {
		log.info("[Kakao] url           - {}", properties.getUrl());
		log.info("[Kakao] authorization - {}", properties.getAuthorization());
		builder.baseUrl(properties.getUrl())
				.clientConnector(new ReactorClientHttpConnector(createHttpClient("Kakao", properties.getTimeout())))
				.defaultHeader(HttpHeaders.AUTHORIZATION, properties.getAuthorization());
	}
}
