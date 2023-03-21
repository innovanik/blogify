package blogify.core.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MessageAccessor {

	private static final ResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();
	private static final String[] CLASSPATH_PREFIXES = new String[] {
		".jar!/", "/classes!/", "/WEB-INF/classes/", "/target/classes/", "/bin/main/", "/bin/test/"
	};
	private static final String CLASSPATH_PATTERN_ALL = "classpath*:";
	private static final String EXTENSION_PROPERTIES = "properties";
	private static final String MESSAGES_BASENAME_LOCALE_SEPARATOR = "_";

	private static MessageSourceAccessor messageSourceAccessor;

	public static void setAccessor(MessageSourceAccessor accessor) {
		messageSourceAccessor = accessor;
	}

	public static String getMessage(String code) {
		return messageSourceAccessor.getMessage(code, getLocale());
	}

	public static String getMessage(String code, String defaultMessage) {
		return messageSourceAccessor.getMessage(code, defaultMessage);
	}

	public static String getMessage(String code, Object[] args) {
		return messageSourceAccessor.getMessage(code, args, getLocale());
	}

	public static String getMessage(String code, Object[] args, String defaultMessage) {
		return messageSourceAccessor.getMessage(code, args, defaultMessage);
	}

	public static Locale getLocale() {
		return Locale.getDefault();
	}

	public static Set<String> getMessagesBasenames(String value) {
		final String extension = getExtension(EXTENSION_PROPERTIES);
		final Set<String> messagesBasenames = new LinkedHashSet<>();

		Arrays.stream(StringUtils.split(value, ","))
				.map(pattern -> StringUtils.trimToNull(pattern))
				.filter(pattern -> pattern != null)
				.flatMap(pattern -> {
					String locationPattern = StringUtils.appendIfMissing(StringUtils.prependIfMissing(pattern, CLASSPATH_PATTERN_ALL, CLASSPATH_PATTERN_ALL), extension, extension);
					List<Resource> resources = new ArrayList<>();
					try {
						resources.addAll(getResources(locationPattern));
					} catch (IOException e) {
						log.warn("The resources of the pattern '{}' does not exist.", pattern);
					}
					return resources.stream();
				})
				.map(resource -> {
					try {
						return resource.getURL().getPath();
					} catch (IOException e) {
						return null;
					}
				})
				.filter(path -> path != null)
				.forEach(path -> Arrays.stream(CLASSPATH_PREFIXES)
										.map(prefix -> {
											int lastIndex = StringUtils.lastIndexOf(path, prefix);
											return lastIndex > -1 ? lastIndex + StringUtils.length(prefix) : lastIndex;
										})
										.max((index1, index2) -> Integer.compare(index1, index2))
										.map(index -> StringUtils.substring(path, index))
										.map(messagesBasename -> StringUtils.removeEnd(messagesBasename, extension))
										.map(messagesBasename -> StringUtils.substringBefore(messagesBasename, MESSAGES_BASENAME_LOCALE_SEPARATOR))
										.ifPresent(messagesBasename -> messagesBasenames.add(messagesBasename)));
		log.debug("Get the base name of message source from resources: {}", messagesBasenames);
		return messagesBasenames;
	}

	private static List<Resource> getResources(String locationPattern) throws IOException {
		return Arrays.asList(RESOLVER.getResources(locationPattern));
	}

	private static String getExtension(String type) {
		return "." + type;
	}
}
