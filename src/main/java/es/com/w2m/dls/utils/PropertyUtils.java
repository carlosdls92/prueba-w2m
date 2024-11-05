package es.com.w2m.dls.utils;

import org.springframework.core.env.PropertyResolver;

import java.util.Optional;

public final class PropertyUtils {
    private static final String APPLICATION_CODE_PROPERTY = "spring.application.name";
    private static final String DEFAULT_APPLICATION_CODE = "unknown";
    private static PropertyResolver resolver;

    public static void setResolver(PropertyResolver resolver) {
        PropertyUtils.resolver = resolver;
    }

    public static String getValue(String property) {
        return resolver.getProperty(property);
    }

    public static <T> T getValue(String property, Class<T> resolvedClazz) {
        return resolver.getProperty(property, resolvedClazz);
    }

    public static <T> Optional<T> getOptionalValue(String property, Class<T> resolvedClazz) {
        return Optional.ofNullable(resolver).map((resp) -> {
            return resp.getProperty(property, resolvedClazz);
        });
    }

    public static Optional<String> getOptionalValue(String property) {
        return Optional.ofNullable(resolver).map((resp) -> {
            return resp.getProperty(property);
        });
    }

    public static String getApplicationCode() {
        return (String)getOptionalValue("spring.application.name").orElse("unknown");
    }

    public static String getQueueName(String queueUrl) {
        String[] segments = queueUrl.split("/");
        return segments[segments.length - 1];
    }

}
