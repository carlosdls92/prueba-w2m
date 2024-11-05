package es.com.w2m.dls.config.error.constants;

import es.com.w2m.dls.utils.PropertyUtils;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.util.ClassUtils;

import java.net.*;
import java.util.concurrent.TimeoutException;

public enum ErrorCategory {
    INVALID_REQUEST("invalid-request", 400),
    ARGUMENT_MISMATCH("argument-mismatch", 400),
    UNAUTHORIZED("unauthorized", 401),
    FORBIDDEN("forbidden", 403),
    RESOURCE_NOT_FOUND("resource-not-found", 404),
    CONFLICT("conflict", 409),
    PRECONDITION_FAILED("precondition-failed", 412),
    BUSINESS_ERROR("invalid-request", 422),
    EXTERNAL_ERROR("external-error", 500),
    HOST_NOT_FOUND("host-not-found", 500),
    UNEXPECTED("unexpected", 500),
    NOT_IMPLEMENTED("not-implemented", 501),
    SERVICE_UNAVAILABLE("service-unavailable", 503),
    EXTERNAL_TIMEOUT("external-timeout", 503);

    private static final String PROPERTY_PREFIX = "application.bespin.error-code.";
    private final String property;
    private final int httpStatus;

    private ErrorCategory(String property, int httpStatus) {
        this.property = "application.bespin.error-code.".concat(property);
        this.httpStatus = httpStatus;
    }

    private String codeProperty() {
        return this.property + ".code";
    }

    private String descriptionProperty() {
        return this.property + ".description";
    }

    private String defaultCode() {
        return "BE9999";
    }

    private String defaultDescription() {
        return "General Exception.";
    }

    public String getCode() {
        return (String) PropertyUtils.getOptionalValue(this.codeProperty()).orElseGet(this::defaultCode);
    }

    public String getDescription() {
        return (String)PropertyUtils.getOptionalValue(this.descriptionProperty()).orElseGet(this::defaultDescription);
    }

    public static ErrorCategory mapExceptionToCategory(Exception ex) {
        Class<? extends Exception> exClass = ex.getClass();
        if (!ClassUtils.isAssignable(exClass, UnknownHostException.class) && !ClassUtils.isAssignable(exClass, NoRouteToHostException.class) && !ClassUtils.isAssignable(exClass, MalformedURLException.class) && !ClassUtils.isAssignable(exClass, URISyntaxException.class)) {
            if (!ClassUtils.isAssignable(exClass, SocketTimeoutException.class) && !ClassUtils.isAssignable(exClass, SocketException.class) && !ClassUtils.isAssignable(exClass, TimeoutException.class)) {
                return ClassUtils.isAssignable(exClass, ConnectException.class) ? SERVICE_UNAVAILABLE : UNEXPECTED;
            } else {
                return EXTERNAL_TIMEOUT;
            }
        } else {
            return HOST_NOT_FOUND;
        }
    }

    public HttpStatus fromCategoryToHttpStatus() {
        return HttpStatus.valueOf(this.getHttpStatus());
    }

    @Generated
    public int getHttpStatus() {
        return this.httpStatus;
    }
}
