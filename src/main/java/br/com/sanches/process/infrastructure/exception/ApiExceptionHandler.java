package br.com.sanches.process.infrastructure.exception;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import br.com.sanches.process.infrastructure.exception.dto.ExceptionResponseDTO;
import br.com.sanches.process.infrastructure.exception.dto.MessageExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;

@ControllerAdvice
public class ApiExceptionHandler {

	public static final String DASH = " - ";
	public static final String ERROR_IN_INPUT_FIELDS = "Erro em campos de entrada.";
	public static final String UNEXPECTED_ERROR = "Erro inesperado";
	public static final String REQUEST_ERROR = "Erro de requisição ";
	public static final String DATA_ENTRY_ERROR = "Erro na entrada de dados";
	public static final String RECORD_NOT_FOUND = "Registro não encontrado";
	public static final String INTEGRATION_ERROR = "Erro de integração";
	public static final String THE_RECORD_CAN_NOT_BE_DELETED = "O registo não pode ser excluído.";
	public static final String DATABASE_INSTABILITY = "Instabilidade no banco de dados";
	public static final String ILLEGAL_ARGUMENT_EXCEPTION = "Argumento inválido";
	public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
	public static final String DELIMITER = ".";
	public static final String HANDLE_INVALID_FORMAT = "A propriedade '%s' recebeu o valor '%s', que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.";
	public static final String HANDLE_PROPERTY_BINDING = "A propriedade '%s' não existe. Corrija ou remova essa propriedade e tente novamente.";
	public static final String HANDLE_METHOD_ARGUMENT_TYPE_MISMATCH = "O parâmetro de URL '%s' recebeu o valor '%s', que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.";

	@ResponseBody
	@ExceptionHandler({ HttpMediaTypeNotAcceptableException.class })
	public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException exception) {
		HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
		return ResponseEntity.status(status).body(getResponse(exception.getMessage(), status.value()));
	}

	private ExceptionResponseDTO getResponse(String message, Integer statusCode) {
		return ExceptionResponseDTO.builder().message(message).statusCode(statusCode).build();
	}

	@ResponseBody
	@ExceptionHandler({ BindException.class })
	public ResponseEntity<Object> handleBindException(BindException exception) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return ResponseEntity.status(status).body(getResponse(exception.getMessage(), status.value()));
	}

	@ResponseBody
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		String detail = String.format(HANDLE_METHOD_ARGUMENT_TYPE_MISMATCH, ex.getName(), ex.getValue(),
				ex.getRequiredType());

		return ResponseEntity.status(status).body(getResponse(detail, status.value()));
	}

	@ResponseBody
	@ExceptionHandler({ PropertyBindingException.class })
	public ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		String path = joinPath(ex.getPath());
		String detail = String.format(HANDLE_PROPERTY_BINDING, path);

		return ResponseEntity.status(status).body(getResponse(detail, status.value()));
	}

	@ResponseBody
	@ExceptionHandler({ InvalidFormatException.class })
	public ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		String path = joinPath(ex.getPath());
		String detail = String.format(HANDLE_INVALID_FORMAT, path, ex.getValue(), ex.getTargetType().getSimpleName());

		return ResponseEntity.status(status).body(getResponse(detail, status.value()));
	}

	@ResponseBody
	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Object> handleEntidadeNaoEncontrada(AccessDeniedException ex) {
		HttpStatus status = HttpStatus.NOT_FOUND;

		return ResponseEntity.status(status).body(getResponse(ex.getMessage(), status.value()));
	}

	@ResponseBody
	@ExceptionHandler({ BusinessException.class })
	public ResponseEntity<Object> handleBusiness(BusinessException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		return ResponseEntity.status(status).body(getResponse(ex.getMessage(), status.value()));
	}

	private String joinPath(List<Reference> references) {
		return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining(DELIMITER));
	}

	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MessageExceptionHandler> invalidArguments(MethodArgumentNotValidException notValid) {

		BindingResult result = notValid.getBindingResult();

		MessageExceptionHandler error = new MessageExceptionHandler(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				ERROR_IN_INPUT_FIELDS);

		for (FieldError erros : result.getFieldErrors()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(erros.getField(), erros.getDefaultMessage());
			error.getErros().add(jsonObject);
		}

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<MessageExceptionHandler> standardError(Exception e, HttpServletRequest request) {

		MessageExceptionHandler error = createMessage(e, INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR,
				HttpStatus.INTERNAL_SERVER_ERROR);

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<MessageExceptionHandler> errorWhenMappingJson(HttpMessageNotReadableException e,
			HttpServletRequest request) {

		MessageExceptionHandler error = createMessage(e, DATA_ENTRY_ERROR, DATA_ENTRY_ERROR, HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<MessageExceptionHandler> sqlError(SQLException e, HttpServletRequest request) {

		MessageExceptionHandler error = createMessage(e, INTERNAL_SERVER_ERROR, DATABASE_INSTABILITY,
				HttpStatus.INTERNAL_SERVER_ERROR);

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<MessageExceptionHandler> illegalArgumentExceptionError(IllegalArgumentException e,
			HttpServletRequest request) {

		MessageExceptionHandler error = createMessage(e, ILLEGAL_ARGUMENT_EXCEPTION, ILLEGAL_ARGUMENT_EXCEPTION,
				HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	private MessageExceptionHandler createMessage(Exception e, String message, String errorType,
			HttpStatus httpStatus) {
		MessageExceptionHandler error = new MessageExceptionHandler(LocalDateTime.now(), httpStatus.value(), message);

		String translatedMessage = errorType + DASH + e.getMessage();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put(message, translatedMessage);
		error.getErros().add(jsonObject);
		return error;
	}

}