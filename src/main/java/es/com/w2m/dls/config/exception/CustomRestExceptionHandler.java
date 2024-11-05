package es.com.w2m.dls.config.exception;

import es.com.w2m.dls.config.error.CustomException;
import es.com.w2m.dls.config.error.RecordNotFoundException;
import es.com.w2m.dls.config.error.constants.ErrorCategory;
import es.com.w2m.dls.config.error.model.CustomErrorResponse;
import es.com.w2m.dls.utils.PropertyUtils;
import es.com.w2m.dls.utils.WebRequestUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomRestExceptionHandler {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<CustomErrorResponse> handlerMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String exName = Optional.of(ex)
                .map(MethodArgumentTypeMismatchException::getName)
                .orElse("");

        String error = exName + " should be of type " + Optional.of(ex)
                .map(TypeMismatchException::getRequiredType)
                .map(Objects::requireNonNull)
                .map(x->(Class) x)
                .map(Class::getName)
                .orElse("");
        CustomErrorResponse errorResponse = CustomErrorResponse
                .builder()
                .code("BE0001")
                .errorType("Functional")
                .category(ErrorCategory.INVALID_REQUEST)
                .description("Los datos proporcionados son incorrectos")
                .exceptionDetails(Arrays.asList(CustomErrorResponse.CustomErrorDetail
                        .builder()
                        .component(PropertyUtils.getApplicationCode())
                        .description(error)
                        .endpoint(WebRequestUtils.getEndpoint(request))
                        .build()))
                .build();
        return ResponseEntity.status(errorResponse.getCategory().fromCategoryToHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<CustomErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        String component = PropertyUtils.getApplicationCode();
        String endPoint = WebRequestUtils.getEndpoint(request);
        List<CustomErrorResponse.CustomErrorDetail> CustomErrorDetails = new ArrayList();
        Iterator var6 = ex.getConstraintViolations().iterator();

        while(var6.hasNext()) {
            ConstraintViolation<?> violation = (ConstraintViolation)var6.next();
            String name = (String)Optional.ofNullable(violation.getMessageTemplate()).orElse(violation.getRootBeanClass().getName());
            CustomErrorDetails.add(CustomErrorResponse.CustomErrorDetail.builder().component(component).endpoint(endPoint).description(name).build());
        }

        CustomErrorResponse customErrorResponse = CustomErrorResponse
                .builder()
                .code("BE0001")
                .errorType("Technical")
                .category(ErrorCategory.INVALID_REQUEST)
                .description("Ha Ocurrido un Error en las cabeceras")
                .exceptionDetails(CustomErrorDetails)
                .build();

        return ResponseEntity.status(customErrorResponse.getCategory().fromCategoryToHttpStatus()).body(customErrorResponse);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<CustomErrorResponse> handleDefault(Exception ex, WebRequest request) {
        log.error(ex.toString());
        CustomErrorResponse errorResponse = CustomErrorResponse
                .builder()
                .code("BE9999")
                .errorType("Technical")
                .description("Ocurrio un error inesperado")
                .description(WebRequestUtils.getEndpoint(request))
                .category(ErrorCategory.mapExceptionToCategory(ex))
                .build();
        return ResponseEntity.status(errorResponse.getCategory().fromCategoryToHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<Object> handleCustomException(CustomException ex, WebRequest request) {
        CustomErrorResponse errorResponse = CustomErrorResponse
                .builder()
                .code(ex.getCode())
                .errorType(ex.getErrorType())
                .category(ex.getCategory())
                .description(ex.getDescription())
                .exceptionDetails(this.toList(ex.getExceptionDetails(), WebRequestUtils.getEndpoint(request)))
                .build();
        log.error(errorResponse.toString());
        HttpStatusCode status = errorResponse.getCategory().fromCategoryToHttpStatus();
        ResponseEntity<Object> response = ResponseEntity.status(status).body(errorResponse);
        return response;
    }

//    @ExceptionHandler(RecordNotFoundException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    public CustomErrorResponse handleNull(RecordNotFoundException exception) {
//        String message = exception.getMessage();
//        String error = exception.getMessage();
//        return new FlashCustomError(HttpStatus.BAD_REQUEST, message, error, LocalDateTime.now().toString(), HttpStatus.BAD_REQUEST.value());
//    }

    private List<CustomErrorResponse.CustomErrorDetail> toList(List<CustomException.CustomExceptionDetail> exceptionDetails, String endpoint) {
        return Optional.ofNullable(exceptionDetails)
                .map(ex-> ex.stream()
                            .map(bE-> this.to(bE, endpoint))
                            .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    private CustomErrorResponse.CustomErrorDetail to(CustomException.CustomExceptionDetail detail, String endpoint) {
        return CustomErrorResponse.CustomErrorDetail.builder().code(detail.getCode()).component(detail.getComponent()).description(detail.getDescription()).endpoint(endpoint).resolved(detail.isResolved()).build();
    }

}
