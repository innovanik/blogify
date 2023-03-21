package blogify.app.api;

import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.result.view.ViewResolver;

import blogify.core.error.BaseErrorWebExceptionHandler;
import blogify.core.filter.ReactiveMdcInjectFilter;
import blogify.res.r2dbc.H2ConsoleManualConfiguration;

@SpringBootApplication
@Import({
	H2ConsoleManualConfiguration.class
})
@EnableTransactionManagement
public class ApiApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	}

	@Configuration(proxyBeanMethods = false)
	static class ReactiveConfiguration {

		@Autowired
		private ServerProperties serverProperties;

		@Bean
		@Order(Ordered.HIGHEST_PRECEDENCE)
		ReactiveMdcInjectFilter reactiveMdcInjectFilter() {
			return new ReactiveMdcInjectFilter();
		}

		@Bean
		@Order(-1)
		public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes,
				WebProperties webProperties, ObjectProvider<ViewResolver> viewResolvers,
				ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext) {
			BaseErrorWebExceptionHandler exceptionHandler = new BaseErrorWebExceptionHandler(errorAttributes,
					webProperties.getResources(), this.serverProperties.getError(), applicationContext);
			exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
			exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
			exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
			return exceptionHandler;
		}
	}
}
