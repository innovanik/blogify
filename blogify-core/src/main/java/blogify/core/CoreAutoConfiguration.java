package blogify.core;

import java.util.Set;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

import blogify.core.context.MessageAccessor;

@Configuration(proxyBeanMethods = false)
@ImportAutoConfiguration(exclude = {
	org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration.class,
}, value = {
	CoreAutoConfiguration.MessageSourceAutoConfiguration.class,
})
public class CoreAutoConfiguration {

	static class MessageSourceAutoConfiguration extends org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration {

		@Override
		public MessageSource messageSource(MessageSourceProperties properties) {
			Set<String> basenames = MessageAccessor.getMessagesBasenames(properties.getBasename());
			properties.setBasename(String.join(",", basenames.toArray(new String[] {})));
			return super.messageSource(properties);
		}

		@Bean
		public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
			MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
			MessageAccessor.setAccessor(messageSourceAccessor);
			return messageSourceAccessor;
		}
	}
}
