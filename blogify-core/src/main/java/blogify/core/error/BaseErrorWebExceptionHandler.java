package blogify.core.error;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import blogify.core.context.AbstractResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Mono;

public class BaseErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

	public BaseErrorWebExceptionHandler(ErrorAttributes errorAttributes, Resources resources,
			ErrorProperties errorProperties, ApplicationContext applicationContext) {
		super(errorAttributes, resources, errorProperties, applicationContext);
	}

	@Override
	protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		ErrorResult result = new ErrorResult();
		result.setMessage(getError(request).getClass().getSimpleName());
		return ServerResponse.status(getHttpStatus(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL))))
								.contentType(MediaType.APPLICATION_JSON)
								.body(BodyInserters.fromValue(result));
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	static class ErrorResult extends AbstractResult {

		private static final long serialVersionUID = 3429755770698554970L;
	}
}
