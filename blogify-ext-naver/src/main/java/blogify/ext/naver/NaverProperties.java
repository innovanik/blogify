package blogify.ext.naver;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import blogify.ext.TimeoutProperties;
import lombok.Data;

@ConfigurationProperties(prefix = "naver")
@Data
class NaverProperties {

	private String url;

	private List<Map<String, String>> authorization;

	private TimeoutProperties timeout;
}
