package blogify.core.error;

import java.util.Optional;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import blogify.core.context.AbstractResult;
import blogify.core.context.MessageAccessor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Mono;

public class BaseErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

	private static final String BASE_ERROR_MESSAGE_CODE_PREFIX = "base.error.";

	public BaseErrorWebExceptionHandler(ErrorAttributes errorAttributes, Resources resources,
			ErrorProperties errorProperties, ApplicationContext applicationContext) {
		super(errorAttributes, resources, errorProperties, applicationContext);
	}

	@Override
	protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		ErrorResult result = new ErrorResult();
		result.setMessage(getMessage(getError(request)));
		return ServerResponse.status(getHttpStatus(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL))))
								.contentType(MediaType.APPLICATION_JSON)
								.body(BodyInserters.fromValue(result));
	}

	private String getMessage(Throwable error) {
		if ( error instanceof WebExchangeBindException ) {
			return Optional.of(((WebExchangeBindException) error).getFieldError())
							.map(fieldError -> fieldError.getDefaultMessage() + " (" + fieldError.getField() + ")")
							.get();
		}
		return MessageAccessor.getMessage(BASE_ERROR_MESSAGE_CODE_PREFIX + error.getClass().getSimpleName(), error.getMessage());
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	static class ErrorResult extends AbstractResult {

		private static final long serialVersionUID = 3429755770698554970L;
	}
}
