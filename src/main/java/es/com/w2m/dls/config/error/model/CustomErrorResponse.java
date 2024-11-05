package es.com.w2m.dls.config.error.model;

import es.com.w2m.dls.config.error.constants.ErrorCategory;
import lombok.*;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {
//    @Schema(
//            title = "Codigo de error de Sistema",
//            example = "BE0001"
//    )
    private String code;
//    @Schema(
//            title = "Descripcion del error de Sistema",
//            example = "Error al llamar al servicio"
//    )
    private String description;
//    @Schema(
//            title = "Tipo de Error de Sistema",
//            example = "TECHNICAL"
//    )
    private String errorType;
//    @Schema(
//            title = "Lista de detalles del error"
//    )
    private List<CustomErrorDetail> exceptionDetails;
//    @Schema(
//            title = "Categoria del error",
//            example = "INVALID_REQUEST"
//    )
//    @JsonProperty
    private ErrorCategory category;

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    public static class CustomErrorDetail {
        //    @Schema(
//            title = "Codigo de error del Detalle/Proveedor",
//            example = "BE0008"
//    )
        private String code;
        //    @Schema(
//            title = "Nombre del componente de falla",
//            example = "support-example"
//    )
        private String component;
        //    @Schema(
//            title = "Descripcion del Detalle",
//            example = "Codigo invalido para el canal"
//    )
        private String description;
        private boolean resolved;
        //    @Schema(
//            title = "Endpoint que ejecuta el servicio",
//            example = "(GET) /support-example/endpoint/v1/resource"
//    )
        private String endpoint;
    }

}
