package blogify.ext;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AccessLevel;
import lombok.Setter;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

public abstract class AbstractClient implements InitializingBean {

	private static final int DEFAULT_CONNECT_TIMEOUT = 3000; // 3 Seconds
	private static final long DEFAULT_READ_TIMEOUT = 5000L; // 5 Seconds
	private static final long DEFAULT_WRITE_TIMEOUT = 5000L; // 5 Seconds

	private static final ExchangeStrategies UNLIMITED_EXCHANGE_STRATEGIES =
			ExchangeStrategies.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)).build();

	private final WebClient.Builder webClientBuilder =
			WebClient.builder().exchangeStrategies(UNLIMITED_EXCHANGE_STRATEGIES);

	@Setter(value = AccessLevel.PRIVATE)
	private WebClient webClient;

	@Override
	public final void afterPropertiesSet() throws Exception {
		init(webClientBuilder);
		setWebClient(webClientBuilder.build());
	}

	protected abstract void init(WebClient.Builder builder);

	protected HttpClient createHttpClient(String name, TimeoutProperties timeout) {
		HttpClient client = HttpClient.create(ConnectionProvider.builder("Connector-" + name + "-Provider")
																.maxConnections(Integer.MAX_VALUE)
																.pendingAcquireTimeout(Duration.ofMillis(0))
																.pendingAcquireMaxCount(-1)
																.build());
		if ( timeout != null ) {
			client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, defaultConnectTimeout(timeout.getConnect()))
					.doOnConnected(connection -> {
						connection.addHandlerFirst(new ReadTimeoutHandler(defaultReadTimeout(timeout.getRead()), TimeUnit.MILLISECONDS));
						connection.addHandlerFirst(new WriteTimeoutHandler(defaultWriteTimeout(timeout.getWrite()), TimeUnit.MILLISECONDS));
					});
		}
		return client;
	}

	private int defaultConnectTimeout(Integer connectTimeout) {
		return connectTimeout != null ? connectTimeout : DEFAULT_CONNECT_TIMEOUT;
	}

	private long defaultReadTimeout(Integer readTimeout) {
		return readTimeout != null ? readTimeout : DEFAULT_READ_TIMEOUT;
	}

	private long defaultWriteTimeout(Integer writeTimeout) {
		return writeTimeout != null ? writeTimeout : DEFAULT_WRITE_TIMEOUT;
	}

	public RequestHeadersUriSpec<?> get() {
		return webClient.get();
	}
}
