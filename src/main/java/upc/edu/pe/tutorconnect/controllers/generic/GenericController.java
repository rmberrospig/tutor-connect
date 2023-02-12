package upc.edu.pe.tutorconnect.controllers.generic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import upc.edu.pe.tutorconnect.controllers.commons.ResponseREST;
import upc.edu.pe.tutorconnect.controllers.commons.ResponseStatus;
import upc.edu.pe.tutorconnect.controllers.constants.ResponseConstant;
import upc.edu.pe.tutorconnect.controllers.enums.ResponseEnums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenericController {

    @Value("${api.version}")
    private String apiVersion;

    protected String formatMapMessage(BindingResult result) {
        List<Map<String, String>> errors =
                result.getFieldErrors().stream().map(err ->
                        {
                            Map<String, String> error = new HashMap<>();
                            error.put(err.getField(), err.getDefaultMessage());
                            return error;
                        }

                ).collect(Collectors.toList());
        return errors.toString();
    }

    protected List<Map<String, String>> formatMapMessageList(BindingResult result) {
        return result.getFieldErrors().stream().map(err ->
                {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }

        ).collect(Collectors.toList());
    }

    protected ResponseEntity<ResponseREST> getBadRequest(BindingResult result){
        ResponseREST res= 	ResponseREST.builder()
                .apiVersion(apiVersion)
                .status(
                        ResponseStatus.builder()
                                .code(ResponseEnums.ALERT)
                                .message(ResponseConstant.MSG_ALERT)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.name())
                                .details(this.formatMapMessageList(result))
                                .build())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    protected ResponseEntity<ResponseREST> getBadRequest(String details){
        ResponseREST res= 	ResponseREST.builder()
                .apiVersion(apiVersion)
                .status(
                        ResponseStatus.builder()
                                .code(ResponseEnums.ALERT)
                                .message(ResponseConstant.MSG_ERROR)
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error(HttpStatus.BAD_REQUEST.name())
                                .details(details)
                                .build())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    protected ResponseEntity<ResponseREST> getNotFoundRequest(){
        ResponseREST res= 	ResponseREST.builder()
                .apiVersion(apiVersion)
                .status(
                        ResponseStatus.builder()
                                .code(ResponseEnums.ALERT)
                                .message(ResponseConstant.MSG_NOT_FOUND)
                                .status(HttpStatus.NOT_FOUND.value())
                                .error(HttpStatus.NOT_FOUND.name())
                                .build())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    protected ResponseEntity<ResponseREST> getNotContentRequest(){
        ResponseREST res= 	ResponseREST.builder()
                .apiVersion(apiVersion)
                .status(
                        ResponseStatus.builder()
                                .code(ResponseEnums.ALERT)
                                .message(ResponseConstant.MSG_NOT_CONTENT)
                                .status(HttpStatus.NO_CONTENT.value())
                                .error(HttpStatus.NO_CONTENT.name())
                                .build())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    protected ResponseEntity<ResponseREST> getCreatedRequest(Object obj){
        ResponseREST res= 	ResponseREST.builder()
                .apiVersion(apiVersion)
                .status(
                        ResponseStatus.builder()
                                .code(ResponseEnums.SUCCESS)
                                .message(ResponseConstant.MSG_SUCCESS)
                                .status(HttpStatus.OK.value())
                                .error(HttpStatus.OK.name())
                                .build())
                .result(obj)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    protected ResponseEntity<ResponseREST> getSuccessRequest(Object obj){
        ResponseREST res= 	ResponseREST.builder()
                .apiVersion(apiVersion)
                .status(
                        ResponseStatus.builder()
                                .code(ResponseEnums.SUCCESS)
                                .message(ResponseConstant.MSG_SUCCESS)
                                .status(HttpStatus.OK.value())
                                .error(HttpStatus.OK.name())
                                .build())
                .result(obj)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    public ResponseEntity<ResponseREST> getErrorRequest() {
        ResponseREST res= 	ResponseREST.builder()
                .apiVersion(apiVersion)
                .status(
                        ResponseStatus.builder()
                                .code(ResponseEnums.ERROR)
                                .message(ResponseConstant.MSG_ERROR)
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                                .build())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
