package upc.edu.pe.tutorconnect.controllers.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseREST {
    private String apiVersion;
    private ResponseStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object result;
}
