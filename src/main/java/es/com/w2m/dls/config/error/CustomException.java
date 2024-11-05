package es.com.w2m.dls.config.error;

import es.com.w2m.dls.config.error.constants.ErrorCategory;
import lombok.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private String code;
    private String description;
    private String errorType;
    private List<CustomExceptionDetail> exceptionDetails;
    private ErrorCategory category;

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    public static class CustomExceptionDetail {
//        @Schema(
//                title = "Codigo de error del Detalle/Proveedor",
//                example = "BE0008"
//        )
        private String code;
//        @Schema(
//                title = "Nombre del componente de falla",
//                example = "support-example"
//        )
        private String component;
//        @Schema(
//                title = "Descripcion del Detalle",
//                example = "Codigo invalido para el canal"
//        )
        private String description;
        private boolean resolved;
//        @Schema(
//                title = "Endpoint que ejecuta el servicio",
//                example = "(GET) /support-example/endpoint/v1/resource"
//        )
        private String endpoint;
    }

    public static CustomException notFound() {
        return builder()
                .code(null)
                .description(null)
                .errorType(null)
//                .exceptionDetails(Collections.emptyList())
                .exceptionDetails(Collections.singletonList(CustomExceptionDetail
                        .builder()
                        .resolved(true)
                        .description(ErrorCategory.RESOURCE_NOT_FOUND.getDescription())
                        .build()))
                .category(ErrorCategory.RESOURCE_NOT_FOUND)
                .build();
    }
}
