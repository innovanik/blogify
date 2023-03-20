package blogify.app.api.exception;

import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WebClientResponseException extends WebClientException {

	private static final long serialVersionUID = 3946523554241361716L;

	private final ClientResponse response;

	private final String responseBody;

	public WebClientResponseException(String msg, ClientResponse response, String responseBody) {
		super(msg);
		this.response = response;
		this.responseBody = responseBody;
	}

	public WebClientResponseException(String msg, Throwable ex, ClientResponse response, String responseBody) {
		super(msg, ex);
		this.response = response;
		this.responseBody = responseBody;
	}

	public WebClientResponseException(Throwable ex, ClientResponse response, String responseBody) {
		this(ex.getMessage(), ex, response, responseBody);
	}
}
