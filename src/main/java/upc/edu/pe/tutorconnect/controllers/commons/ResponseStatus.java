package upc.edu.pe.tutorconnect.controllers.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import upc.edu.pe.tutorconnect.controllers.enums.ResponseEnums;

import java.util.Date;

@JsonPropertyOrder({"code", "message","details","status", "error", "dateTime" })
@Data
@Builder
public class ResponseStatus {

    @Builder.Default
    private Date dateTime= new Date(); //"2021-02-14T18:10:49.440+00:00"
    private Integer status; //"status": 500
    private String error; // "error": "Internal Server Error"
    private ResponseEnums code;
    private Object message; // "message": "No message available"
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object 	details;
}
