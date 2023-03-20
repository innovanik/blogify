package blogify.ext.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

import blogify.ext.TimeoutProperties;
import lombok.Data;

@ConfigurationProperties(prefix = "kakao")
@Data
class KakaoProperties {

	private String url;

	private String authorization;

	private TimeoutProperties timeout;
}
